package sbmlme;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods for adding references to {@link SubreactionData} to the
 * annotation of reactions and {@link StoichiometricData} objects.
 * 
 * @author Marc A. Voigt
 */
public class SubreactionReference extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  /**
   * 
   */
  public SubreactionReference() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        SubreactionReference.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  /**
   * @param sr
   */
  public SubreactionReference(SubreactionReference sr) {
    super(sr);
    if (sr.isSetId()) {
      setId(sr.getId());
    }
  }


  /**
   * Create an {@link XMLNode} of the new SubreactionReference for the given
   * subreaction and number of usages.
   * 
   * @param subreaction
   *        the id of the subreaction
   * @param numUsage
   *        the number of usages in the reaction
   * @return the XMLNode of the finished SubreactionReference
   */
  public XMLNode createSubreactionReference(String subreaction,
    double numUsage) {
    SubreactionReference temp = new SubreactionReference();
    temp.setSubreaction(createSBMLConformId(subreaction));
    temp.setStoichiometry(String.valueOf(numUsage));
    return temp;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "subreaction" is set.
   * 
   * @return whether the attribute "subreaction" is set
   */
  public boolean isSetSubreaction() {
    return isSetAttribute(MEConstants.subreaction);
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
   * Returns the value of the attribute "subreaction".
   * 
   * @return the value of the attribute "subreaction"
   */
  public String getSubreation() {
    if (isSetSubreaction()) {
      return getAttribute(MEConstants.subreaction);
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
   * Sets the value of the attribute "subreaction".
   * <p>
   * The given id must refer to a member of the "ListOfSubreactionData" in the
   * {@link MEProcessData} annotation.
   * </p>
   * 
   * @param subreaction
   *        the id of the subreaction
   * @return
   */
  public int setSubreaction(String subreaction) {
    return setAttribute(MEConstants.subreaction, subreaction);
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
   * Create an empty list of SubreactionReferences.
   * 
   * @return the {@link XMLNode} with the list of SubreactionReferences
   */
  public XMLNode ListOfSubreactionReference() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfSubreactionReferences", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of SubreactionReferences with a single child.
   * 
   * @param sr
   *        the child SubreactionReference
   * @return the {@link XMLNode} with the list of SubreactionReferences
   */
  public XMLNode ListOfSubreactionReference(SubreactionReference sr) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfSubreactionReferences", ns, prefix),
        new XMLAttributes());
    listOf.addChild(sr);
    return listOf;
  }
}
