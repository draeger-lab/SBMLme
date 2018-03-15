package sbmlme.converter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.Objective;
import org.sbml.jsbml.ext.groups.Group;
import org.sbml.jsbml.ext.groups.GroupsConstants;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import sbmlme.MEConstants;
import sbmlme.MEProcessData;
import sbmlme.MEReactionPlugin;
import sbmlme.MESpeciesPlugin;

/**
 * Contains methods for converting a COBRAme JSON file into a SBML model.
 * 
 * @author Marc A. Voigt
 */
@SuppressWarnings("restriction")
public class MEJsonToSBML implements MEConstants, MEJsonConstants {

  /**
   * Reads in the given COBRAme JSON file and converts it to the CombineArchive
   * of the SBMLme representation with the respective SBML and SBOL files.
   * 
   * @param jsonFile
   *        the JSON file of the COBRAme model
   * @param output
   *        the prefix of the output files
   * @param modelName
   *        the name of the model in SBML
   * @param validation
   *        whether the created SBML model should be validated, currently does
   *        not involve the validity of the SBMLme attributes.
   * @param tidy
   *        whether the documents should be printed tidy.
   * @throws IOException
   * @throws SBOLValidationException
   * @throws ParseException
   * @throws SBMLException
   * @throws XMLStreamException
   * @throws SBOLConversionException
   * @throws JDOMException
   * @throws java.text.ParseException
   * @throws CombineArchiveException
   * @throws URISyntaxException
   * @throws TransformerException
   */
  public MEJsonToSBML(String jsonFile, String output, String modelName,
    boolean validation, boolean tidy) throws IOException,
    SBOLValidationException, ParseException, SBMLException, XMLStreamException,
    SBOLConversionException, JDOMException, java.text.ParseException,
    CombineArchiveException, URISyntaxException, TransformerException {
    System.out.println("Start conversion from JSON to SBML/SBOL");
    String sbmlPrefix = prefix;
    String sbmlNamespace = ns;
    String uriSBOL = sbolURI;
    // lists for main classes in COBRAme
    List<JsonNode> reactions = new ArrayList<JsonNode>();
    List<JsonNode> metabolites = new ArrayList<JsonNode>();
    List<JsonNode> processData = new ArrayList<JsonNode>();
    JsonNode globalInfo = null;
    // helper map for mapping ProcessData Ids to indices in List<JsonNode>
    // processData for quickly connecting reaction with ProcessData object
    Map<String, Integer> processDataIndices = new HashMap<String, Integer>();
    // list for ProcessData objects which won't be used in the iteration through
    // the reactions list (StoichiometricData, SubreactionData,
    // TranslocationData)
    List<JsonNode> processDataSBML = new ArrayList<JsonNode>();
    // memory efficient JSON file reading
    JsonFactory factory = new MappingJsonFactory();
    JsonParser jsonParser = factory.createParser(new File(jsonFile));
    JsonToken currentToken;
    currentToken = jsonParser.nextToken();
    // test if root is object
    if (currentToken != JsonToken.START_OBJECT) {
      System.out.println("Error: root should be object: quiting.");
      return;
    }
    // initiate globalInfo for test
    // loop for adding nodes to lists
    while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
      String fieldName = jsonParser.getCurrentName();
      // get arrays from JSON
      currentToken = jsonParser.nextToken();
      // add reactions to reaction list
      if (fieldName.equals(reactionsField)) {
        System.out.println("reading reactions");
        if (currentToken == JsonToken.START_ARRAY) {
          // for each reaction in array
          while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            JsonNode tempNode = jsonParser.readValueAsTree();
            reactions.add(tempNode);
          }
        }
        // add metabolites to metabolite list
      } else if (fieldName.equals(metabolitesField)) {
        System.out.println("reading metabolites");
        if (currentToken == JsonToken.START_ARRAY) {
          // for each metabolite in array
          while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            JsonNode tempNode = jsonParser.readValueAsTree();
            metabolites.add(tempNode);
          }
        }
      } else if (fieldName.equals(processDataField)) {
        System.out.println("reading ProcessData");
        if (currentToken == JsonToken.START_ARRAY) {
          // for each ProcessData in array
          while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            JsonNode tempNode = jsonParser.readValueAsTree();
            // if entry can not be processed by reaction reference add it to
            // separate list
            if (tempNode.get(processDataType).has(StoichiometricData)
              || tempNode.get(processDataType).has(SubreactionData)
              || tempNode.get(processDataType).has(TranslocationData)) {
              processDataSBML.add(tempNode);
            } else {
              // includes generic Data although it will currently not be used
              processData.add(tempNode);
              processDataIndices.put(tempNode.get(id).textValue(),
                processData.size() - 1);
            }
          }
        }
      } else if (fieldName.equals(global_Info)) {
        globalInfo = jsonParser.readValueAsTree();
      }
    }
    System.out.println("reactions: " + String.valueOf(reactions.size()));
    System.out.println("metabolites: " + String.valueOf(metabolites.size()));
    System.out.println("processData: "
      + String.valueOf(processData.size() + processDataSBML.size()));
    // System.out.println(metabolites.get(1));
    // setting up documents
    // create new SBML and SBOL documents and set their namespaces
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.addDeclaredNamespace(sbmlPrefix, sbmlNamespace);
    SBOLDocument sbol = new SBOLDocument();
    sbol.setDefaultURIprefix(uriSBOL);
    // initialize model
    Model model = doc.createModel(modelName);
    model.initDefaults(3, 1);
    // create basic objective
    // Objective for flux coefficient
    FBCModelPlugin fbcModel =
      (FBCModelPlugin) model.getPlugin(FBCConstants.shortLabel);
    // needs to be false due to InitialAssignments for SpeciesReferences
    fbcModel.setStrict(false);
    /*
     * may require test for type if an attribute for this gets included into the
     * JSON file
     */
    Objective objective =
      fbcModel.createObjective("objective", Objective.Type.MAXIMIZE);
    fbcModel.setActiveObjective(objective);
    // basic groups package Implementation
    GroupsModelPlugin groups =
      (GroupsModelPlugin) model.getPlugin(GroupsConstants.shortLabel);
    // adding species from list of metabolites to SBML and SBOL documents
    System.out.println("adding Species to model");
    addSpeciesFromJSON(model, sbol, groups, metabolites);
    // add globalInfo to Parameters
    System.out.println("adding globalInfo to Parameters");
    addGlobalInfoFromJSON(model, globalInfo, groups);
    // add ProcessDataSBML entries to the model annotation
    System.out.println(
      "adding SubreactionData, TranslocationData and StoichiometricData to MEProcessData annotation");
    addMEProcessDataFromJSON(model, processDataSBML);
    // add reactions to documents
    System.out.println("adding reactions to model");
    addReactionsFromJSON(model, sbol, groups, objective, reactions, processData,
      processDataIndices);
    // write combine archive
    System.out.println("Reactions in SBML model: " + model.getReactionCount());
    System.out.println("Species in SBML model: " + model.getSpeciesCount());
    // Validation of SBML document (currently only for core, fbc and groups
    // attributes)
    if (validation) {
      System.out.println("Validate SBML document");
      doc.checkConsistencyOffline();
      System.out.println(doc.getErrorLog().getValidationErrors());
    }
    System.out.println("write model to CombineArchive");
    // write to CombineArchive
    if (tidy) {
      TidySBMLWriter tidySBMLWriter = new TidySBMLWriter();
      tidySBMLWriter.write(doc, output + ".sbml");
    } else {
      SBMLWriter SBMLWriter = new SBMLWriter();
      SBMLWriter.write(doc, output + ".sbml");
    }
    // SBOL Test
    sbol.write(output + ".sbol");
    CombineArchive ca = new CombineArchive(new File(output + ".zip"));
    ca.addEntry(new File(""), new File(output + ".sbml"),
      new URI("http://identifiers.org/combine.specifications/sbml"));
    ca.addEntry(new File(""), new File(output + ".sbol"),
      new URI("http://identifiers.org/combine.specifications/sbol"));
    ca.pack();
    ca.close();
  }


  /**
   * Adds all species from a list of JsonNodes to the SBML and SBOL models
   * according to their
   * type.
   * 
   * @param model
   *        the SBML model
   * @param sbol
   *        the SBOL document
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param metabolites
   *        the list of species from the JSON file
   * @throws SBOLValidationException
   */
  public void addSpeciesFromJSON(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, List<JsonNode> metabolites)
    throws SBOLValidationException {
    MESpeciesPlugin meSpeciesPlugin = new MESpeciesPlugin();
    for (JsonNode species : metabolites) {
      if (species.get(metaboliteType).has(transcribedGene)) {
        meSpeciesPlugin.createMESequenceSpecies(model,
          species.get(id).textValue(), species.get(formula).textValue(),
          species.get(name).textValue(), species.get(compartment).textValue(),
          sbol,
          species.get(metaboliteType).get(transcribedGene)
                 .get(nucleotide_sequence).textValue(),
          species.get(metaboliteType).get(transcribedGene).get(rna_type)
                 .textValue(),
          species.get(metaboliteType).get(transcribedGene).get(strand)
                 .textValue(),
          species.get(metaboliteType).get(transcribedGene).get(left_pos)
                 .asInt(),
          groups, transcribedGene);
      } else if (species.get(metaboliteType).has(processedProtein)) {
        meSpeciesPlugin.createMEProcessedSpecies(model,
          species.get(id).textValue(), species.get(formula).textValue(),
          species.get(name).textValue(), species.get(compartment).textValue(),
          species.get(metaboliteType).get(processedProtein)
                 .get(unprocessed_protein).textValue(),
          groups, processedProtein);
      } else {
        meSpeciesPlugin.createMESpecies(model, species.get(id).textValue(),
          species.get(formula).textValue(), species.get(name).textValue(),
          species.get(compartment).textValue(), groups,
          species.get(metaboliteType).toString().split("\"")[1]);
      }
    }
  }


  /**
   * Adds all global information from a JsonNode to the parameters in the SBML
   * model.
   * 
   * @param model
   *        The SBML model
   * @param globalInfo
   *        the JsonNode containing the global information
   * @param groups
   *        the GroupsModelPlugin of the model
   */
  public void addGlobalInfoFromJSON(Model model, JsonNode globalInfo,
    GroupsModelPlugin groups) {
    for (Iterator<Entry<String, JsonNode>> fnIterate =
      globalInfo.fields(); fnIterate.hasNext();) {
      Entry<String, JsonNode> globalInfoEntry = fnIterate.next();
      // append common identifier for better reverse processing
      String parameterName = globalInfoEntry.getKey();
      double parameterValue = globalInfoEntry.getValue().doubleValue();
      model.createParameter(parameterName);
      model.getParameter(parameterName).initDefaults(2, 4, true);
      model.getParameter(parameterName).setValue(parameterValue);
      // add parameter to group of GlobalInfo
      Group group = (Group) groups.getGroup(globalInformation);
      if (group != null) {
        group.createMemberWithIdRef(parameterName);
      } else {
        group = groups.createGroup(globalInformation);
        group.setKind(Group.Kind.classification);
        group.createMemberWithIdRef(parameterName);
      }
    }
  }


  /**
   * Adds ProcessData information from the list of reaction independent process
   * data of the JSON file. To be more specific the list of process data
   * includes all StoichiometricData, SubreactionData and TranslocationData
   * objects.
   * 
   * @param model
   *        the SBML model
   * @param processDataSBML
   *        the list of process data objects that do not encode information of a
   *        single reaction
   */
  public void addMEProcessDataFromJSON(Model model,
    List<JsonNode> processDataSBML) {
    MEProcessData meProcessData = new MEProcessData();
    meProcessData = meProcessData.createMEProcessData();
    for (JsonNode entry : processDataSBML) {
      if (entry.get(processDataType).has(StoichiometricData)) {
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        List<String> speciesReferences = new ArrayList<String>();
        List<Double> stoichiometries = new ArrayList<Double>();
        // fill map for SubreactionReferences
        for (Iterator<Entry<String, JsonNode>> subreactionEntry =
          entry.get(processDataType).get(StoichiometricData).get(subreactions)
               .fields(); subreactionEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = subreactionEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        // fill Lists for Stoichiometries
        for (Iterator<Entry<String, JsonNode>> stoichiometricEntry =
          entry.get(processDataType).get(StoichiometricData).get(stoichiometry_)
               .fields(); stoichiometricEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = stoichiometricEntry.next();
          speciesReferences.add(currentEntry.getKey());
          stoichiometries.add(currentEntry.getValue().asDouble());
        }
        // add StoichiometricData
        meProcessData.addStoichiometricData(meProcessData,
          entry.get(id).textValue(),
          entry.get(processDataType).get(StoichiometricData).get(lower_Bound)
               .asDouble(),
          entry.get(processDataType).get(StoichiometricData).get(upper_bound)
               .asDouble(),
          subreactionMap, speciesReferences, stoichiometries);
      } else if (entry.get(processDataType).has(SubreactionData)) {
        List<String> enzymes = new ArrayList<String>();
        Map<String, Integer> elementContributions =
          new HashMap<String, Integer>();
        List<String> speciesReferences = new ArrayList<String>();
        List<Integer> stoichiometries = new ArrayList<Integer>();
        // prepare list for enzymes
        // several cases in COBRAme: null, single String, (empty) List
        // first case: is null (nullnode)?
        if (!entry.get(processDataType).get(SubreactionData).get(enzyme)
                  .isNull()) {
          // second case: is array?
          if (entry.get(processDataType).get(SubreactionData).get(enzyme)
                   .isArray()) {
            for (JsonNode node : entry.get(processDataType).get(SubreactionData)
                                      .get(enzyme)) {
              enzymes.add(node.asText());
            }
            // else: single string
          } else {
            enzymes.add(entry.get(processDataType).get(SubreactionData)
                             .get(enzyme).asText());
          }
        }
        // prepare lists for element contributions
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(processDataType).get(SubreactionData)
               .get(element_contribution).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          elementContributions.put(currentEntry.getKey(),
            currentEntry.getValue().asInt());
        }
        // prepare lists for subreaction stoichiometries
        for (Iterator<Entry<String, JsonNode>> stoichiometryEntry =
          entry.get(processDataType).get(SubreactionData).get(stoichiometry)
               .fields(); stoichiometryEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = stoichiometryEntry.next();
          speciesReferences.add(currentEntry.getKey());
          stoichiometries.add(currentEntry.getValue().asInt());
        }
        // add SubreactionData
        meProcessData.addSubreactionData(meProcessData,
          entry.get(id).textValue(),
          entry.get(processDataType).get(SubreactionData).get(keff).asDouble(),
          enzymes, elementContributions, speciesReferences, stoichiometries);
      } else {
        List<String> enzymeReferences = new ArrayList<String>();
        List<Boolean> fixedKeff = new ArrayList<Boolean>();
        List<Boolean> listLengthDependent = new ArrayList<Boolean>();
        List<String> speciesReferences = new ArrayList<String>();
        List<Double> listStoichiometries = new ArrayList<Double>();
        // prepare lists for stoichiometries
        for (Iterator<Entry<String, JsonNode>> stoichiometryEntry =
          entry.get(processDataType).get(TranslocationData).get(stoichiometry)
               .fields(); stoichiometryEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = stoichiometryEntry.next();
          speciesReferences.add(currentEntry.getKey());
          listStoichiometries.add(currentEntry.getValue().asDouble());
        }
        // prepare lists for enzyme dictionary
        for (Iterator<Entry<String, JsonNode>> dictEntry =
          entry.get(processDataType).get(TranslocationData).get(enzyme_dict)
               .fields(); dictEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = dictEntry.next();
          enzymeReferences.add(currentEntry.getKey());
          fixedKeff.add(currentEntry.getValue().get(fixed_keff).asBoolean());
          listLengthDependent.add(
            currentEntry.getValue().get(lengthDep).asBoolean());
        }
        // add TranslocationData
        meProcessData.addTranslocationData(meProcessData,
          entry.get(id).textValue(),
          entry.get(processDataType).get(TranslocationData).get(keff)
               .asDouble(),
          enzymeReferences, fixedKeff, listLengthDependent, speciesReferences,
          listStoichiometries, entry.get(processDataType).get(TranslocationData)
                                    .get(lengthDepEnergy).asBoolean());
      }
    }
    // append process data
    model.appendAnnotation(meProcessData);
  }


  /**
   * Adds all reactions from a list of JsonNodes to the SBML and SBOL models
   * according to their type. Also adds all process data information to the
   * reaction annotations that is not added to the model annotation by the
   * {@link #addMEProcessDataFromJSON(Model, List)} method.
   * 
   * @param model
   *        the SBML model
   * @param sbol
   *        the SBOL document
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the objective of the model
   * @param reactions
   *        the list of reactions from the JSON file
   * @param processData
   *        the list of process data objects that encode additional reaction
   *        specific information
   * @param processDataIndices
   *        the map with indices of the processData list for easier traversing
   *        the processData list
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void addReactionsFromJSON(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, Objective objective, List<JsonNode> reactions,
    List<JsonNode> processData, Map<String, Integer> processDataIndices)
    throws ParseException, SBOLValidationException {
    // create ME Reactions
    MEReactionPlugin meReactionPlugin = new MEReactionPlugin();
    for (JsonNode entry : reactions) {
      if (entry.get(reactionType).has(summaryVariable)) {
        // add SummaryVariable to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
          entry.get(id).textValue(), entry.get(name).textValue(),
          entry.get(upper_bound).asText(), entry.get(lower_Bound).asText(),
          speciesIds, coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue());
      } else if (entry.get(reactionType).has(genericFormation)) {
        // add GenericFormationReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        meReactionPlugin.createGenericFormationReaction(model, groups,
          objective, entry.get(id).textValue(), entry.get(name).textValue(),
          entry.get(upper_bound).asDouble(), entry.get(lower_Bound).asDouble(),
          speciesIds, coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue());
      } else if (entry.get(reactionType).has(metabolicReaction)) {
        // add Metabolic Reaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        meReactionPlugin.createMetabolicReaction(model, groups, objective,
          entry.get(id).textValue(), entry.get(name).textValue(),
          entry.get(reactionType).get(metabolicReaction)
               .get(stoichiometric_Data).textValue(),
          entry.get(upper_bound).asDouble(), entry.get(lower_Bound).asDouble(),
          speciesIds, coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(),
          entry.get(reactionType).get(metabolicReaction).get(keff).asDouble(),
          entry.get(reactionType).get(metabolicReaction).get(reverse)
               .asBoolean(),
          entry.get(reactionType).get(metabolicReaction).get(complex_data)
               .textValue());
      } else if (entry.get(reactionType).has(meReaction)) {
        // add MEReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        meReactionPlugin.createMEReaction(model, groups, objective,
          entry.get(id).textValue(), entry.get(name).textValue(),
          entry.get(upper_bound).asDouble(), entry.get(lower_Bound).asDouble(),
          speciesIds, coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue());
      } else if (entry.get(reactionType).has(transcription)) {
        // add TranscriptionReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        String dataId = entry.get(reactionType).get(transcription)
                             .get(transcription_data).asText();
        // get ProcessData object
        JsonNode processDataEntry =
          processData.get(processDataIndices.get(dataId)).get(processDataType)
                     .get(transcriptionData);
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(subreactions).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        meReactionPlugin.createTranscriptionReaction(model, sbol, groups,
          objective, entry.get(id).textValue(), entry.get(name).textValue(),
          dataId, entry.get(upper_bound).asDouble(),
          entry.get(lower_Bound).asDouble(), speciesIds, coefficients,
          entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(),
          processDataEntry.get(nucleotide_sequence).asText(), subreactionMap);
      } else if (entry.get(reactionType).has(translation)) {
        // add TranslationReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        String dataId = entry.get(reactionType).get(translation)
                             .get(translation_data).asText();
        // get ProcessData object
        JsonNode processDataEntry =
          processData.get(processDataIndices.get(dataId)).get(processDataType)
                     .get(translationData);
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(subreactions).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        meReactionPlugin.createTranslationReaction(model, sbol, groups,
          objective, entry.get(id).textValue(), entry.get(name).textValue(),
          dataId, entry.get(upper_bound).asDouble(),
          entry.get(lower_Bound).asDouble(), speciesIds, coefficients,
          entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(),
          processDataEntry.get(nucleotide_sequence).asText(), subreactionMap);
      } else if (entry.get(reactionType).has(tRNACharging)) {
        // add tRNAChargingReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        String dataId =
          entry.get(reactionType).get(tRNACharging).get(tRNA_data).asText();
        // get ProcessData object
        JsonNode processDataEntry =
          processData.get(processDataIndices.get(dataId)).get(processDataType)
                     .get(tRNAData);
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(subreactions).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        meReactionPlugin.createtRNAChargingReaction(model, groups, objective,
          entry.get(id).textValue(), entry.get(name).textValue(), dataId,
          entry.get(upper_bound).asDouble(), entry.get(lower_Bound).asDouble(),
          processDataEntry.get(synthetaseKeff).asDouble(), speciesIds,
          coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(), subreactionMap,
          processDataEntry.get(synthetase).textValue(),
          processDataEntry.get(codon).textValue(),
          processDataEntry.get(amino_acid).textValue());
      } else if (entry.get(reactionType).has(complexFormation)) {
        // add ComplexFormation Reaction
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        String dataId = entry.get(reactionType).get(complexFormation)
                             .get(complex_data_id).asText();
        String complexId = entry.get(reactionType).get(complexFormation)
                                .get(complex_id_).asText();
        // get ProcessData object
        JsonNode processDataEntry =
          processData.get(processDataIndices.get(dataId)).get(processDataType)
                     .get(complexData);
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(subreactions).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        meReactionPlugin.createComplexFormationReaction(model, groups,
          objective, entry.get(id).textValue(), entry.get(name).textValue(),
          dataId, complexId, entry.get(upper_bound).asDouble(),
          entry.get(lower_Bound).asDouble(), speciesIds, coefficients,
          entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(), subreactionMap);
      } else if (entry.get(reactionType).has(postTranslationReaction)) {
        // add PostTranslationReaction to model
        List<String> speciesIds = new ArrayList<String>();
        List<String> coefficients = new ArrayList<String>();
        Map<String, Double> subreactionMap = new HashMap<String, Double>();
        List<String> translocationList = new ArrayList<String>();
        List<Double> multipliers = new ArrayList<Double>();
        List<String> keqFolding = new ArrayList<String>();
        List<Double> keqValues = new ArrayList<Double>();
        List<String> kFolding = new ArrayList<String>();
        List<Double> kValues = new ArrayList<Double>();
        List<String> surfaceArea = new ArrayList<String>();
        List<Double> surfaceAreaValue = new ArrayList<Double>();
        String dataId = entry.get(reactionType).get(postTranslationReaction)
                             .get(posttranslation_data).asText();
        // get ProcessData object
        JsonNode processDataEntry =
          processData.get(processDataIndices.get(dataId)).get(processDataType)
                     .get(posttranslationData);
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          entry.get(metabolitesField).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          speciesIds.add(currentEntry.getKey());
          coefficients.add(currentEntry.getValue().asText());
        }
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(subreactions).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          subreactionMap.put(currentEntry.getKey(),
            currentEntry.getValue().asDouble());
        }
        // combine list of translocation multipliers with list of translocation
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(translocation_multiplier).fields(); elementEntry
                                                                               .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          translocationList.add(currentEntry.getKey());
          multipliers.add(currentEntry.getValue().asDouble());
        }
        // set multiplier to 0.0 for entries of translocation list
        for (JsonNode node : processDataEntry.get(translocation)) {
          translocationList.add(node.asText());
          multipliers.add(0.0);
        }
        // set lists for keqFolding
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(keq_folding).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          keqFolding.add(currentEntry.getKey());
          keqValues.add(currentEntry.getValue().asDouble());
        }
        // set lists for kFolding
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(k_folding).fields(); elementEntry.hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          kFolding.add(currentEntry.getKey());
          kValues.add(currentEntry.getValue().asDouble());
        }
        // set lists for surface area
        for (Iterator<Entry<String, JsonNode>> elementEntry =
          processDataEntry.get(surface_area).fields(); elementEntry
                                                                   .hasNext();) {
          Entry<String, JsonNode> currentEntry = elementEntry.next();
          surfaceArea.add(currentEntry.getKey());
          surfaceAreaValue.add(currentEntry.getValue().asDouble());
        }
        meReactionPlugin.createPostTranslationReaction(model, groups, objective,
          entry.get(id).textValue(), entry.get(name).textValue(), dataId,
          entry.get(upper_bound).asDouble(), entry.get(lower_Bound).asDouble(),
          speciesIds, coefficients, entry.get(objectiveCoefficient).asDouble(),
          entry.get(variable_kind).textValue(), subreactionMap,
          processDataEntry.get(aggregation_propensity).asDouble(),
          translocationList, multipliers,
          processDataEntry.get(propensity_scaling).asDouble(), surfaceArea,
          surfaceAreaValue, keqFolding, keqValues, kFolding, kValues,
          processDataEntry.get(biomass_type).textValue());
      }
    }
  }
}
