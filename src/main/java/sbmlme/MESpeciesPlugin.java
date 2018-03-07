package sbmlme;

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
 * implements attributes and methods to encode all information of species in a
 * COBRAme model in SBML.
 * 
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
    super(new XMLTriple(speciesPlugin, ns, prefix), new XMLAttributes());
  }


  public MESpeciesPlugin(MESpeciesPlugin sp) {
    super(sp);
    if (sp.isSetId()) {
      setId(sp.getId());
    }
  }


  /**
   * get the Compartment in the SBML model for the given string, create a
   * Compartment if not already present
   * 
   * @param compartment
   *        the id of the desired compartment
   * @param model
   *        the SBML model
   * @return the Compartment with the given id
   */
  public Compartment getSBMLCompartment(String compartment, Model model) {
    Compartment comp = model.getCompartment(compartment);
    if (comp != null) {
      return comp;
    } else {
      comp = model.createCompartment(compartment);
      comp.initDefaults(2, 4, true);
      comp.setConstant(false);
      return comp;
    }
  }


  /**
   * Create and add an SBMLme species to the model that does not require
   * additional attributes.
   * <p>
   * This method should be used for all COBRAme species except for species of
   * type "TranscribedGene" or "ProcessedProtein". These two types of species
   * require additional attributes which cannot be encoded by SBML or one of its
   * packages and should be added to the model by their own methods.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param id
   *        the unique id of the species to be created
   * @param formula
   *        the formula of the species, can be null
   * @param name
   *        the name of the species, can be null
   * @param compartment
   *        the id of the compartment
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param groupName
   *        the id of the group that the species should be a part of. This
   *        should equal to the type of species in the COBRAme model.
   */
  public void createMESpecies(Model model, String id, String formula,
    String name, String compartment, GroupsModelPlugin groups,
    String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    // get SBML compartment
    Compartment c = getSBMLCompartment(compartment, model);
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
   * Create and add an SBMLme species to the model that represents a processed
   * protein.
   * <p>
   * This method should be used for COBRAme species of type "ProcessedProtein".
   * This method adds an additional attribute named "unprocessedProteinId" to
   * the annotation of the species which contains the id of the precursor of the
   * protein.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param id
   *        the unique id of the species to be created
   * @param formula
   *        the formula of the species, can be null
   * @param name
   *        the name of the species, can be null
   * @param compartment
   *        the id of the compartment
   * @param unprocessedId
   *        the id of the unprocessed protein that is the precursor of the
   *        processed species created by this method
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param groupName
   *        the id of the group that the species should be a part of. This
   *        should equal to the type of species in the COBRAme model.
   */
  public void createMEProcessedSpecies(Model model, String id, String formula,
    String name, String compartment, String unprocessedId,
    GroupsModelPlugin groups, String groupName) {
    // make valid SBML id
    id = createSBMLConformId(id);
    // get SBML compartment
    Compartment c = getSBMLCompartment(compartment, model);
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
    meSpecies.getAnnotation().appendNonRDFAnnotation(seqME);
  }


  /**
   * Create and add an SBMLme species to the model that represents a RNA.
   * <p>
   * This method should be used for COBRAme species of type "TranscribedGene".
   * The method adds the nucleotide sequence of the species with information
   * regarding its location to the given SBOL document. The method adds two
   * attributes to the annotation of the species in SBML, "sequence" and
   * "genomePosition". The attribute "sequence" contains the URI of the
   * ComponentDefinition in the SBOL document the nucleotide has been added to.
   * The attribute "genomePosition" contains the starting position of the
   * nucleotide sequence on the genome of the modeled organism. The attribute
   * "genomePosition" is needed since SBOL defines position in relation to a
   * sequence while COBRAme defines position in relation to a genome.
   * </p>
   * 
   * @param model
   *        the SBML model
   * @param id
   *        the unique id of the species to be created
   * @param formula
   *        the formula of the species, can be null
   * @param name
   *        the name of the species, can be null
   * @param compartment
   *        the id of the compartment
   * @param sbol
   *        the SBOL document to which the sequence should be added
   * @param sequence
   *        the nucleotide sequence of the species
   * @param role
   *        the type of RNA the species belongs to
   * @param orientation
   *        the strand on the genome that the sequence is located on
   * @param genomePosition
   *        the position on the genome where the sequence starts
   * @param groups
   *        the GroupsModelPlugin of the SBML model
   * @param groupName
   *        the id of the group that the species should be a part of. This
   *        should equal to the type of species in the COBRAme model.
   * @throws SBOLValidationException
   */
  public void createMESequenceSpecies(Model model, String id, String formula,
    String name, String compartment, SBOLDocument sbol, String sequence,
    String role, String orientation, int genomePosition,
    GroupsModelPlugin groups, String groupName) throws SBOLValidationException {
    // make valid SBML id
    id = createSBMLConformId(id);
    // get SBML compartment
    Compartment c = getSBMLCompartment(compartment, model);
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
   * Returns whether the attribute "genomePosition" is set.
   * 
   * @return whether the attribute "genomePosition" is set.
   */
  public boolean isSetGenomePos() {
    return isSetAttribute(MEConstants.genomePos);
  }


  /**
   * Returns whether the attribute "unprocessedProteinId" is set.
   * 
   * @return whether the attribute "unprocessedProteinId" is set.
   */
  public boolean isSetUnprocessed() {
    return isSetAttribute(MEConstants.unprocessed);
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
   * Returns the value of the attribute "genomePosition".
   * 
   * @return the value of the attribute "genomePosition".
   */
  public String getGenomePos() {
    if (isSetGenomePos()) {
      return getAttribute(MEConstants.genomePos);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "unprocessedProteinId".
   * 
   * @return the value of the attribute "unprocessedProteinId".
   */
  public String getUnprocessed() {
    if (isSetUnprocessed()) {
      return getAttribute(MEConstants.unprocessed);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "sequence".
   * <p>
   * The optional attribute "sequence" takes a string that represents a
   * valid URI for a ComponentDefinition in an SBOL document. This attribute is
   * mandatory when the species is of type "TranscribedGene".
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
   * Sets the value of the attribute "genomePosition".
   * <p>
   * The optional attribute "genomePosition" contains the position on the genome
   * where the nucleotide sequence starts. This attribute is mandatory when the
   * species is of type "TranscribedGene".
   * </p>
   * 
   * @param value
   *        position on the genome where the nucleotide sequence starts
   * @return
   */
  public int setGenomePos(String value) {
    return setAttribute(MEConstants.genomePos, value);
  }


  /**
   * Sets the value of the attribute "unprocessedProteinId".
   * <p>
   * The optional attribute "unprocessedProteinId" contains the id of a species
   * that is the precursor of the processed protein. This attribute is mandatory
   * when the species is of type "ProcessedProtein".
   * </p>
   * 
   * @param value
   *        the id of the precursor of the species
   * @return
   */
  public int setUnprocessed(String value) {
    return setAttribute(MEConstants.unprocessed, value);
  }
}
