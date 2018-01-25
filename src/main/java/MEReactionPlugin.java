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
    super(new XMLTriple("meReactionPlugin", ns, prefix), new XMLAttributes());
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
        SpeciesReference temp = reaction.createReactant(
          reaction.getId() + "___" + speciesId + "___reactant", speciesId);
        temp.setConstant(false);
        // need to convert value due to COBRAme only possessing one list for
        // both reactants & products
        temp.setStoichiometry(tempCoefficient * -1.0);
      } else {
        SpeciesReference temp = reaction.createProduct(
          reaction.getId() + "___" + speciesId + "___product", speciesId);
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
        SpeciesReference temp = reaction.createReactant(
          reaction.getId() + "___" + speciesId + "___reactant", speciesId);
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
        SpeciesReference temp = reaction.createProduct(
          reaction.getId() + "___" + speciesId + "___product", speciesId);
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
    List<Parameter> listParameter = model.getListOfParameters();
    boolean upperSet = false;
    boolean upperNumber = false;
    boolean lowerSet = false;
    boolean lowerNumber = false;
    // Are the values for the Bounds simple numbers or
    if (upperBound.matches("-?\\d+\\.?\\d*")) {
      upperNumber = true;
    }
    if (lowerBound.matches("-?\\d+\\.?\\d*")) {
      lowerNumber = true;
    }
    // test if value already in a parameter
    for (int i = 0; i < listParameter.size(); i++) {
      if (upperNumber && !upperSet) {
        if (listParameter.get(i).getValue() == Double.valueOf(upperBound)) {
          fbcTempReaction.setUpperFluxBound(listParameter.get(i));
          upperSet = true;
        }
      } else if (!upperSet) {
        // test if bound equals an already known bound (either another bound or
        // an parameter used in other part of model
        if (listParameter.get(i).getId().equals(
          "me_bound_" + upperBound.replaceAll("[\\+\\-\\*\\/\\(\\)\\.]", "__"))
          || listParameter.get(i).getId().equals(upperBound)) {
          fbcTempReaction.setUpperFluxBound(listParameter.get(i));
          upperSet = true;
        }
      }
      if (lowerNumber && !lowerSet) {
        if (listParameter.get(i).getValue() == Double.valueOf(lowerBound)) {
          fbcTempReaction.setLowerFluxBound(listParameter.get(i));
          lowerSet = true;
        }
      } else if (!lowerSet) {
        // test if bound equals an already known bound (either another bound or
        // an parameter used in other part of model
        if (listParameter.get(i).getId().equals(
          "me_bound_" + lowerBound.replaceAll("[\\+\\-\\*\\/\\(\\)\\.]", "__"))
          || listParameter.get(i).getId().equals(lowerBound)) {
          fbcTempReaction.setLowerFluxBound(listParameter.get(i));
          lowerSet = true;
        }
      }
    }
    // if a bound is not set yet create a new parameter for it and set it
    if (!upperSet) {
      if (upperNumber) {
        Parameter upper = model.createParameter(
          "me_bound_" + upperBound.replaceAll("[\\.]", "__"));
        upper.setValue(Double.valueOf(upperBound));
        upper.setConstant(true);
        fbcTempReaction.setUpperFluxBound(upper);
      } else {
        Parameter upper = model.createParameter(
          "me_bound_" + upperBound.replaceAll("[\\+\\-\\*\\/\\(\\)\\.]", "__"));
        upper.setConstant(true);
        InitialAssignment upperIA = model.createInitialAssignment();
        upperIA.setMath(ASTNode.parseFormula(upperBound));
        upperIA.setVariable(upper.getId());
        fbcTempReaction.setUpperFluxBound(upper);
      }
    }
    if (!lowerSet) {
      if (lowerNumber) {
        Parameter lower = model.createParameter(
          "bound_" + lowerBound.replaceAll("[\\.]", "__"));
        lower.setValue(Double.valueOf(lowerBound));
        lower.setConstant(true);
        fbcTempReaction.setLowerFluxBound(lower);
      } else {
        Parameter lower = model.createParameter(
          "me_bound_" + lowerBound.replaceAll("[\\+\\-\\*\\/\\(\\)\\.]", "__"));
        lower.setConstant(true);
        InitialAssignment lowerIA = model.createInitialAssignment();
        lowerIA.setMath(ASTNode.parseFormula(lowerBound));
        lowerIA.setVariable(lower.getId());
        fbcTempReaction.setUpperFluxBound(lower);
      }
    }
  }


  // Functions for correctly setting COBRAme compliant reactions
  /**
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
    Reaction tempReaction = model.createReaction(id);
    tempReaction.initDefaults(2, 4, true);
    tempReaction.setName(name);
    Group group = (Group) groups.getGroup("summaryVariable");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("summaryVariable");
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
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    Group group = (Group) groups.getGroup("genericFormationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("genericFormationReaction");
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
          id + "___" + speciesIds.get(i) + "___reactant", speciesIds.get(i));
        // need to convert value due to COBRAme only possessing one list for
        // both reactants & products
        temp.setStoichiometry(tempCoefficient * -1.0);
        temp.setConstant(false);
        metabolite = speciesIds.get(i);
      } else {
        SpeciesReference temp = tempReaction.createProduct(
          id + "___" + speciesIds.get(i) + "___product", speciesIds.get(i));
        temp.setStoichiometry(tempCoefficient);
        temp.setConstant(false);
        generic = "genericData___" + speciesIds.get(i);
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
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("transcriptionReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("transcriptionReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    // add sequence to SBOL document
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    meSBOL.createSBOL(sbol, dataId, sequence, "DNA");
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setSequence(sbol.getDefaultURIprefix() + dataId);
    meReaction.setDataId(dataId);
    meReaction.addChild(listSubRef);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
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
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("translationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("translationReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    // add sequence to SBOL document
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    meSBOL.createSBOL(sbol, dataId, sequence, "mRNA");
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setSequence(sbol.getDefaultURIprefix() + dataId);
    meReaction.setDataId(dataId);
    meReaction.addChild(listSubRef);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
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
   * @throws ParseException
   * @throws SBOLValidationException
   */
  public void createtRNAChargingReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, String dataId,
    double upperBound, double lowerBound, double keff, List<String> speciesIds,
    List<String> coefficients, double objectiveCoefficient, String variableKind,
    List<String> subreactions, List<Integer> subreactionCoefficients,
    String synthetase, String codon)
    throws ParseException, SBOLValidationException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("tRNAChargingReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("tRNAChargingReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setCodon(codon);
    meReaction.setKeff(String.valueOf(keff));
    meReaction.setSynthetase(synthetase);
    meReaction.addChild(listSubRef);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
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
   * @param saOuter
   * @param saInner
   * @param keqFolding
   * @param keqValues
   * @param kFolding
   * @param kValues
   * @throws ParseException
   */
  public void createPostTranslationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, List<String> subreactions,
    List<Integer> subreactionCoefficients, double aggregationPropensity,
    List<String> translocation, List<Double> multipliers,
    double propensityScaling, double saOuter, double saInner,
    List<String> keqFolding, List<Double> keqValues, List<String> kFolding,
    List<Double> kValues) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    tempReaction.appendAnnotation(listSubRef);
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setAggregationPropensity(String.valueOf(aggregationPropensity));
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
    meReaction.setSurfaceAreaInner(String.valueOf(saInner));
    meReaction.setSurfaceAreaOuter(String.valueOf(saOuter));
    RateConstants rateConstants = new RateConstants();
    XMLNode listKeq = rateConstants.ListOfEquilibriumConstants();
    XMLNode listK = rateConstants.ListOfRateConstants();
    for (int i = 0; i < keqFolding.size(); i++) {
      listKeq.addChild(
        rateConstants.createRateConstant(keqFolding.get(i), keqValues.get(i)));
    }
    for (int i = 0; i < kFolding.size(); i++) {
      listK.addChild(
        rateConstants.createRateConstant(kFolding.get(i), kValues.get(i)));
    }
    meReaction.addChild(listKeq);
    meReaction.addChild(listK);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * reduced PostTranslationReaction without surface Area and rate constants
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
   * @param aggregationPropensity
   * @param translocation
   * @param multipliers
   * @param propensityScaling
   * @throws ParseException
   */
  public void createPostTranslationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, List<String> subreactions,
    List<Integer> subreactionCoefficients, double aggregationPropensity,
    List<String> translocation, List<Double> multipliers,
    double propensityScaling) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    tempReaction.appendAnnotation(listSubRef);
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    meReaction.setAggregationPropensity(String.valueOf(aggregationPropensity));
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
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * reduced PostTranslationReaction without surface area, rate constants and
   * subreactions
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
   * @param aggregationPropensity
   * @param translocation
   * @param multipliers
   * @param propensityScaling
   * @throws ParseException
   */
  public void createPostTranslationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind,
    double aggregationPropensity, List<String> translocation,
    List<Double> multipliers, double propensityScaling) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    tempReaction.appendAnnotation(meReaction);
  }


  /**
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
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind, List<String> subreactions,
    List<Integer> subreactionCoefficients) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    // add subreaction references to annotations
    SubreactionReference subReference = new SubreactionReference();
    XMLNode listSubRef = subReference.ListOfSubreactionReference();
    for (int i = 0; i < subreactions.size(); i++) {
      listSubRef.addChild(subReference.createSubreactionReference(
        subreactions.get(i), subreactionCoefficients.get(i)));
    }
    tempReaction.appendAnnotation(listSubRef);
    // add ME exclusive data to annotation
    MEReactionPlugin meReaction = new MEReactionPlugin();
    meReaction.setVariableKind(variableKind);
    meReaction.setDataId(dataId);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * create reduced ComplexFormationReaction without subreactions
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
   * @throws ParseException
   */
  public void createComplexFormationReaction(Model model,
    GroupsModelPlugin groups, Objective objective, String id, String name,
    String dataId, double upperBound, double lowerBound,
    List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    tempReaction.appendAnnotation(meReaction);
  }


  /**
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
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
    meReaction.setComplexId(complexDataId);
    tempReaction.appendAnnotation(meReaction);
  }


  /**
   * alternative if not all attributes are set in json
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
  public void createMetabolicReaction(Model model, GroupsModelPlugin groups,
    Objective objective, String id, String name, double upperBound,
    double lowerBound, List<String> speciesIds, List<String> coefficients,
    double objectiveCoefficient, String variableKind) throws ParseException {
    Reaction tempReaction = model.createReaction(id);
    tempReaction.setName(name);
    tempReaction.initDefaults(2, 4, true);
    // add reaction to group
    Group group = (Group) groups.getGroup("postTranslationReaction");
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup("postTranslationReaction");
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
