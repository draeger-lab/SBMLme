package sbmlme;

import java.util.Arrays;
import java.util.List;

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
   * adds a single species from a COBRAme reaction list to the corresponding
   * SBML reaction
   * 
   * @param model
   * @param reaction
   * @param speciesId
   * @param coefficient
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
      if (coefficient.substring(0, 1).equals("-")) {
        SpeciesReference temp = reaction.createReactant(reaction.getId() + "___"
          + createSBMLConformId(speciesId) + "___reactant",
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
      } else {
        SpeciesReference temp = reaction.createProduct(reaction.getId() + "___"
          + createSBMLConformId(speciesId) + "___product",
          createSBMLConformId(speciesId));
        temp.setConstant(false);
        ASTNode coefficientNode = ASTNode.parseFormula(coefficient);
        InitialAssignment speciesAssignment = model.createInitialAssignment();
        speciesAssignment.setMath(coefficientNode);
        speciesAssignment.setVariable(temp.getId());
      }
    }
  }


  /**
   * set Bounds of a fbc reaction to a valid parameter
   * 
   * @param model
   * @param fbcTempReaction
   * @param upperBound
   * @param lowerBound
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
   * create and adds a SummaryVariable Reaction to the model.
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
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
   * create and add a formation reaction which transforms a species into a
   * generic Component
   * and add the reactant to the group of the generic component
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
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
   * create a transcription reaction
   * 
   * @param model
   * @param sbol
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param sequence
   * @param subreactions
   * @param subreactionCoefficients
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createTranscriptionReaction(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, String sequence,
    List<String> subreactions, List<Integer> subreactionCoefficients)
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
    if (subreactions != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (int i = 0; i < subreactions.size(); i++) {
        listSubRef.addChild(subReference.createSubreactionReference(
          subreactions.get(i), subreactionCoefficients.get(i)));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * create a translation reaction
   * 
   * @param model
   * @param sbol
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param sequence
   * @param subreactions
   * @param subreactionCoefficients
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createTranslationReaction(Model model, SBOLDocument sbol,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, String sequence,
    List<String> subreactions, List<Integer> subreactionCoefficients)
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
    if (subreactions != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (int i = 0; i < subreactions.size(); i++) {
        listSubRef.addChild(subReference.createSubreactionReference(
          subreactions.get(i), subreactionCoefficients.get(i)));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * create a tRNA charging reaction
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param keff
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param subreactions
   * @param subreactionCoefficients
   * @param synthetase
   * @param codon
   * @param aminoAcid
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createtRNAChargingReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, String dataId,
    double upperBound, double lowerBound, double keff, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind,
    List<String> subreactions, List<Integer> subreactionCoefficients,
    String synthetase, String codon, String aminoAcid)
    throws ParseException, SBOLValidationException {
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
    if (subreactions != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (int i = 0; i < subreactions.size(); i++) {
        listSubRef.addChild(subReference.createSubreactionReference(
          subreactions.get(i), subreactionCoefficients.get(i)));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * create a post translation reaction
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param keff
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param subreactions
   * @param subreactionCoefficients
   * @param aggregationPropensity
   * @param translocation
   * @param multipliers
   * @param propensityScaling
   * @param surfaceArea
   * @param surfaceAreaValue
   * @param keqFolding
   * @param keqValues
   * @param kFolding
   * @param kValues
   * @param biomassType
   * @throws ParseException
   */
  public void createPostTranslationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, List<String> subreactions,
    List<Integer> subreactionCoefficients, double aggregationPropensity,
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
    if (subreactions != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (int i = 0; i < subreactions.size(); i++) {
        listSubRef.addChild(subReference.createSubreactionReference(
          subreactions.get(i), subreactionCoefficients.get(i)));
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
   * create a complex formation reaction
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param subreactions
   * @param subreactionCoefficients
   * @throws ParseException
   */
  public void createComplexFormationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, String complexId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, List<String> subreactions,
    List<Integer> subreactionCoefficients) throws ParseException {
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
    if (subreactions != null) {
      // add subreaction references to annotations
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (int i = 0; i < subreactions.size(); i++) {
        listSubRef.addChild(subReference.createSubreactionReference(
          subreactions.get(i), subreactionCoefficients.get(i)));
      }
      meReaction.addChild(listSubRef);
    }
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * create a metabolic reaction based on the M-model
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param dataId
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
   * @param keff
   * @param reverse
   * @param complexDataId
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
   * create a ME exclusive reaction
   * 
   * @param model
   * @param groups
   * @param objective
   * @param id
   * @param name
   * @param upperBound
   * @param lowerBound
   * @param speciesIds
   * @param coefficients
   * @param objectiveCoefficient
   * @param variableKind
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
  public boolean isSetSequence() {
    return isSetAttribute(MEConstants.sequence);
  }


  public boolean isSetSynthetase() {
    return isSetAttribute(MEConstants.synthetase);
  }


  public boolean isSetComplexId() {
    return isSetAttribute(MEConstants.complexId);
  }


  public boolean isSetKeff() {
    return isSetAttribute(MEConstants.keff);
  }


  public boolean isSetDataId() {
    return isSetAttribute(MEConstants.dataId);
  }


  public boolean isSetStoichiometricRef() {
    return isSetAttribute(MEConstants.stoichiometricRef);
  }


  public boolean isSetReverse() {
    return isSetAttribute(MEConstants.reverse);
  }


  public boolean isSetCodon() {
    return isSetAttribute(MEConstants.codon);
  }


  public boolean isSetAminoAcid() {
    return isSetAttribute(MEConstants.aminoAcid);
  }


  public boolean isSetBiomassType() {
    return isSetAttribute(MEConstants.biomassType);
  }


  public boolean isSetSurfaceAreaInner() {
    return isSetAttribute(MEConstants.surfaceAreaInner);
  }


  public boolean isSetSurfaceAreaOuter() {
    return isSetAttribute(MEConstants.surfaceAreaOuter);
  }


  public boolean isSetAggregationPropensity() {
    return isSetAttribute(MEConstants.aggregationPropensity);
  }


  public boolean isSetPropensityScaling() {
    return isSetAttribute(MEConstants.propensityScaling);
  }


  public boolean isSetVariableKind() {
    return isSetAttribute(MEConstants.variableKind);
  }


  // getter functions for attributes
  public String getSequence() {
    if (isSetSequence()) {
      return getAttribute(MEConstants.sequence);
    }
    return "";
  }


  public String getComplexId() {
    if (isSetComplexId()) {
      return getAttribute(MEConstants.complexId);
    }
    return "";
  }


  public String getSynthetase() {
    if (isSetSynthetase()) {
      return getAttribute(MEConstants.synthetase);
    }
    return "";
  }


  public String getKeff() {
    if (isSetKeff()) {
      return getAttribute(MEConstants.keff);
    }
    return "";
  }


  public String getDataId() {
    if (isSetDataId()) {
      return getAttribute(MEConstants.dataId);
    }
    return "";
  }


  public String getStoichiometricRef() {
    if (isSetStoichiometricRef()) {
      return getAttribute(MEConstants.stoichiometricRef);
    }
    return "";
  }


  public String getReverse() {
    if (isSetReverse()) {
      return getAttribute(MEConstants.reverse);
    }
    return "";
  }


  public String getCodon() {
    if (isSetCodon()) {
      return getAttribute(MEConstants.codon);
    }
    return "";
  }


  public String getAminoAcid() {
    if (isSetAminoAcid()) {
      return getAttribute(MEConstants.aminoAcid);
    }
    return "";
  }


  public String getBiomassType() {
    if (isSetBiomassType()) {
      return getAttribute(MEConstants.biomassType);
    }
    return "";
  }


  public String getSurfaceAreaInner() {
    if (isSetSurfaceAreaInner()) {
      return getAttribute(MEConstants.surfaceAreaInner);
    }
    return "";
  }


  public String getSurfaceAreaOuter() {
    if (isSetSurfaceAreaOuter()) {
      return getAttribute(MEConstants.surfaceAreaOuter);
    }
    return "";
  }


  public String getAggregationPropensity() {
    if (isSetAggregationPropensity()) {
      return getAttribute(MEConstants.aggregationPropensity);
    }
    return "";
  }


  public String getPropensityScaling() {
    if (isSetPropensityScaling()) {
      return getAttribute(MEConstants.propensityScaling);
    }
    return "";
  }


  public String getVariableKind() {
    if (isSetVariableKind()) {
      return getAttribute(MEConstants.variableKind);
    }
    return "";
  }


  // Setter functions
  public int setSequence(String value) {
    return setAttribute(MEConstants.sequence, value);
  }


  public int setComplexId(String value) {
    return setAttribute(MEConstants.complexId, value);
  }


  public int setSynthetase(String value) {
    return setAttribute(MEConstants.synthetase, value);
  }


  public int setKeff(String value) {
    return setAttribute(MEConstants.keff, value);
  }


  public int setDataId(String value) {
    return setAttribute(MEConstants.dataId, value);
  }


  public int setStoichiometricRef(String value) {
    return setAttribute(MEConstants.stoichiometricRef, value);
  }


  public int setReverse(String value) {
    return setAttribute(MEConstants.reverse, value);
  }


  public int setCodon(String value) {
    return setAttribute(MEConstants.codon, value);
  }


  public int setAminoAcid(String value) {
    return setAttribute(MEConstants.aminoAcid, value);
  }


  public int setBiomassType(String value) {
    return setAttribute(MEConstants.biomassType, value);
  }


  public int setSurfaceAreaInner(String value) {
    return setAttribute(MEConstants.surfaceAreaInner, value);
  }


  public int setSurfaceAreaOuter(String value) {
    return setAttribute(MEConstants.surfaceAreaOuter, value);
  }


  public int setAggregationPropensity(String value) {
    return setAttribute(MEConstants.aggregationPropensity, value);
  }


  public int setPropensityScaling(String value) {
    return setAttribute(MEConstants.propensityScaling, value);
  }


  public int setVariableKind(String value) {
    return setAttribute(MEConstants.variableKind, value);
  }
}
