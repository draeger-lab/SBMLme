package sbmlme;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods and attributes for adding the stoichiometry of the
 * subunits of a protein complex to the annotation of a complex formation
 * reaction.
 * 
 * @author Marc A. Voigt
 */
public class StoichiometricReference extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  /**
   * 
   */
  public StoichiometricReference() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        StoichiometricReference.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  /**
   * @param sr
   */
  public StoichiometricReference(StoichiometricReference sr) {
    super(sr);
    if (sr.isSetId()) {
      setId(sr.getId());
    }
  }


  /**
   * Create an {@link XMLNode} of the new StoichiometricReference for the given
   * subunit and stoichiometry.
   * 
   * @param subunit
   *        the id of the subunit
   * @param stoichiometry
   *        the number of subunits in the complex
   * @return the XMLNode of the finished SubreactionReference
   */
  public XMLNode createStoichiometricReference(String subunit,
    double stoichiometry) {
    StoichiometricReference temp = new StoichiometricReference();
    temp.setSubunit(createSBMLConformId(subunit));
    temp.setStoichiometry(String.valueOf(stoichiometry));
    return temp;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "subunit" is set.
   * 
   * @return whether the attribute "subunit" is set
   */
  public boolean isSetSubunit() {
    return isSetAttribute(MEConstants.subunit);
  }


  /**
   * Returns whether the attribute "stoichiometry" is set.
   * 
   * @return whether the attribute "stoichiometry" is set
   */
  public boolean isSetStoichiometry() {
    return isSetAttribute(MEConstants.stoichiometry);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "subunit".
   * 
   * @return the value of the attribute "subunit"
   */
  public String getSubunit() {
    if (isSetSubunit()) {
      return getAttribute(MEConstants.subunit);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "stoichiometry".
   * 
   * @return the value of the attribute "stoichiometry"
   */
  public String getStoichiometry() {
    if (isSetStoichiometry()) {
      return getAttribute(MEConstants.stoichiometry);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "subunit".
   * 
   * @param subunit
   *        the id of the subunit
   * @return
   */
  public int setSubunit(String subunit) {
    return setAttribute(MEConstants.subunit, subunit);
  }


  /**
   * Sets the value of the attribute "stoichiometry".
   * 
   * @param stoichiometry
   *        the number of usages
   * @return
   */
  public int setStoichiometry(String stoichiometry) {
    return setAttribute(MEConstants.stoichiometry, stoichiometry);
  }


  // ListOf functionality
  /**
   * Create an empty list of StoichiometricReferences.
   * 
   * @return the {@link XMLNode} with the list of StoichiometricReferences
   */
  public XMLNode ListOfStoichiometricReference() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfStoichiometricReferences", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of StoichiometricReferences with a single child.
   * 
   * @param sr
   *        the child StoichiometricReference
   * @return the {@link XMLNode} with the list of StoichiometricReferences
   */
  public XMLNode ListOfStoichiometricReference(StoichiometricReference sr) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfStoichiometricReferences", ns, prefix),
        new XMLAttributes());
    listOf.addChild(sr);
    return listOf;
  }
}
