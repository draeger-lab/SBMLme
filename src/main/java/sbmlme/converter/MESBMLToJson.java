package sbmlme.converter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.FBCReactionPlugin;
import org.sbml.jsbml.ext.fbc.FBCSpeciesPlugin;
import org.sbml.jsbml.ext.groups.Group;
import org.sbml.jsbml.ext.groups.GroupsConstants;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.ext.groups.Member;
import org.sbml.jsbml.xml.XMLNode;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.Range;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SystemsBiologyOntology;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import sbmlme.MEConstants;

/**
 * contains methods for converting a SBMLme model into a COBRAme JSON file
 * 
 * @author Marc A. Voigt
 */
@SuppressWarnings("restriction")
public class MESBMLToJson implements MEJsonConstants, MEConstants {

  /**
   * read in a SBML file and a SBOL file and convert it to a JSON file
   * 
   * @param sbmlFile
   * @param sbolFile
   * @param output
   * @param tidy
   * @throws XMLStreamException
   * @throws IOException
   * @throws SBOLValidationException
   * @throws SBOLConversionException
   */
  public MESBMLToJson(String sbmlFile, String sbolFile, String output,
    boolean tidy) throws XMLStreamException, IOException,
    SBOLValidationException, SBOLConversionException {
    // read the SBML and SBOL documents
    System.out.println("Read in SBML and SBOL files");
    SBMLReader sbmlReader = new SBMLReader();
    SBMLDocument sbmlDoc = sbmlReader.readSBML(sbmlFile);
    Model model = sbmlDoc.getModel();
    SBOLDocument sbolDoc = SBOLReader.read(sbolFile);
    // start building JSON
    MEJsonCOBRAme jsonCOBRAme = new MEJsonCOBRAme();
    List<MEJsonReaction> reactions = new ArrayList<MEJsonReaction>();
    List<MEJsonProcessData> processData = new ArrayList<MEJsonProcessData>();
    List<MEJsonMetabolite> meMetabolites = new ArrayList<MEJsonMetabolite>();
    // iterate over groups to add all reactions and species to the JSON
    GroupsModelPlugin groups =
      (GroupsModelPlugin) model.getPlugin(GroupsConstants.shortLabel);
    // get list of IdRef from groups for easier parsing
    // need to first create a link to a Group since .getGroup(String) returns a
    // SBase instead of a Group
    Group transcribed = (Group) groups.getGroup(transcribedGene);
    List<String> listTranscribed = new ArrayList<String>();
    for (Member member : transcribed.getListOfMembers()) {
      listTranscribed.add(member.getIdRef());
    }
    Group rnap = (Group) groups.getGroup(rnaPolymerase);
    List<String> listRNAP = new ArrayList<String>();
    for (Member member : rnap.getListOfMembers()) {
      listRNAP.add(member.getIdRef());
    }
    Group translated = (Group) groups.getGroup(translatedGene);
    List<String> listTranslated = new ArrayList<String>();
    for (Member member : translated.getListOfMembers()) {
      listTranslated.add(member.getIdRef());
    }
    Group generictRNA = (Group) groups.getGroup(gentRNA);
    List<String> listtRNA = new ArrayList<String>();
    for (Member member : generictRNA.getListOfMembers()) {
      listtRNA.add(member.getIdRef());
    }
    Group processed = (Group) groups.getGroup(processedProtein);
    List<String> listProcessed = new ArrayList<String>();
    for (Member member : processed.getListOfMembers()) {
      listProcessed.add(member.getIdRef());
    }
    Group complexGroup = (Group) groups.getGroup(complex);
    List<String> listComplex = new ArrayList<String>();
    for (Member member : complexGroup.getListOfMembers()) {
      listComplex.add(member.getIdRef());
    }
    // set Map for global info
    Map<String, Double> globalInfo = new HashMap<String, Double>();
    FBCModelPlugin fbcModel =
      (FBCModelPlugin) model.getPlugin(FBCConstants.shortLabel);
    for (Group group : groups.getListOfGroups()) {
      String groupId = group.getId();
      System.out.println("Add " + groupId + " to JSON");
      // reactions
      if (groupId.equals(summaryVariable) || groupId.equals(meReaction)
        || groupId.equals(genericFormation) || groupId.equals(transcription)
        || groupId.equals(translation) || groupId.equals(tRNACharging)
        || groupId.equals(postTranslationReaction)
        || groupId.equals(complexFormationReaction)
        || groupId.equals(metabolicReaction)) {
        for (Member member : group.getListOfMembers()) {
          reactions.add(addReaction(member, model, fbcModel, processData,
            groupId, sbolDoc, listTranscribed, listRNAP, listTranslated,
            listtRNA, listProcessed, listComplex));
        }
      } else if (groupId.equals(globalInformation)) {
        // add globalInfo
        for (Member member : group.getListOfMembers()) {
          String memberId = member.getIdRef();
          globalInfo.put(ConvertSBMLIdToCOBRAId(memberId),
            model.getParameter(memberId).getValue());
        }
      } else if (groupId.startsWith(genericData)) {
        // add Generic Data to ProcessData list
        String genericDataId = groupId.replace(genericData, "");
        List<String> component_list = new ArrayList<String>();
        MEJsonProcessData genericData = new MEJsonProcessData();
        MEJsonProcessDataType genericDataType = new MEJsonProcessDataType();
        MEJsonProcessDataTypeAttributes genericDataTypeAttributes =
          new MEJsonProcessDataTypeAttributes();
        genericData.setId(genericDataId);
        // add all component to a list
        for (Member member : group.getListOfMembers()) {
          component_list.add(member.getIdRef());
        }
        // set ProcessData structure
        genericDataTypeAttributes.setComponent_list(component_list);
        genericDataType.setGenericData(genericDataTypeAttributes);
        genericData.setProcessDataType(genericDataType);
        processData.add(genericData);
      } else if (groupId.equals(constraint) || groupId.equals(complex)
        || groupId.equals(metabolite) || groupId.equals(translatedGene)
        || groupId.equals(gentRNA) || groupId.equals(transcribedGene)
        || groupId.equals(genericComponent) || groupId.equals(processedProtein)
        || groupId.equals(ribosome) || groupId.equals(rnaPolymerase)) {
        // add species to metabolites
        for (Member member : group.getListOfMembers()) {
          meMetabolites.add(addMetabolites(member, model, sbolDoc, groupId));
        }
      }
    }
    // read SubreactionData, TranslocationData and StoichiometricData from SBML
    // model annotation
    XMLNode modelAnnotation = model.getAnnotation().getFullAnnotation();
    List<XMLNode> listSubreactionData =
      modelAnnotation.getChildElement(processDataPlugin, "*")
                     .getChildElement(listOfSubreactionData, "*")
                     .getChildElements(subreactionData, "*");
    List<XMLNode> listTranslocationData =
      modelAnnotation.getChildElement(processDataPlugin, "*")
                     .getChildElement(listOfTranslocationData, "*")
                     .getChildElements(subreactionData, "*");
    List<XMLNode> listStoichiometricData =
      modelAnnotation.getChildElement(processDataPlugin, "*")
                     .getChildElement(listOfStoichiometricData, "*")
                     .getChildElements(stoichiometricData, "*");
    // add SubreactionData
    System.out.println("Add SubreactionData to JSON");
    for (XMLNode subreactionData : listSubreactionData) {
      processData.add(addSubreactionData(subreactionData));
    }
    // add TranslocationData
    System.out.println("Add TranslocationData to JSON");
    for (XMLNode translocationData : listTranslocationData) {
      processData.add(addTranslocationData(translocationData));
    }
    // add StoichiometricData
    System.out.println("Add StoichiometricData to JSON");
    for (XMLNode stoichiometricData : listStoichiometricData) {
      processData.add(addStoichiometricData(stoichiometricData));
    }
    // finish JSON
    jsonCOBRAme.setReactions(reactions);
    jsonCOBRAme.setProcessData(processData);
    jsonCOBRAme.setMetabolites(meMetabolites);
    jsonCOBRAme.setGlobalInfo(globalInfo);
    // print JSON
    System.out.println("Write JSON to file");
    ObjectMapper objectMapper = new ObjectMapper();
    // readable output
    if (tidy) {
      objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    StringWriter stringJSON = new StringWriter();
    objectMapper.writeValue(stringJSON, jsonCOBRAme);
    objectMapper.writeValue(new File(output + ".json"), jsonCOBRAme);
  }


  /**
   * Since COBRA ids are semantically overloaded and may contain characters
   * forbidden in SBML they had to be converted to create SBML conform ids. This
   * method is intended to be used to create COBRA ids from these SBML conform
   * ids.
   * 
   * @param id
   * @return
   */
  public String ConvertSBMLIdToCOBRAId(String id) {
    id = id.replaceAll("__meCOLONme__", ":");
    id = id.replaceAll("__meMINUSme__", "-");
    id = id.replaceAll("__meSLASHme__", "/");
    id = id.replaceAll("__meSTARTme__", "");
    return id;
  }


  /**
   * creates a metabolite for the list of metabolites of the JSON output
   * 
   * @param member
   * @param model
   * @param sbolDoc
   * @param groupId
   * @return
   */
  public MEJsonMetabolite addMetabolites(Member member, Model model,
    SBOLDocument sbolDoc, String groupId) {
    MEJsonMetabolite species = new MEJsonMetabolite();
    MEJsonMetaboliteType speciesType = new MEJsonMetaboliteType();
    MEJsonMetaboliteTypeAttributes speciesTypeAttributes =
      new MEJsonMetaboliteTypeAttributes();
    Species sbmlSpecies = model.getSpecies(member.getIdRef());
    species.setName(sbmlSpecies.getName());
    species.setCompartment(sbmlSpecies.getCompartment());
    species.setId(ConvertSBMLIdToCOBRAId(member.getIdRef()));
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) sbmlSpecies.getPlugin(FBCConstants.shortLabel);
    species.setFormula(fbcSpecies.getChemicalFormula());
    if (groupId.equals(constraint)) {
      speciesType.setConstraint(speciesTypeAttributes);
    } else if (groupId.equals(complex)) {
      speciesType.setComplex(speciesTypeAttributes);
    } else if (groupId.equals(metabolite)) {
      speciesType.setMetabolite(speciesTypeAttributes);
    } else if (groupId.equals(translatedGene)) {
      speciesType.setTranslatedGene(speciesTypeAttributes);
    } else if (groupId.equals(gentRNA)) {
      speciesType.setGenerictRNA(speciesTypeAttributes);
    } else if (groupId.equals(genericComponent)) {
      speciesType.setGenericComponent(speciesTypeAttributes);
    } else if (groupId.equals(ribosome)) {
      speciesType.setRibosome(speciesTypeAttributes);
    } else if (groupId.equals(rnaPolymerase)) {
      speciesType.setRNAP(speciesTypeAttributes);
    } else if (groupId.equals(processedProtein)) {
      speciesTypeAttributes.setUnprocessedProteinId(
        ConvertSBMLIdToCOBRAId(sbmlSpecies.getAnnotation().getFullAnnotation()
                                          .getChildElement(speciesPlugin, "*")
                                          .getAttrValue(unprocessed)));
      speciesType.setProcessedProtein(speciesTypeAttributes);
    } else if (groupId.equals(transcribedGene)) {
      String sbolId =
        sbmlSpecies.getAnnotation().getFullAnnotation()
                   .getChildElement(speciesPlugin, "*").getAttrValue(sequence);
      Integer leftPos =
        Integer.valueOf(sbmlSpecies.getAnnotation().getFullAnnotation()
                                   .getChildElement(speciesPlugin, "*")
                                   .getAttrValue(genomePos));
      // the set of sequences should only contain one element therefore
      // the next element can simply be added as the nucleotide sequence
      for (Iterator<Sequence> sequenceIterator =
        sbolDoc.getComponentDefinition(java.net.URI.create(sbolId))
               .getSequences().iterator(); sequenceIterator.hasNext();) {
        speciesTypeAttributes.setNucleotideSequence(
          // elements may not be visible
          sequenceIterator.next().getElements());
      }
      Range range =
        (Range) sbolDoc.getComponentDefinition(java.net.URI.create(sbolId))
                       .getSequenceAnnotation(
                         member.getIdRef() + sbolAnnotation)
                       .getLocation(member.getIdRef() + sbolLoc);
      // set Positions on genome
      speciesTypeAttributes.setLeftPos(leftPos);
      speciesTypeAttributes.setRightPos(leftPos + range.getEnd());
      URI rnaURI =
        (URI) sbolDoc.getComponentDefinition(java.net.URI.create(sbolId))
                     .getRoles().toArray()[0];
      // set RNA type
      SystemsBiologyOntology sbo = new SystemsBiologyOntology();
      if (rnaURI.equals(sbo.getURIbyName(messengerRNA))) {
        speciesTypeAttributes.setRNAType(mRNA);
      } else if (rnaURI.equals(sbo.getURIbyName(transferRNA))) {
        speciesTypeAttributes.setRNAType(tRNA);
      } else if (rnaURI.equals(sbo.getURIbyName(noncodingRNA))) {
        speciesTypeAttributes.setRNAType(ncRNA);
      } else if (rnaURI.equals(sbo.getURIbyName(ribosomalRNA))) {
        speciesTypeAttributes.setRNAType(rRNA);
      }
      // set Strand
      if (range.getOrientation().equals(OrientationType.INLINE)) {
        speciesTypeAttributes.setStrand("+");
      } else {
        speciesTypeAttributes.setStrand("-");
      }
      speciesType.setTranscribedGene(speciesTypeAttributes);
    }
    species.setMetaboliteType(speciesType);
    return species;
  }


  /**
   * create SubreactionData entry for the list of process data
   * 
   * @param subreactionData
   * @return
   */
  public MEJsonProcessData addSubreactionData(XMLNode subreactionData) {
    MEJsonProcessData processDataEntry = new MEJsonProcessData();
    MEJsonProcessDataType processDataEntryType = new MEJsonProcessDataType();
    MEJsonProcessDataTypeAttributes processDataEntryTypeAttributes =
      new MEJsonProcessDataTypeAttributes();
    processDataEntry.setId(
      ConvertSBMLIdToCOBRAId(subreactionData.getAttrValue(id)));
    processDataEntryTypeAttributes.setKeff(
      Double.valueOf(subreactionData.getAttrValue(keff)));
    // add list of enzymes
    List<XMLNode> listEnzymeNodes =
      subreactionData.getChildElement(listOfEnzymeInformation, "*")
                     .getChildElements(enzymeInformation, "*");
    List<String> listEnzymes = new ArrayList<String>();
    for (XMLNode enzyme : listEnzymeNodes) {
      listEnzymes.add(ConvertSBMLIdToCOBRAId(enzyme.getAttrValue(enzymeRef)));
    }
    processDataEntryTypeAttributes.setEnzyme(listEnzymes);
    // add Map of Element Contributions
    List<XMLNode> listElementNodes =
      subreactionData.getChildElement(listOfElementContributions, "*")
                     .getChildElements(elementContribution, "*");
    Map<String, Integer> elementContributions = new HashMap<String, Integer>();
    for (XMLNode elementNode : listElementNodes) {
      elementContributions.put(elementNode.getAttrValue(element),
        Integer.valueOf(elementNode.getAttrValue(value)));
    }
    processDataEntryTypeAttributes.setElement_contribution(
      elementContributions);
    // add Stoichiometry
    List<XMLNode> listReactantNodes =
      subreactionData.getChildElement(listOfReactants, "*")
                     .getChildElements(speciesRef, "*");
    List<XMLNode> listProductNodes =
      subreactionData.getChildElement(listOfProducts, "*")
                     .getChildElements(speciesRef, "*");
    Map<String, Double> stoichiometries = new HashMap<String, Double>();
    for (XMLNode entry : listReactantNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)) * -1);
    }
    for (XMLNode entry : listProductNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)));
    }
    processDataEntryTypeAttributes.setStoichiometry(stoichiometries);
    processDataEntryType.setSubreactionData(processDataEntryTypeAttributes);
    processDataEntry.setProcessDataType(processDataEntryType);
    return processDataEntry;
  }


  /**
   * create TranslocationData entry for the list of process data
   * 
   * @param translocationData
   * @return
   */
  public MEJsonProcessData addTranslocationData(XMLNode translocationData) {
    MEJsonProcessData processDataEntry = new MEJsonProcessData();
    MEJsonProcessDataType processDataEntryType = new MEJsonProcessDataType();
    MEJsonProcessDataTypeAttributes processDataEntryTypeAttributes =
      new MEJsonProcessDataTypeAttributes();
    processDataEntry.setId(
      ConvertSBMLIdToCOBRAId(translocationData.getAttrValue(id)));
    processDataEntryTypeAttributes.setKeff(
      Double.valueOf(translocationData.getAttrValue(keff)));
    processDataEntryTypeAttributes.setLength_dependent_energy(
      Boolean.valueOf(translocationData.getAttrValue(lengthDependent)));
    // add enzyme_dict
    Map<String, Map<String, Boolean>> enzymeDict =
      new HashMap<String, Map<String, Boolean>>();
    List<XMLNode> enzymeDictNodes =
      translocationData.getChildElement(listOfEnzymeInformation, "*")
                       .getChildElements(enzymeInformation, "*");
    for (XMLNode node : enzymeDictNodes) {
      Map<String, Boolean> dict = new HashMap<String, Boolean>();
      dict.put(fixed_keff, Boolean.valueOf(node.getAttrValue(fixedkeff)));
      dict.put(lengthDep, Boolean.valueOf(node.getAttrValue(lengthDependent)));
      enzymeDict.put(ConvertSBMLIdToCOBRAId(node.getAttrValue(enzymeRef)),
        dict);
    }
    processDataEntryTypeAttributes.setEnzyme_dict(enzymeDict);
    // add Stoichiometry
    List<XMLNode> listReactantNodes =
      translocationData.getChildElement(listOfReactants, "*")
                       .getChildElements(speciesRef, "*");
    List<XMLNode> listProductNodes =
      translocationData.getChildElement(listOfProducts, "*")
                       .getChildElements(speciesRef, "*");
    Map<String, Double> stoichiometries = new HashMap<String, Double>();
    for (XMLNode entry : listReactantNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)) * -1);
    }
    for (XMLNode entry : listProductNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)));
    }
    processDataEntryTypeAttributes.setStoichiometry(stoichiometries);
    processDataEntryType.setTranslocationData(processDataEntryTypeAttributes);
    processDataEntry.setProcessDataType(processDataEntryType);
    return processDataEntry;
  }


  /**
   * create StoichiometricData entry for the list of process data
   * 
   * @param stoichiometricData
   * @return
   */
  public MEJsonProcessData addStoichiometricData(XMLNode stoichiometricData) {
    MEJsonProcessData processDataEntry = new MEJsonProcessData();
    MEJsonProcessDataType processDataEntryType = new MEJsonProcessDataType();
    MEJsonProcessDataTypeAttributes processDataEntryTypeAttributes =
      new MEJsonProcessDataTypeAttributes();
    processDataEntry.setId(
      ConvertSBMLIdToCOBRAId(stoichiometricData.getAttrValue(id)));
    // set bounds
    processDataEntryTypeAttributes.setLower_bound(
      Double.valueOf(stoichiometricData.getAttrValue(lowerFluxBound)));
    processDataEntryTypeAttributes.setUpper_bound(
      Double.valueOf(stoichiometricData.getAttrValue(upperFluxBound)));
    // add subreations
    List<XMLNode> listSubreactions =
      stoichiometricData.getChildElement(listOfSubreactions, "*")
                        .getChildElements(subreaction, "*");
    Map<String, Double> subreactions = new HashMap<String, Double>();
    for (XMLNode entry : listSubreactions) {
      subreactions.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(subreaction)),
        Double.valueOf(entry.getAttrValue(coefficient)));
    }
    processDataEntryTypeAttributes.setSubreactions(subreactions);
    // add stoichiometry
    List<XMLNode> listReactantNodes =
      stoichiometricData.getChildElement(listOfReactants, "*")
                        .getChildElements(speciesRef, "*");
    List<XMLNode> listProductNodes =
      stoichiometricData.getChildElement(listOfProducts, "*")
                        .getChildElements(speciesRef, "*");
    Map<String, Double> stoichiometries = new HashMap<String, Double>();
    for (XMLNode entry : listReactantNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)) * -1);
    }
    for (XMLNode entry : listProductNodes) {
      stoichiometries.put(ConvertSBMLIdToCOBRAId(entry.getAttrValue(species)),
        Double.valueOf(entry.getAttrValue(stoichiometry)));
    }
    processDataEntryTypeAttributes.set_stoichiometry(stoichiometries);
    processDataEntryType.setStoichiometricData(processDataEntryTypeAttributes);
    processDataEntry.setProcessDataType(processDataEntryType);
    return processDataEntry;
  }


  /**
   * create reaction entry for list of reactions
   * 
   * @param member
   * @param model
   * @param fbcModel
   * @param processData
   * @param groupId
   * @param sbolDoc
   * @param listTranscribed
   * @param listRNAP
   * @param listTranslated
   * @param listtRNA
   * @param listProcessed
   * @param listComplex
   * @return
   */
  public MEJsonReaction addReaction(Member member, Model model,
    FBCModelPlugin fbcModel, List<MEJsonProcessData> processData,
    String groupId, SBOLDocument sbolDoc, List<String> listTranscribed,
    List<String> listRNAP, List<String> listTranslated, List<String> listtRNA,
    List<String> listProcessed, List<String> listComplex) {
    String memberId = member.getIdRef();
    Reaction reaction = model.getReaction(memberId);
    FBCReactionPlugin fbcTemp =
      (FBCReactionPlugin) reaction.getPlugin(FBCConstants.shortLabel);
    MEJsonReaction jsonReaction = new MEJsonReaction();
    MEJsonReactionType reactionType = new MEJsonReactionType();
    // set common attributes
    jsonReaction.setId(ConvertSBMLIdToCOBRAId(memberId));
    Parameter lower = fbcTemp.getLowerFluxBoundInstance();
    if (lower.isSetValue()) {
      jsonReaction.setLowerBound(String.valueOf(lower.getValue()));
    } else {
      jsonReaction.setLowerBound(
        model.getInitialAssignmentBySymbol(lower.getId()).getMath().toFormula()
             .replace("^", "**"));
    }
    Parameter upper = fbcTemp.getUpperFluxBoundInstance();
    if (upper.isSetValue()) {
      jsonReaction.setUpperBound(String.valueOf(upper.getValue()));
    } else {
      jsonReaction.setUpperBound(
        model.getInitialAssignmentBySymbol(upper.getId()).getMath().toFormula()
             .replace("^", "**"));
    }
    jsonReaction.setName(reaction.getName());
    // active objective -> List<FluxObjectives> ->
    // FluxObjective.coefficient
    jsonReaction.setObjectiveCoefficient(
      fbcModel.getActiveObjectiveInstance().getListOfFluxObjectives()
              .get(memberId + coefficientEnd).getCoefficient());
    // full annotation -> child "meReactionPlugin" -> value of attribute
    // variableKind
    jsonReaction.setVariableKind(
      reaction.getAnnotation().getFullAnnotation()
              .getChildElement(reactionPlugin, "*").getAttrValue(variableKind));
    // add species to ObjectNode for metabolite
    ObjectNode node = JsonNodeFactory.instance.objectNode();
    for (SpeciesReference species : reaction.getListOfProducts()) {
      if (species.isSetValue()) {
        node.put(ConvertSBMLIdToCOBRAId(species.getSpecies()),
          species.getValue());
      } else {
        ASTNode tempProductMath =
          model.getInitialAssignmentBySymbol(species.getId()).getMath();
        String math = tempProductMath.toString().replace("^", "**");
        node.put(ConvertSBMLIdToCOBRAId(species.getSpecies()), math);
      }
    }
    // since COBRAme only has only one list for all species in a reaction,
    // the reactants are identified by negative values which are not
    // allowed in SBML. As a solution the value of the species has been
    // multiplied with -1 which needs to get undone when converting back
    // to JSON
    for (SpeciesReference species : reaction.getListOfReactants()) {
      if (species.isSetValue()) {
        node.put(ConvertSBMLIdToCOBRAId(species.getSpecies()),
          species.getValue() * -1);
      } else {
        ASTNode tempReactantMath =
          model.getInitialAssignmentBySymbol(species.getId()).getMath();
        String math = tempReactantMath.toString().replace("^", "**");
        // the multiplication with -1 should appear at the end of the
        // formula string
        if (math.endsWith("*-1")) {
          math = math.substring(0, math.length() - 3);
          // just for safety purpose if the species reference has not been
          // added correctly to the SBML model
        } else if (math.startsWith("-1*")) {
          math = math.substring(3, math.length());
        }
        node.put(ConvertSBMLIdToCOBRAId(species.getSpecies()), math);
      }
    }
    jsonReaction.setMetabolites(node);
    // set type of reaction
    MEJsonReactionTypeAttributes typeAttributes =
      new MEJsonReactionTypeAttributes();
    if (groupId.equals(summaryVariable)) {
      reactionType.setSummaryVariable(typeAttributes);
    } else if (groupId.equals(meReaction)) {
      reactionType.setMEReaction(typeAttributes);
    } else if (groupId.equals(genericFormation)) {
      reactionType.setGenericFormationReaction(typeAttributes);
    } else if (groupId.equals(transcription)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      typeAttributes.setTranscriptionDataId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      reactionType.setTranscriptionReaction(typeAttributes);
      MEJsonProcessData tempProcess = new MEJsonProcessData();
      MEJsonProcessDataType tempProcessType = new MEJsonProcessDataType();
      MEJsonProcessDataTypeAttributes tempProcessTypeAttributes =
        new MEJsonProcessDataTypeAttributes();
      tempProcess.setId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      String sbolId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                    .getAttrValue(sequence);
      // the set of sequences should only contain one element therefore
      // the next element can simply be added as the nucleotide sequence
      for (Iterator<Sequence> sequenceIterator =
        sbolDoc.getComponentDefinition(java.net.URI.create(sbolId))
               .getSequences().iterator(); sequenceIterator.hasNext();) {
        tempProcessTypeAttributes.setNucleotideSequence(
          // elements may not be visible
          sequenceIterator.next().getElements());
      }
      // add subreations to ProcessData
      Map<String, Double> subreactions = new HashMap<String, Double>();
      List<XMLNode> subreactionReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listSubreactionReferences, "*")
                      .getChildElements(subreactionRef, "*");
      // iterate over List of Subreaction References
      for (Iterator<XMLNode> subreactionIter =
        subreactionReferences.iterator(); subreactionIter.hasNext();) {
        XMLNode subreactionRef = subreactionIter.next();
        subreactions.put(
          ConvertSBMLIdToCOBRAId(subreactionRef.getAttrValue(subreaction)),
          Double.valueOf(subreactionRef.getAttrValue(stoichiometry)));
      }
      tempProcessTypeAttributes.setSubreactions(subreactions);
      // add list of RNA products
      List<String> listRNAProducts = new ArrayList<String>();
      for (SpeciesReference species : reaction.getListOfProducts()) {
        // check if current product is a RNA
        if (listTranscribed.contains(species.getSpecies())) {
          listRNAProducts.add(ConvertSBMLIdToCOBRAId(species.getSpecies()));
        }
      }
      tempProcessTypeAttributes.setRNAProducts(listRNAProducts);
      // add the single RNA polymerase to ProcessData attributes
      for (SpeciesReference species : reaction.getListOfReactants()) {
        // check if current species is the polymerase
        if (listRNAP.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setRNA_polymerase(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      tempProcessType.setTranscriptionData(tempProcessTypeAttributes);
      tempProcess.setProcessDataType(tempProcessType);
      processData.add(tempProcess);
    } else if (groupId.equals(translation)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      typeAttributes.setTranslationDataId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      reactionType.setTranslationReaction(typeAttributes);
      MEJsonProcessData tempProcess = new MEJsonProcessData();
      MEJsonProcessDataType tempProcessType = new MEJsonProcessDataType();
      MEJsonProcessDataTypeAttributes tempProcessTypeAttributes =
        new MEJsonProcessDataTypeAttributes();
      tempProcess.setId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      String sbolId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                    .getAttrValue(sequence);
      // the set of sequences should only contain one element therefore
      // the next element can simply be added as the nucleotide sequence
      for (Iterator<Sequence> sequenceIterator =
        sbolDoc.getComponentDefinition(java.net.URI.create(sbolId))
               .getSequences().iterator(); sequenceIterator.hasNext();) {
        tempProcessTypeAttributes.setNucleotideSequence(
          // elements may not be visible
          sequenceIterator.next().getElements());
      }
      // add subreations to ProcessData
      Map<String, Double> subreactions = new HashMap<String, Double>();
      List<XMLNode> subreactionReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listSubreactionReferences, "*")
                      .getChildElements(subreactionRef, "*");
      // iterate over List of Subreaction References
      for (Iterator<XMLNode> subreactionIter =
        subreactionReferences.iterator(); subreactionIter.hasNext();) {
        XMLNode subreactionRef = subreactionIter.next();
        subreactions.put(
          ConvertSBMLIdToCOBRAId(subreactionRef.getAttrValue(subreaction)),
          Double.valueOf(subreactionRef.getAttrValue(stoichiometry)));
      }
      tempProcessTypeAttributes.setSubreactions(subreactions);
      // add mRNA
      for (SpeciesReference species : reaction.getListOfReactants()) {
        // check if current product is a RNA
        if (listTranscribed.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setmRNA(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      // add protein
      for (SpeciesReference species : reaction.getListOfProducts()) {
        // check if current product is a RNA
        if (listTranslated.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setProtein(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      tempProcessType.setTranslationData(tempProcessTypeAttributes);
      tempProcess.setProcessDataType(tempProcessType);
      processData.add(tempProcess);
    } else if (groupId.equals(tRNACharging)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      typeAttributes.settRNADataId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      reactionType.settRNAChargingReaction(typeAttributes);
      MEJsonProcessData tempProcess = new MEJsonProcessData();
      MEJsonProcessDataType tempProcessType = new MEJsonProcessDataType();
      MEJsonProcessDataTypeAttributes tempProcessTypeAttributes =
        new MEJsonProcessDataTypeAttributes();
      tempProcess.setId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      // add subreations to ProcessData
      Map<String, Double> subreactions = new HashMap<String, Double>();
      List<XMLNode> subreactionReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listSubreactionReferences, "*")
                      .getChildElements(subreactionRef, "*");
      // iterate over List of Subreaction References
      for (Iterator<XMLNode> subreactionIter =
        subreactionReferences.iterator(); subreactionIter.hasNext();) {
        XMLNode subreactionRef = subreactionIter.next();
        subreactions.put(
          ConvertSBMLIdToCOBRAId(subreactionRef.getAttrValue(subreaction)),
          Double.valueOf(subreactionRef.getAttrValue(stoichiometry)));
      }
      // add RNA
      for (SpeciesReference species : reaction.getListOfReactants()) {
        // check if current product is a RNA
        if (listTranscribed.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setRNA(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      tempProcessTypeAttributes.setSubreactions(subreactions);
      tempProcessTypeAttributes.setSynthetase_keff(
        Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                     .getAttrValue(keff)));
      tempProcessTypeAttributes.setSynthetase(ConvertSBMLIdToCOBRAId(
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getAttrValue(synthetase)));
      tempProcessTypeAttributes.setCodon(
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getAttrValue(codon));
      tempProcessTypeAttributes.setAmino_acid(
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getAttrValue(aminoAcid));
      tempProcessType.settRNAData(tempProcessTypeAttributes);
      tempProcess.setProcessDataType(tempProcessType);
      processData.add(tempProcess);
    } else if (groupId.equals(postTranslationReaction)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      typeAttributes.setPostTranslationDataId(
        ConvertSBMLIdToCOBRAId(sbmlDataId));
      reactionType.setPostTranslationReaction(typeAttributes);
      MEJsonProcessData tempProcess = new MEJsonProcessData();
      MEJsonProcessDataType tempProcessType = new MEJsonProcessDataType();
      MEJsonProcessDataTypeAttributes tempProcessTypeAttributes =
        new MEJsonProcessDataTypeAttributes();
      tempProcess.setId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      // add subreations to ProcessData
      Map<String, Double> subreactions = new HashMap<String, Double>();
      List<XMLNode> subreactionReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listSubreactionReferences, "*")
                      .getChildElements(subreactionRef, "*");
      // iterate over List of Subreaction References
      for (Iterator<XMLNode> subreactionIter =
        subreactionReferences.iterator(); subreactionIter.hasNext();) {
        XMLNode subreactionRef = subreactionIter.next();
        subreactions.put(
          ConvertSBMLIdToCOBRAId(subreactionRef.getAttrValue(subreaction)),
          Double.valueOf(subreactionRef.getAttrValue(stoichiometry)));
      }
      tempProcessTypeAttributes.setSubreactions(subreactions);
      // add unprocessed Protein
      for (SpeciesReference species : reaction.getListOfReactants()) {
        // check if current product is a protein
        if (listTranslated.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setUnprocessed_protein_id(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      // add processed Protein
      for (SpeciesReference species : reaction.getListOfProducts()) {
        // check if current product is a protein
        if (listProcessed.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setProcessed_protein_id(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      tempProcessTypeAttributes.setBiomass_type(
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getAttrValue(biomassType));
      tempProcessTypeAttributes.setAggregation_propensity(
        Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                     .getAttrValue(aggregationPropensity)));
      tempProcessTypeAttributes.setPropensity_scaling(
        Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                     .getAttrValue(propensityScaling)));
      // add Translocations to ProcessData
      Map<String, Double> translocationMultipliers =
        new HashMap<String, Double>();
      List<String> translocationList = new ArrayList<String>();
      List<XMLNode> translocationReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listTranslocationReferences, "*")
                      .getChildElements(translocationRef, "*");
      // iterate over List of Translocation References
      for (Iterator<XMLNode> translocationIter =
        translocationReferences.iterator(); translocationIter.hasNext();) {
        XMLNode translocationRef = translocationIter.next();
        if (Double.valueOf(translocationRef.getAttrValue(multiplier))
                  .equals(0.0)) {
          translocationList.add(ConvertSBMLIdToCOBRAId(
            translocationRef.getAttrValue(translocation)));
        } else {
          translocationMultipliers.put(
            ConvertSBMLIdToCOBRAId(
              translocationRef.getAttrValue(translocation)),
            Double.valueOf(translocationRef.getAttrValue(multiplier)));
        }
      }
      tempProcessTypeAttributes.setTranslocation_multipliers(
        translocationMultipliers);
      tempProcessTypeAttributes.setTranslocation(translocationList);
      // add EquilibriumConstants to ProcessData
      Map<String, Double> keqFolding = new HashMap<String, Double>();
      List<XMLNode> EquilibriumConstantReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listEquilibrium, "*")
                      .getChildElements(rateConstant, "*");
      // iterate over List of Equilibrium Constant References
      for (Iterator<XMLNode> keqIter =
        EquilibriumConstantReferences.iterator(); keqIter.hasNext();) {
        XMLNode keqRef = keqIter.next();
        keqFolding.put(ConvertSBMLIdToCOBRAId(keqRef.getAttrValue(temperature)),
          Double.valueOf(keqRef.getAttrValue(rate)));
      }
      tempProcessTypeAttributes.setKeq_folding(keqFolding);
      // add RateConstants to ProcessData
      Map<String, Double> kFolding = new HashMap<String, Double>();
      List<XMLNode> RateConstantReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listRateConstant, "*")
                      .getChildElements(rateConstant, "*");
      // iterate over List of Rate Constant References
      for (Iterator<XMLNode> kIter =
        RateConstantReferences.iterator(); kIter.hasNext();) {
        XMLNode kRef = kIter.next();
        kFolding.put(ConvertSBMLIdToCOBRAId(kRef.getAttrValue(temperature)),
          Double.valueOf(kRef.getAttrValue(rate)));
      }
      tempProcessTypeAttributes.setK_folding(kFolding);
      // add surface area to ProcessData
      Map<String, Double> surfaceArea = new HashMap<String, Double>();
      if (sbmlAnnotation.getChildElement(reactionPlugin, "*")
                        .hasAttr(surfaceAreaInner)) {
        surfaceArea.put(saInner,
          Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                       .getAttrValue(surfaceAreaInner)));
      }
      if (sbmlAnnotation.getChildElement(reactionPlugin, "*")
                        .hasAttr(surfaceAreaOuter)) {
        surfaceArea.put(saOuter,
          Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                       .getAttrValue(surfaceAreaOuter)));
      }
      tempProcessTypeAttributes.setSurface_area(surfaceArea);
      tempProcessType.setPostTranslationData(tempProcessTypeAttributes);
      tempProcess.setProcessDataType(tempProcessType);
      processData.add(tempProcess);
    } else if (groupId.equals(complexFormationReaction)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      String sbmlComplexId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                           .getAttrValue(complexId);
      typeAttributes.setComplexDataId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      typeAttributes.setComplexFormationComplexId(
        ConvertSBMLIdToCOBRAId(sbmlComplexId));
      reactionType.setComplexFormation(typeAttributes);
      MEJsonProcessData tempProcess = new MEJsonProcessData();
      MEJsonProcessDataType tempProcessType = new MEJsonProcessDataType();
      MEJsonProcessDataTypeAttributes tempProcessTypeAttributes =
        new MEJsonProcessDataTypeAttributes();
      tempProcess.setId(ConvertSBMLIdToCOBRAId(sbmlDataId));
      // add subreations to ProcessData
      Map<String, Double> subreactions = new HashMap<String, Double>();
      List<XMLNode> subreactionReferences =
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getChildElement(listSubreactionReferences, "*")
                      .getChildElements(subreactionRef, "*");
      // iterate over List of Subreaction References
      for (Iterator<XMLNode> subreactionIter =
        subreactionReferences.iterator(); subreactionIter.hasNext();) {
        XMLNode subreactionRef = subreactionIter.next();
        subreactions.put(
          ConvertSBMLIdToCOBRAId(subreactionRef.getAttrValue(subreaction)),
          Double.valueOf(subreactionRef.getAttrValue(stoichiometry)));
      }
      tempProcessTypeAttributes.setSubreactions(subreactions);
      // set stoichiometry in Process Data
      Map<String, Double> stoichiometry = new HashMap<String, Double>();
      for (SpeciesReference species : reaction.getListOfReactants()) {
        // check if current reactant is a protein
        if (listTranslated.contains(species.getSpecies())) {
          stoichiometry.put(ConvertSBMLIdToCOBRAId(species.getSpecies()),
            species.getStoichiometry());
        }
      }
      tempProcessTypeAttributes.setStoichiometry(stoichiometry);
      // add Complex to Process Data
      for (SpeciesReference species : reaction.getListOfProducts()) {
        // check if current product is a complex
        if (listComplex.contains(species.getSpecies())) {
          tempProcessTypeAttributes.setComplex_id(
            ConvertSBMLIdToCOBRAId(species.getSpecies()));
          break;
        }
      }
      tempProcessType.setComplexData(tempProcessTypeAttributes);
      tempProcess.setProcessDataType(tempProcessType);
      processData.add(tempProcess);
    } else if (groupId.equals(metabolicReaction)) {
      XMLNode sbmlAnnotation = reaction.getAnnotation().getFullAnnotation();
      String sbmlDataId = sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                        .getAttrValue(dataId);
      typeAttributes.setStoichiometricDataId(
        ConvertSBMLIdToCOBRAId(sbmlDataId));
      typeAttributes.setMetabolicReactionComplexData(ConvertSBMLIdToCOBRAId(
        sbmlAnnotation.getChildElement(reactionPlugin, "*")
                      .getAttrValue(complexId)));
      typeAttributes.setKeff(
        Double.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                     .getAttrValue(keff)));
      typeAttributes.setReverse(
        Boolean.valueOf(sbmlAnnotation.getChildElement(reactionPlugin, "*")
                                      .getAttrValue(reverse)));
      reactionType.setMetabolicReaction(typeAttributes);
    }
    jsonReaction.setReactionType(reactionType);
    return jsonReaction;
  }
}
