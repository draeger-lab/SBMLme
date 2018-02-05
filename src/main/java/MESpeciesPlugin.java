import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCSpeciesPlugin;
import org.sbml.jsbml.ext.groups.Group;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLTriple;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;

/**
 * @author Marc A. Voigt
 */
public class MESpeciesPlugin extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public MESpeciesPlugin() {
    super(new XMLTriple("meSpeciesPlugin", ns, prefix), new XMLAttributes());
  }


  public MESpeciesPlugin(MESpeciesPlugin sp) {
    super(sp);
    if (sp.isSetId()) {
      setId(sp.getId());
    }
  }


  /**
   * Since COBRAme stores the information about compartments in their species
   * Ids they need to be extracted
   * ATTENTION: A COBRAme model may contain species Ids which do not include
   * information about their compartments. In this case it is assumed that the
   * species is in the cytosol.
   * Currently only one letter abbreviations for compartments are supported.
   * 
   * @param model
   * @param id
   * @return
   */
  public Compartment getCompartmentFromId(Model model, String id) {
    // COBRAme ids may contain double underscores which do not declare the
    // department and are therefore removed
    id = id.replaceAll("__", "");
    if (id.substring(id.length() - 2, id.length() - 1).equals("_")) {
      // last element identifies the compartment
      String compartmentId = id.substring(id.length() - 1, id.length());
      Compartment comp = model.getCompartment(compartmentId);
      if (comp != null) {
        return comp;
      } else {
        comp = model.createCompartment(compartmentId);
        // it cannot be assumed that the compartment is constant therefore the
        // attribute is set to false
        comp.initDefaults(2, 4, true);
        comp.setConstant(false);
        return comp;
      }
      // if no compartment is given in the identifier assume cytosol
    } else {
      // assume identifier for cytosol is c
      Compartment comp = model.getCompartment("c");
      if (comp != null) {
        return comp;
      } else {
        comp = model.createCompartment("c");
        // it cannot be assumed that the compartment is constant therefore the
        // attribute is set to false
        comp.initDefaults(2, 4, true);
        comp.setConstant(false);
        return comp;
      }
    }
  }


  /**
   * create COBRAme species with an explicitly given compartment
   * 
   * @param model
   * @param id
   * @param formula
   * @param name
   * @param c
   * @param groups
   * @param groupName
   */
  public void createMESpecies(Model model, String id, String formula,
    String name, Compartment c, GroupsModelPlugin groups, String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  /**
   * create COBRAme species
   * 
   * @param model
   * @param id
   * @param formula
   * @param name
   * @param groups
   * @param groupName
   */
  public void createMESpecies(Model model, String id, String formula,
    String name, GroupsModelPlugin groups, String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies =
      model.createSpecies(id, name, getCompartmentFromId(model, id));
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  /**
   * create COBRAme processed species with an explicitly given compartment
   * 
   * @param model
   * @param id
   * @param formula
   * @param name
   * @param c
   * @param unprocessedId
   * @param group
   */
  public void createMEProcessedSpecies(Model model, String id, String formula,
    String name, Compartment c, String unprocessedId, GroupsModelPlugin groups,
    String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    MESpeciesPlugin seqME = new MESpeciesPlugin();
    seqME.setUnprocessed(createSBMLConformId(unprocessedId));
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  /**
   * create COBRAme processed species without an explicitly given compartment
   * 
   * @param model
   * @param id
   * @param formula
   * @param name
   * @param unprocessedId
   * @param groups
   * @param groupName
   */
  public void createMEProcessedSpecies(Model model, String id, String formula,
    String name, String unprocessedId, GroupsModelPlugin groups,
    String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies =
      model.createSpecies(id, name, getCompartmentFromId(model, id));
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    MESpeciesPlugin seqME = new MESpeciesPlugin();
    // for continuity
    seqME.setUnprocessed(createSBMLConformId(unprocessedId));
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  /**
   * create COBRAme Sequence species
   * outdated, needs to be removed once model is updated
   * 
   * @param model
   * @param id
   * @param name
   * @param c
   * @param formula
   * @param sbol
   * @param sequence
   * @param role
   * @param orientation
   * @param genomePosition
   * @param group
   * @throws SBOLValidationException
   */
  public void createMESequenceSpecies(Model model, String id, String name,
    Compartment c, String formula, SBOLDocument sbol, String sequence,
    String role, String orientation, int genomePosition,
    GroupsModelPlugin groups, String groupName) throws SBOLValidationException {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    OrientationType ori = null;
    if ((orientation == "+") || (orientation == "inline")
      || (orientation == "INLINE") || (orientation == "Inline")
      || (orientation == "Forward") || (orientation == "FORWARD")) {
      ori = OrientationType.INLINE;
    } else {
      ori = OrientationType.REVERSECOMPLEMENT;
    }
    meSBOL.createSBOLSequenceWithAnnotation(sbol, id, sequence, role, ori);
    MESpeciesPlugin seqME = new MESpeciesPlugin();
    seqME.setSequence(sbol.getDefaultURIprefix() + id);
    seqME.setGenomePos(Integer.toString(genomePosition));
    meSpecies.getAnnotation().appendNonRDFAnnotation(seqME);
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  /**
   * create COBRAme Sequence species
   * 
   * @param model
   * @param id
   * @param name
   * @param formula
   * @param sbol
   * @param sequence
   * @param role
   * @param orientation
   * @param genomePosition
   * @param group
   * @throws SBOLValidationException
   */
  public void createMESequenceSpecies(Model model, String id, String formula,
    String name, SBOLDocument sbol, String sequence, String role,
    String orientation, int genomePosition, GroupsModelPlugin groups,
    String groupName) throws SBOLValidationException {
    // make valid SBML id
    id = createSBMLConformId(id);
    Species meSpecies =
      model.createSpecies(id, name, getCompartmentFromId(model, id));
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    if (formula != null) {
      fbcSpecies.setChemicalFormula(formula);
    }
    MESBOLPlugin meSBOL = new MESBOLPlugin();
    OrientationType ori = null;
    if ((orientation == "+") || (orientation == "inline")
      || (orientation == "INLINE") || (orientation == "Inline")
      || (orientation == "Forward") || (orientation == "FORWARD")) {
      ori = OrientationType.INLINE;
    } else {
      ori = OrientationType.REVERSECOMPLEMENT;
    }
    meSBOL.createSBOLSequenceWithAnnotation(sbol, id, sequence, role, ori);
    MESpeciesPlugin seqME = new MESpeciesPlugin();
    seqME.setSequence(sbol.getDefaultURIprefix() + id);
    seqME.setGenomePos(Integer.toString(genomePosition));
    meSpecies.getAnnotation().appendNonRDFAnnotation(seqME);
    Group group = (Group) groups.getGroup(groupName);
    if (group != null) {
      group.createMemberWithIdRef(id);
    } else {
      group = groups.createGroup(groupName);
      group.setKind(Group.Kind.classification);
      group.createMemberWithIdRef(id);
    }
  }


  // functions for checking if a certain attribute is set
  public boolean isSetSequence() {
    return isSetAttribute(MEConstants.sequence);
  }


  public boolean isSetGenomePos() {
    return isSetAttribute(MEConstants.genomePos);
  }


  public boolean isSetUnprocessed() {
    return isSetAttribute(MEConstants.unprocessed);
  }


  // getter functions for attributes
  public String getSequence() {
    if (isSetSequence()) {
      return getAttribute(MEConstants.sequence);
    }
    return "";
  }


  public String getGenomePos() {
    if (isSetGenomePos()) {
      return getAttribute(MEConstants.genomePos);
    }
    return "";
  }


  public String getUnprocessed() {
    if (isSetUnprocessed()) {
      return getAttribute(MEConstants.unprocessed);
    }
    return "";
  }


  // Setter functions
  public int setSequence(String value) {
    return setAttribute(MEConstants.sequence, value);
  }


  public int setGenomePos(String value) {
    return setAttribute(MEConstants.genomePos, value);
  }


  public int setUnprocessed(String value) {
    return setAttribute(MEConstants.unprocessed, value);
  }
}
