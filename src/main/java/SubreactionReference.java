import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
   * @param subreaction
   * @param numUsage
   * @return
   */
  public XMLNode createSubreactionReference(String subreaction, int numUsage) {
    SubreactionReference temp = new SubreactionReference();
    temp.setSubreaction(createSBMLConformId(subreaction));
    temp.setStoichiometry(String.valueOf(numUsage));
    return temp;
  }


  // functions for checking if a certain attribute is set
  /**
   * @return
   */
  public boolean isSetSubreaction() {
    return isSetAttribute(MEConstants.subreaction);
  }


  /**
   * @return
   */
  public boolean isSetStoichiometry() {
    return isSetAttribute(MEConstants.stoichiometry);
  }


  // getter functions for attributes
  /**
   * @return
   */
  public String getSubreation() {
    if (isSetSubreaction()) {
      return getAttribute(MEConstants.subreaction);
    }
    return "";
  }


  /**
   * @return
   */
  public String getStoichiometry() {
    if (isSetStoichiometry()) {
      return getAttribute(MEConstants.stoichiometry);
    }
    return "";
  }


  // Setter functions
  /**
   * @param subreaction
   * @return
   */
  public int setSubreaction(String subreaction) {
    return setAttribute(MEConstants.subreaction, subreaction);
  }


  /**
   * @param stoichiometry
   * @return
   */
  public int setStoichiometry(String stoichiometry) {
    return setAttribute(MEConstants.stoichiometry, stoichiometry);
  }


  // ListOf functionality
  /**
   * @return
   */
  public XMLNode ListOfSubreactionReference() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfSubreactionReferences", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * @param sr
   * @return
   */
  public XMLNode ListOfSubreactionReference(SubreactionReference sr) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfSubreactionReferences", ns, prefix),
        new XMLAttributes());
    listOf.addChild(sr);
    return listOf;
  }
}
