package sbmlme;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.InitialAssignment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCReactionPlugin;
import org.sbml.jsbml.ext.fbc.Objective;
import org.sbml.jsbml.ext.groups.Group;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;

/**
 * Implements attributes and methods to encode all information of reactions in a
 * COBRAme model in SBML.
 * <p>
 * Besides SBML core the packages fbc and groups are used in all reaction
 * creating methods of this class. The groups package is used to encode the
 * reaction type. The fbc package is used to encode the upper and lower bounds
 * and the objective coefficient of the reaction.
 * </p>
 * 
 * @author Marc A. Voigt
 */
public class MEReactionPlugin extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public MEReactionPlugin() {
    super(new XMLTriple(reactionPlugin, ns, prefix), new XMLAttributes());
  }


  public MEReactionPlugin(MEReactionPlugin rp) {
    super(rp);
    if (rp.isSetId()) {
      setId(rp.getId());
    }
  }


  /**
   * Adds a single species from a COBRAme reaction list to the corresponding
   * SBML reaction.
   * <p>
   * In COBRAme reactions only possess one list for both reactants and products.
   * When species are added from this list reactants and products need to be
   * separated, the method does this by checking if the species' coefficient is
   * negative.
   * In COBRAme coefficients can also be complex formulas that depend on
   * parameters. When such a coefficient is added the formula is checked for
   * currently unknown parameters that need to be added to the ListOfParameters
   * of the model. The check whether a species is a product or reactant is based
   * on the first character in the string that is not an opening bracket, if
   * this is a minus the species is assumed to be a reactant.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param reaction
   *        the SBML reaction that the species should be added to
   * @param speciesId
   *        the id of the species
   * @param coefficient
   *        the coefficient of the species
   * @throws ParseException
   */
  public void addCOBRAmeSpeciesToReaction(Model model, Reaction reaction,
    String speciesId, String coefficient) throws ParseException {
    // Is coefficient integer or double
    if (coefficient.matches("-?\\d+\\.?\\d*")) {
      double tempCoefficient = Double.valueOf(coefficient);
      if (tempCoefficient < 0) {
        SpeciesReference temp = reaction.createReactant(reaction.getId() + "___"
          + createSBMLConformId(speciesId) + "___reactant",
          createSBMLConformId(speciesId));
        temp.setConstant(false);
        // need to convert value due to COBRAme only possessing one list for
        // both reactants & products
        temp.setStoichiometry(tempCoefficient * -1.0);
      } else {
        SpeciesReference temp = reaction.createProduct(reaction.getId() + "___"
          + createSBMLConformId(speciesId) + "___product",
          createSBMLConformId(speciesId));
        temp.setStoichiometry(tempCoefficient);
        temp.setConstant(false);
      }
    } else {
      // search for new parameter ids
      List<String> parameterSearch =
        Arrays.asList(coefficient.replaceAll(" ", "")
                                 .split("(?<=[-+*/\\(\\)])|(?=[-+*/\\(\\)])"));
      for (int i = 0; i < parameterSearch.size(); i++) {
        // if element contains word characters and does not match an Integer
        if ((parameterSearch.get(i).matches("\\w+"))
          && !(parameterSearch.get(i).matches("\\d+"))) {
          // if element is not already in List of Parameters add it
          if (!model.containsParameter(parameterSearch.get(i))) {
            model.createParameter(parameterSearch.get(i));
            model.getParameter(parameterSearch.get(i)).initDefaults(2, 4, true);
            model.getParameter(parameterSearch.get(i)).setConstant(false);
          }
        }
      }
      // check the first character that is not an opening bracket, for minus to
      // decide between reactant and product
      String testCoefficient = coefficient;
      for (int i = 0; i < coefficient.length(); i++) {
        if (testCoefficient.substring(0, 1).equals("(")) {
          testCoefficient = testCoefficient.substring(1);
        } else if (testCoefficient.substring(0, 1).equals("-")) {
          SpeciesReference temp = reaction.createReactant(reaction.getId()
            + "___" + createSBMLConformId(speciesId) + "___reactant",
            createSBMLConformId(speciesId));
          temp.setConstant(false);
          ASTNode coefficientNode = ASTNode.parseFormula(coefficient);
          // need to convert value due to COBRAme only possessing one list for
          // both reactants & products
          ASTNode convert = new ASTNode();
          convert.setValue(-1);
          InitialAssignment speciesAssignment = model.createInitialAssignment();
          speciesAssignment.setMath(coefficientNode.multiplyWith(convert));
          speciesAssignment.setVariable(temp.getId());
          break;
        } else {
          SpeciesReference temp = reaction.createProduct(reaction.getId()
            + "___" + createSBMLConformId(speciesId) + "___product",
            createSBMLConformId(speciesId));
          temp.setConstant(false);
          ASTNode coefficientNode = ASTNode.parseFormula(coefficient);
          InitialAssignment speciesAssignment = model.createInitialAssignment();
          speciesAssignment.setMath(coefficientNode);
          speciesAssignment.setVariable(temp.getId());
          break;
        }
      }
    }
  }


  /**
   * Sets the Bounds of a fbc reaction to a valid parameter. If there is no
   * valid
   * parameter a new one is created.
   * <p>
   * In order to not create several parameters with the same value this method
   * translates the value into a String that can be used as a valid SBML id. The
   * method then searches the ListOfParameter for a parameter with the
   * translated id. If no such parameter is found a new parameter is created.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param fbcTempReaction
   *        the fbc plugin for the reaction
   * @param upperBound
   *        the designated upper bound
   * @param lowerBound
   *        the designated lower bound
   * @throws ParseException
   */
  public void setBounds(Model model, FBCReactionPlugin fbcTempReaction,
    String upperBound, String lowerBound) throws ParseException {
    boolean upperNumber = false;
    boolean lowerNumber = false;
    // Are the values for the Bounds simple numbers?
    if (upperBound.matches("-?\\d+\\.?\\d*")) {
      upperNumber = true;
    }
    if (lowerBound.matches("-?\\d+\\.?\\d*")) {
      lowerNumber = true;
    }
    // set basic ids for bounds that apply if the bound is an int or double
    String upperId = "me_bound_" + upperBound;
    upperId = upperId.replaceAll("\\-", "__minus__");
    upperId = upperId.replaceAll("\\.", "__");
    String lowerId = "me_bound_" + lowerBound;
    lowerId = lowerId.replaceAll("\\-", "__minus__");
    lowerId = lowerId.replaceAll("\\.", "__");
    // change the id if the bound is a coplex expression to create a valid SBML
    // id
    if (!upperNumber) {
      // change power operator from Pythons ** to ^ to make the value valid
      upperBound = upperBound.replaceAll("\\*\\*", "\\^");
      upperId = upperId.replaceAll("\\+", "__plus__");
      upperId = upperId.replaceAll("\\*\\*", "__power__");
      upperId = upperId.replaceAll("\\^", "__power__");
      upperId = upperId.replaceAll("\\*", "__times__");
      upperId = upperId.replaceAll("\\/", "__divide__");
      upperId = upperId.replaceAll("\\(", "__open__");
      upperId = upperId.replaceAll("\\)", "__close__");
      upperId = upperId.replaceAll(" ", "");
    }
    if (!lowerNumber) {
      // change power operator from Pythons ** to ^ to make the value valid
      lowerBound = lowerBound.replaceAll("\\*\\*", "\\^");
      lowerId = lowerId.replaceAll("\\+", "__plus__");
      lowerId = lowerId.replaceAll("\\*\\*", "__power__");
      lowerId = lowerId.replaceAll("\\^", "__power__");
      lowerId = lowerId.replaceAll("\\*", "__times__");
      lowerId = lowerId.replaceAll("\\/", "__divide__");
      lowerId = lowerId.replaceAll("\\(", "__open__");
      lowerId = lowerId.replaceAll("\\)", "__close__");
      lowerId = lowerId.replaceAll(" ", "");
    }
    // test if parameter already set
    if (model.getParameter(upperId) != null) {
      // case parameter already in model
      fbcTempReaction.setUpperFluxBound(upperId);
    } else if (upperNumber) {
      // case parameter not in model but is a double or int
      Parameter upper = model.createParameter(upperId);
      upper.setConstant(true);
      upper.setValue(Double.valueOf(upperBound));
      fbcTempReaction.setUpperFluxBound(upper);
    } else {
      // case parameter not in model and is a complex expression that may be
      // dependent on other parameters
      Parameter upper = model.createParameter(upperId);
      upper.setConstant(true);
      InitialAssignment upperIA = model.createInitialAssignment();
      upperIA.setMath(ASTNode.parseFormula(upperBound));
      upperIA.setVariable(upper.getId());
      fbcTempReaction.setUpperFluxBound(upper);
    }
    // test if parameter already set
    if (model.getParameter(lowerId) != null) {
      // case parameter already in model
      fbcTempReaction.setLowerFluxBound(lowerId);
    } else if (lowerNumber) {
      // case parameter not in model but is a double or int
      Parameter lower = model.createParameter(lowerId);
      lower.setConstant(true);
      lower.setValue(Double.valueOf(lowerBound));
      fbcTempReaction.setLowerFluxBound(lower);
    } else {
      // case parameter not in model and is a complex expression that may be
      // dependent on other parameters
      Parameter lower = model.createParameter(lowerId);
      lower.setConstant(true);
      InitialAssignment lowerIA = model.createInitialAssignment();
      lowerIA.setMath(ASTNode.parseFormula(lowerBound));
      lowerIA.setVariable(lower.getId());
      fbcTempReaction.setLowerFluxBound(lower);
    }
  }


  // Functions for correctly setting COBRAme compliant reactions
  /**
   * Create and add a SummaryVariable Reaction to the model.
   * <p>
   * In COBRAme this reaction type is used to apply global constraints to the
   * model. This method adds the SBMLme exclusive attribute "variableKind" to
   * the annotation of the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @throws ParseException
   */
  public void createSummaryVariableReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String upperBound, String lowerBound, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind)
    throws ParseException {
    id = createSBMLConformId(id);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    tempReaction.setName(name);
    Group group = (Group) groups.getGroup(summaryVariable);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(summaryVariable);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, upperBound, lowerBound);
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a formation reaction which transforms a species into a
   * generic Component.
   * <p>
   * This method also adds the reactant to the group of the generic component.
   * This method adds the SBMLme exclusive attribute "variableKind" to
   * the annotation of the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @throws ParseException
   */
  public void createGenericFormationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    double upperBound, double lowerBound, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind)
    throws ParseException {
    id = createSBMLConformId(id);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    Group group = (Group) groups.getGroup(genericFormation);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(genericFormation);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // create group for representing the genericData of COBRAme
    String metabolite = "";
    String generic = "";
    for (int i = 0; i < speciesIds.size(); i++) {
      double tempCoefficient = Double.valueOf(coefficients.get(i));
      if (tempCoefficient < 0) {
        SpeciesReference temp = tempReaction.createReactant(
          id + "___" + createSBMLConformId(speciesIds.get(i)) + "___reactant",
          createSBMLConformId(speciesIds.get(i)));
        // need to convert value due to COBRAme only possessing one list for
        // both reactants & products
        temp.setStoichiometry(tempCoefficient * -1.0);
        temp.setConstant(false);
        metabolite = createSBMLConformId(speciesIds.get(i));
      } else {
        // each GenericFormationReaction only possesses one reactant and one
        // product therefore the generic id can be used as group name
        SpeciesReference temp = tempReaction.createProduct(
          id + "___" + createSBMLConformId(speciesIds.get(i)) + "___product",
          createSBMLConformId(speciesIds.get(i)));
        temp.setStoichiometry(tempCoefficient);
        temp.setConstant(false);
        generic = "genericData___" + createSBMLConformId(speciesIds.get(i));
      }
    }
    // test if group already exists
    Group genericGroup = (Group) groups.getGroup(generic);
    if (genericGroup != null) {
      genericGroup.createMemberWithIdRef(metabolite);
    } else {
      genericGroup = groups.createGroup(generic);
      genericGroup.setKind(Group.Kind.collection);
      genericGroup.createMemberWithIdRef(metabolite);
    }
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a transcription reaction to the model.
   * <p>
   * This method also creates a new ComponentDefinition with the given sequence
   * in the SBOL document for the transcription unit. The method adds four
   * SBMLme attributes to the annotation of the reaction, "variableKind",
   * "dataId", "sequence" and "listOfSubreactionReferences". The attribute
   * "sequence" contains the URI of the ComponentDefinition in the SBOL document
   * for the transcription unit. The "listOfSubreactionReferences" contains all
   * subreactions that take part in the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param sbol
   *        the SBOL document to which the sequence should be added
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param sequence
   *        the nucleotide sequence of the transcription unit, cannot be null or
   *        empty string
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createTranscriptionReaction(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, String sequence,
    Map<String, Double> subreactionMap)
    throws ParseException, SBOLValidationException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(transcription);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(transcription);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add sequence to SBOL document
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    MEReactionPlugin meReaction = new MEReactionPlugin();
    // test if dataId is also a species id which is valid in COBRAme but would
    // not work in SBOL because ids must be globally unique here
    if (model.getSpecies(dataId) != null) {
      meSBOL.createSBOL(sbol, dataId + "__REACTION", sequence, "DNA");
      meReaction.setSequence(
        sbol.getDefaultURIprefix() + dataId + "__REACTION");
    } else {
      meSBOL.createSBOL(sbol, dataId, sequence, "DNA");
      meReaction.setSequence(sbol.getDefaultURIprefix() + dataId);
    }
    // add ME exclusive data to annotation
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    // test should only return false if information is missing
    // might need to print out a warning during validation
    if (subreactionMap != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a translation reaction to the model.
   * <p>
   * This method also creates a new ComponentDefinition with the given sequence
   * in the SBOL document for the transcribed mRNA. The method adds four
   * SBMLme attributes to the annotation of the reaction, "variableKind",
   * "dataId", "sequence" and "listOfSubreactionReferences". The attribute
   * "sequence" contains the URI of the ComponentDefinition in the SBOL document
   * for the transcription unit. The "listOfSubreactionReferences" contains all
   * subreactions that take part in the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param sbol
   *        the SBOL document to which the sequence should be added
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param sequence
   *        the nucleotide sequence of the transcribed mRNA, cannot be null or
   *        empty string
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createTranslationReaction(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, String sequence,
    Map<String, Double> subreactionMap)
    throws ParseException, SBOLValidationException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(translation);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(translation);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add sequence to SBOL document
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    MEReactionPlugin meReaction = new MEReactionPlugin();
    // test if dataId is also a species id which is valid in COBRAme but would
    // not work in SBOL because ids must be globally unique here
    if (model.getSpecies(dataId) != null) {
      meSBOL.createSBOL(sbol, dataId + "__REACTION", sequence, "mRNA");
      meReaction.setSequence(
        sbol.getDefaultURIprefix() + dataId + "__REACTION");
    } else {
      meSBOL.createSBOL(sbol, dataId, sequence, "mRNA");
      meReaction.setSequence(sbol.getDefaultURIprefix() + dataId);
    }
    // add ME exclusive data to annotation
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    // test should only return false if information is missing
    // might need to print out a warning during validation
    if (subreactionMap != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a tRNA charging reaction to the model.
   * <p>
   * The method adds the attributes "dataId", "keff", "variableKind",
   * "synthetase", "codon", "aminoAcid" and "listOfSubreactionReferences" to the
   * annotation of the reaction. The "listOfSubreactionReferences" contains all
   * subreactions that take part in the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param keff
   *        the effective turnover rate of the synthetase of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @param synthetase
   *        the id of the tRNA synthetase to charge the tRNA with an amino acid
   * @param codon
   *        the codon that is translated by the tRNA
   * @param aminoAcid
   *        the id of the amino acid that the tRNA transfers to a peptide
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createtRNAChargingReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, String dataId,
    double upperBound, double lowerBound, double keff, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind,
    Map<String, Double> subreactionMap, String synthetase, String codon,
    String aminoAcid) throws ParseException, SBOLValidationException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(tRNACharging);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(tRNACharging);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setCodon(codon);
    meReaction.setKeff(String.valueOf(keff));
    meReaction.setSynthetase(synthetase);
    meReaction.setAminoAcid(aminoAcid);
    // test should only return false if information is missing
    // might need to print out a warning during validation
    if (subreactionMap != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a post translation reaction to the model.
   * <p>
   * The method adds the SBMLme attributes "dataID", "variableKind",
   * "aggregationPropensity", "propensityScaling", "biomassType",
   * "surfaceAreaInnerMembrane", "surfaceAreaOuterMembrane",
   * "listOfSubreactionReferences" and "listOfTranslocationReferences" to the
   * annotation of the reaction. The "listOfSubreactionReferences" contains all
   * "subreactionReference" that take part in the reaction. The
   * "listOfTranslocationReferences" contains all "translocationReferences" that
   * take part in the reaction. The attributes "surfaceAreaInnerMembrane" and
   * "surfaceAreaOuterMembrane" contain the value of the area occupied by the
   * protein in the inner/outer membrane.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @param aggregationPropensity
   *        the aggregation propensity for the protein
   * @param translocation
   *        the list of ids of translocation pathways involved in the reaction,
   *        may be null
   * @param multipliers
   *        the list of multipliers belonging to the translocations, the order
   *        of both lists must be the same, may be null
   * @param propensityScaling
   *        accounts for the propensity of some peptides to be folded by certain
   *        chaperones
   * @param surfaceArea
   *        the list for the possible surfaces, valid values are
   *        "SA_inner_membrane" and "SA_outer_membrane", may be null
   * @param surfaceAreaValue
   *        the list for the area occupied by the protein in the surfaces given
   *        in the list "surfaceArea", the order of both lists must be the same,
   *        may be null
   * @param keqFolding
   *        list of temperatures for keq folding, may be null
   * @param keqValues
   *        list of temperature dependent keq folding values for the protein,
   *        may be null
   * @param kFolding
   *        list of temperatures for k folding, may be null
   * @param kValues
   *        list of temperature dependent rate constants for protein folding,
   *        may be null
   * @param biomassType
   *        the id of the biomass type added by the subreactions of the
   *        reaction, may be null
   * @throws ParseException
   */
  public void createPostTranslationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind,
    Map<String, Double> subreactionMap, double aggregationPropensity,
    List<String> translocation, List<Double> multipliers,
    double propensityScaling, List<String> surfaceArea,
    List<Double> surfaceAreaValue, List<String> keqFolding,
    List<Double> keqValues, List<String> kFolding, List<Double> kValues,
    String biomassType) throws ParseException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(postTranslationReaction);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(postTranslationReaction);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setAggregationPropensity(String.valueOf(aggregationPropensity));
    meReaction.setBiomassType(biomassType);
    if (subreactionMap != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      meReaction.addChild(listSubRef);
    }
    TranslocationReference transloc = new TranslocationReference();
    // create and add combined list of translocations, elements with multipliers
    // not zero refer to COBRAme list of translocation multipliers while the
    // rest refer to the list of translocations
    XMLNode listTranslocations = transloc.ListOfTranslocationReference();
    for (int i = 0; i < translocation.size(); i++) {
      listTranslocations.addChild(transloc.createTranslocationReference(
        translocation.get(i), multipliers.get(i)));
    }
    meReaction.addChild(listTranslocations);
    meReaction.setPropensityScaling(String.valueOf(propensityScaling));
    // surfaceArea can only have two entries: "SA_inner_membrane" and
    // "SA_outer_membrane"
    if (surfaceArea != null) {
      if (!surfaceArea.isEmpty()) {
        if (surfaceArea.get(0).equals("SA_inner_membrane")) {
          meReaction.setSurfaceAreaInner(
            String.valueOf(surfaceAreaValue.get(0)));
          meReaction.setSurfaceAreaOuter(
            String.valueOf(surfaceAreaValue.get(1)));
        } else {
          meReaction.setSurfaceAreaInner(
            String.valueOf(surfaceAreaValue.get(1)));
          meReaction.setSurfaceAreaOuter(
            String.valueOf(surfaceAreaValue.get(0)));
        }
      }
    }
    RateConstants rateConstants = new RateConstants();
    if (keqFolding != null) {
      XMLNode listKeq = rateConstants.ListOfEquilibriumConstants();
      for (int i = 0; i < keqFolding.size(); i++) {
        listKeq.addChild(rateConstants.createRateConstant(keqFolding.get(i),
          keqValues.get(i)));
      }
      meReaction.addChild(listKeq);
    }
    if (kFolding != null) {
      XMLNode listK = rateConstants.ListOfRateConstants();
      for (int i = 0; i < kFolding.size(); i++) {
        listK.addChild(
          rateConstants.createRateConstant(kFolding.get(i), kValues.get(i)));
      }
      meReaction.addChild(listK);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a complex formation reaction to the model.
   * <p>
   * The method adds the SBMLme attributes "dataId", "variableKind" and
   * "ListOfSubreactionReferences". The "listOfSubreactionReferences" contains
   * all subreactions that take part in the reaction.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @throws ParseException
   */
  public void createComplexFormationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, String complexId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind,
    Map<String, Double> subreactionMap) throws ParseException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    complexId = createSBMLConformId(complexId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(complexFormationReaction);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(complexFormationReaction);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setComplexId(complexId);
    if (subreactionMap != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add a metabolic reaction to the model.
   * <p>
   * In COBRAme all reversible enzymatic reactions are broken into two
   * irreversible reactions representing the two directions of the reaction.
   * Therefore the boolean attribute "reverse" is added to the annotation of the
   * reaction to define the direction. The attributes "dataId", "keff",
   * "variableKind" and "complexDataId" are also added to the annotation.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param dataId
   *        the id of the process data object that is referred by the reaction
   *        in COBRAme, cannot be null or empty string
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param keff
   *        the effective turnover rate of the enzymes in the translocation
   *        pathway
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @param keff
   *        the effective turnover rate that couples enzymatic dilution to
   *        metabolic flux
   * @param reverse
   *        whether the reaction corresponds to the reverse reaction of the
   *        reversible reaction
   * @param complexDataId
   *        the id of the ComplexData object that details the enzyme which
   *        catalyzes the reaction
   * @throws ParseException
   */
  public void createMetabolicReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, String dataId,
    double upperBound, double lowerBound, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind,
    double keff, boolean reverse, String complexDataId) throws ParseException {
    id = createSBMLConformId(id);
    dataId = createSBMLConformId(dataId);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(metabolicReaction);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(metabolicReaction);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setKeff(String.valueOf(keff));
    meReaction.setReverse(String.valueOf(reverse));
    // set optional ComplexDataId if not null
    if (complexDataId != null) {
      complexDataId = createSBMLConformId(complexDataId);
      meReaction.setComplexId(complexDataId);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * Create and add an ME exclusive reaction to the model.
   * <p>
   * This method adds the SBMLme exclusive attribute "variableKind" to the
   * annotation of the reaction.
   * 
   * @param model
   *        the SBML model
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param objective
   *        the active objective of the SBML model
   * @param id
   *        the id of the reaction to be added
   * @param name
   *        the name of the reaction, can be null
   * @param upperBound
   *        the upper bound of the reaction
   * @param lowerBound
   *        the lower bound of the reaction
   * @param speciesIds
   *        the list with the ids of all species in the reaction
   * @param coefficients
   *        the list with all species coefficients in the reaction, must have
   *        the same order as the list with species ids
   * @param objectiveCoefficient
   *        the coefficient of the flux objective of the reaction
   * @param variableKind
   *        can be either "continuous" or "discrete"
   * @throws ParseException
   */
  public void createMEReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, double upperBound,
    double lowerBound, List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind) throws ParseException {
    id = createSBMLConformId(id);
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setReversible(false);
    // add reaction to group
    Group group = (Group) groups.getGroup(meReaction);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(meReaction);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
    // set fbc data
    FBCReactionPlugin fbcTempReaction =
      (FBCReactionPlugin) tempReaction.getPlugin(FBCConstants.shortLabel);
    setBounds(model, fbcTempReaction, String.valueOf(upperBound),
      String.valueOf(lowerBound));
    objective.createFluxObjective(id + "_coefficient", "", objectiveCoefficient,
      model.getReaction(id));
    // add species to reaction
    for (int i = 0; i < speciesIds.size(); i++) {
      addCOBRAmeSpeciesToReaction(model, tempReaction, speciesIds.get(i),
        coefficients.get(i));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    tempReaction.appendAnnotation(meReaction);
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "sequence" is set.
   * 
   * @return whether the attribute "sequence" is set.
   */
  public boolean isSetSequence() {
    return isSetAttribute(MEConstants.sequence);
  }


  /**
   * Returns whether the attribute "synthetase" is set.
   * 
   * @return whether the attribute "synthetase" is set.
   */
  public boolean isSetSynthetase() {
    return isSetAttribute(MEConstants.synthetase);
  }


  /**
   * Returns whether the attribute "complexId" is set.
   * 
   * @return whether the attribute "complexId" is set.
   */
  public boolean isSetComplexId() {
    return isSetAttribute(MEConstants.complexId);
  }


  /**
   * Returns whether the attribute "keff" is set.
   * 
   * @return whether the attribute "keff" is set.
   */
  public boolean isSetKeff() {
    return isSetAttribute(MEConstants.keff);
  }


  /**
   * Returns whether the attribute "dataId" is set.
   * 
   * @return whether the attribute "dataId" is set.
   */
  public boolean isSetDataId() {
    return isSetAttribute(MEConstants.dataId);
  }


  /**
   * Returns whether the attribute "reverse" is set.
   * 
   * @return whether the attribute "reverse" is set.
   */
  public boolean isSetReverse() {
    return isSetAttribute(MEConstants.reverse);
  }


  /**
   * Returns whether the attribute "codon" is set.
   * 
   * @return whether the attribute "codon" is set.
   */
  public boolean isSetCodon() {
    return isSetAttribute(MEConstants.codon);
  }


  /**
   * Returns whether the attribute "aminoAcid" is set.
   * 
   * @return whether the attribute "aminoAcid" is set.
   */
  public boolean isSetAminoAcid() {
    return isSetAttribute(MEConstants.aminoAcid);
  }


  /**
   * Returns whether the attribute "biomassType" is set.
   * 
   * @return whether the attribute "biomassType" is set.
   */
  public boolean isSetBiomassType() {
    return isSetAttribute(MEConstants.biomassType);
  }


  /**
   * Returns whether the attribute "surfaceAreaInnerMembrane" is set.
   * 
   * @return whether the attribute "surfaceAreaInnerMembrane" is set.
   */
  public boolean isSetSurfaceAreaInner() {
    return isSetAttribute(MEConstants.surfaceAreaInner);
  }


  /**
   * Returns whether the attribute "surfaceAreaOuterMembrane" is set.
   * 
   * @return whether the attribute "surfaceAreaOuterMembrane" is set.
   */
  public boolean isSetSurfaceAreaOuter() {
    return isSetAttribute(MEConstants.surfaceAreaOuter);
  }


  /**
   * Returns whether the attribute "aggregationPropensity" is set.
   * 
   * @return whether the attribute "aggregationPropensity" is set.
   */
  public boolean isSetAggregationPropensity() {
    return isSetAttribute(MEConstants.aggregationPropensity);
  }


  /**
   * Returns whether the attribute "propensityScaling" is set.
   * 
   * @return whether the attribute "propensityScaling" is set.
   */
  public boolean isSetPropensityScaling() {
    return isSetAttribute(MEConstants.propensityScaling);
  }


  /**
   * Returns whether the attribute "variableKind" is set.
   * 
   * @return whether the attribute "variableKind" is set.
   */
  public boolean isSetVariableKind() {
    return isSetAttribute(MEConstants.variableKind);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "sequence".
   * 
   * @return the value of the attribute "sequence".
   */
  public String getSequence() {
    if (isSetSequence()) {
      return getAttribute(MEConstants.sequence);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "complexId".
   * 
   * @return the value of the attribute "complexId".
   */
  public String getComplexId() {
    if (isSetComplexId()) {
      return getAttribute(MEConstants.complexId);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "synthetase".
   * 
   * @return the value of the attribute "synthetase".
   */
  public String getSynthetase() {
    if (isSetSynthetase()) {
      return getAttribute(MEConstants.synthetase);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "keff".
   * 
   * @return the value of the attribute "keff".
   */
  public String getKeff() {
    if (isSetKeff()) {
      return getAttribute(MEConstants.keff);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "dataId".
   * 
   * @return the value of the attribute "dataId".
   */
  public String getDataId() {
    if (isSetDataId()) {
      return getAttribute(MEConstants.dataId);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "reverse".
   * 
   * @return the value of the attribute "reverse".
   */
  public String getReverse() {
    if (isSetReverse()) {
      return getAttribute(MEConstants.reverse);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "codon".
   * 
   * @return the value of the attribute "codon".
   */
  public String getCodon() {
    if (isSetCodon()) {
      return getAttribute(MEConstants.codon);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "aminoAcid".
   * 
   * @return the value of the attribute "aminoAcid".
   */
  public String getAminoAcid() {
    if (isSetAminoAcid()) {
      return getAttribute(MEConstants.aminoAcid);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "biomassType".
   * 
   * @return the value of the attribute "biomassType".
   */
  public String getBiomassType() {
    if (isSetBiomassType()) {
      return getAttribute(MEConstants.biomassType);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "surfaceAreaInnerMembrane".
   * 
   * @return the value of the attribute "surfaceAreaInnerMembrane".
   */
  public String getSurfaceAreaInner() {
    if (isSetSurfaceAreaInner()) {
      return getAttribute(MEConstants.surfaceAreaInner);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "surfaceAreaOuterMembrane".
   * 
   * @return the value of the attribute "surfaceAreaOuterMembrane".
   */
  public String getSurfaceAreaOuter() {
    if (isSetSurfaceAreaOuter()) {
      return getAttribute(MEConstants.surfaceAreaOuter);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "aggregationPropensity".
   * 
   * @return the value of the attribute "aggregationPropensity".
   */
  public String getAggregationPropensity() {
    if (isSetAggregationPropensity()) {
      return getAttribute(MEConstants.aggregationPropensity);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "propensityScaling".
   * 
   * @return the value of the attribute "propensityScaling".
   */
  public String getPropensityScaling() {
    if (isSetPropensityScaling()) {
      return getAttribute(MEConstants.propensityScaling);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "variableKind".
   * 
   * @return the value of the attribute "variableKind".
   */
  public String getVariableKind() {
    if (isSetVariableKind()) {
      return getAttribute(MEConstants.variableKind);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "sequence".
   * <p>
   * The optional attribute "sequence" takes a string that represents a
   * valid URI for a ComponentDefinition in an SBOL document. This attribute is
   * mandatory when the reaction is of type "TranscriptionReaction" or of type
   * "TranslationReaction".
   * </p>
   * 
   * @param value
   *        valid URI of a ComponentDefinition in an SBOL document
   * @return
   */
  public int setSequence(String value) {
    return setAttribute(MEConstants.sequence, value);
  }


  /**
   * Sets the value of the attribute "complexId".
   * <p>
   * The optional attribute "complexId" takes a string representing the Id of
   * the ComplexData object that is referred by the reaction. This attribute is
   * mandatory for reactions of type "ComplexFormationReaction" and
   * "MetabolicReaction".
   * </p>
   * 
   * @param value
   *        id of the ComplexData object that is referred in COBRAme
   * @return
   */
  public int setComplexId(String value) {
    return setAttribute(MEConstants.complexId, value);
  }


  /**
   * Sets the value of the attribute "synthetase".
   * <p>
   * The optional attribute "synthetase" takes a string representing the Id of a
   * synthetase. This attribute is mandatory for reactions of type
   * "tRNAChargingReaction".
   * </p>
   * 
   * @param value
   *        id of the synthetase that synthesizes the tRNA charging
   * @return
   */
  public int setSynthetase(String value) {
    return setAttribute(MEConstants.synthetase, value);
  }


  /**
   * Sets the value of the attribute "keff".
   * <p>
   * The optional attribute "keff" contains the turnover rate of an enzyme
   * catalyzing a reaction. The attribute is mandatory for reactions of type
   * "MetabolicReaction".
   * </p>
   * 
   * @param value
   *        the effective turnover rate of the enzyme catalyzing the reaction
   * @return
   */
  public int setKeff(String value) {
    return setAttribute(MEConstants.keff, value);
  }


  /**
   * Sets the value of the attribute "dataId".
   * <p>
   * The optional attribute "dataId" contains the Id of the process data object
   * in COBRAme that contains additional information about the reaction. The
   * attribute is mandatory for all reaction types except "SummaryVariable",
   * "MEReaction" and "GenericFormationreaction".
   * </p>
   * 
   * @param value
   *        the Id of the process data object in COBRAme containing additional
   *        reaction information.
   * @return
   */
  public int setDataId(String value) {
    return setAttribute(MEConstants.dataId, value);
  }


  /**
   * Sets the value of the attribute "reverse".
   * <p>
   * The optional attribute "reverse" contains a boolean representing if the
   * "MetabolicReaction" is the reverse direction of a reversible reaction or
   * not.
   * </p>
   * 
   * @param value
   *        whether the reaction represents the reverse direction or not
   * @return
   */
  public int setReverse(String value) {
    return setAttribute(MEConstants.reverse, value);
  }


  /**
   * Sets the value of the attribute "codon".
   * <p>
   * The optional attribute "codon" takes a string representing a codon that
   * will be translated to a specific amino acid. This attribute should only be
   * used in reactions of type "tRNAChargingReaction".
   * </p>
   * 
   * @param value
   *        a String containing a triplet of nucleotides in single-letter
   *        representation
   * @return
   */
  public int setCodon(String value) {
    return setAttribute(MEConstants.codon, value);
  }


  /**
   * Sets the value of the attribute "aminoAcid".
   * <p>
   * The optional attribute "aminoAcid" takes a string representing an amino
   * acid that will be added to a peptide by the charged tRNA. This attribute
   * should only be used in reactions of type "tRNAChargingReaction".
   * </p>
   * 
   * @param value
   *        a String containing the id of an amino acid
   * @return
   */
  public int setAminoAcid(String value) {
    return setAttribute(MEConstants.aminoAcid, value);
  }


  /**
   * Sets the value of the attribute "biomassType".
   * <p>
   * The optional attribute "biomassType" takes a string representing a type of
   * biomass that is used in reactions of type "PostTranslationReaction". This
   * attribute is used when biomass is added to the translated protein to
   * indicate which type of biomass is added by the reaction.
   * </p>
   * 
   * @param value
   *        a String containing the id of an constraint representing a type of
   *        biomass
   * @return
   */
  public int setBiomassType(String value) {
    return setAttribute(MEConstants.biomassType, value);
  }


  /**
   * Sets the value of the attribute "surfaceAreaInnerMembrane".
   * <p>
   * The optional attribute "surfaceAreaInnerMembrane" contains information
   * about the area that is occupied by the protein altered by the reaction of
   * type "PostTranslationReaction".
   * </p>
   * 
   * @param value
   *        a double representing the area that is occupied by the protein
   * @return
   */
  public int setSurfaceAreaInner(String value) {
    return setAttribute(MEConstants.surfaceAreaInner, value);
  }


  /**
   * Sets the value of the attribute "surfaceAreaOuterMembrane".
   * <p>
   * The optional attribute "surfaceAreaOuterMembrane" contains information
   * about the area that is occupied by the protein altered by the reaction of
   * type "PostTranslationReaction".
   * </p>
   * 
   * @param value
   *        a double representing the area that is occupied by the protein
   * @return
   */
  public int setSurfaceAreaOuter(String value) {
    return setAttribute(MEConstants.surfaceAreaOuter, value);
  }


  /**
   * Sets the value of the attribute "aggregationPropensity".
   * <p>
   * The optional attribute "aggregationPropensity" contains information
   * about propensity of the protein to aggregate. This attribute is mandatory
   * for reactions of type "PostTranslationReaction".
   * </p>
   * 
   * @param value
   *        a double representing the propensity of the protein to aggregate.
   * @return
   */
  public int setAggregationPropensity(String value) {
    return setAttribute(MEConstants.aggregationPropensity, value);
  }


  /**
   * Sets the value of the attribute "propensityScaling".
   * <p>
   * The optional attribute "propensityScaling" contains information
   * about propensity of the protein to aggregate. This attribute is mandatory
   * for reactions of type "PostTranslationReaction".
   * </p>
   * 
   * @param value
   *        string representation of a double value accounting for the
   *        propensity of some peptides to be folded by certain chaperones
   * @return
   */
  public int setPropensityScaling(String value) {
    return setAttribute(MEConstants.propensityScaling, value);
  }


  /**
   * Sets the value of the mandatory attribute "variableKind".
   * <p>
   * The attribute defines if the values for the reaction flux are continuous or
   * discrete.
   * </p>
   * 
   * @param value
   *        a string, either "continuous" or "discrete"
   * @return
   */
  public int setVariableKind(String value) {
    return setAttribute(MEConstants.variableKind, value);
  }
}
