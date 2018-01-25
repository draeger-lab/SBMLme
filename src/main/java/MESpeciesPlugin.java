import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCSpeciesPlugin;
import org.sbml.jsbml.ext.groups.Group;
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
   * create COBRAme species
   * 
   * @param model
   * @param id
   * @param formula
   * @param name
   * @param c
   * @param group
   */
  public void createMESpecies(Model model, String id, String formula,
    String name, Compartment c, Group group) {
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    fbcSpecies.setChemicalFormula(formula);
    group.createMemberWithIdRef(id);
  }


  /**
   * create COBRAme processed species
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
    String name, Compartment c, String unprocessedId, Group group) {
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    fbcSpecies.setChemicalFormula(formula);
    MESpeciesPlugin seqME = new MESpeciesPlugin();
    seqME.setUnprocessed(unprocessedId);
    group.createMemberWithIdRef(id);
  }


  /**
   * create COBRAme Sequence species
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
    String role, String orientation, int genomePosition, Group group)
    throws SBOLValidationException {
    Species meSpecies = model.createSpecies(id, name, c);
    meSpecies.initDefaults(2, 4, true);
    FBCSpeciesPlugin fbcSpecies =
      (FBCSpeciesPlugin) meSpecies.getPlugin(FBCConstants.shortLabel);
    fbcSpecies.setChemicalFormula(formula);
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
    group.createMemberWithIdRef(id);
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
