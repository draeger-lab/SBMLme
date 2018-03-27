package sbmlme;

import java.util.LinkedHashMap;
import java.util.Map;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements method and attributes for creating lists of element contributions
 * for a {@link SubreactionData} object that is a member of the list of
 * SubreactionData in the {@link MEProcessData}.
 * 
 * @author Marc A. Voigt
 */
public class ElementContribution extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public ElementContribution() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        ElementContribution.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  public ElementContribution(ElementContribution ec) {
    super(ec);
    if (ec.isSetId()) {
      setId(ec.getId());
    }
  }


  /**
   * Create a list of ElementContribution for a {@link SubreactionData} object.
   * 
   * @param elementContributions
   *        map containing net element contributions
   * @return the XMLNode containing the list of ElementContribution
   */
  public XMLNode createListOfElementContributions(
    LinkedHashMap<String, Integer> elementContributions) {
    XMLNode list = ListOfElementContributions();
    for (Map.Entry<String, Integer> entry : elementContributions.entrySet()) {
      ElementContribution tempIter = new ElementContribution();
      tempIter.setElement(entry.getKey());
      tempIter.setValue(String.valueOf(entry.getValue()));
      list.addChild(tempIter);
    }
    return list;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "element" is set.
   * 
   * @return whether the attribute "element" is set
   */
  public boolean isSetElement() {
    return isSetAttribute(MEConstants.element);
  }


  /**
   * Returns whether the attribute "value" is set.
   * 
   * @return whether the attribute "value" is set
   */
  public boolean isSetValue() {
    return isSetAttribute(MEConstants.value);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "element".
   * 
   * @return the value of the attribute "element"
   */
  public String getElement() {
    if (isSetElement()) {
      return getAttribute(MEConstants.element);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "value".
   * 
   * @return the value of the attribute "value"
   */
  public String getValue() {
    if (isSetValue()) {
      return getAttribute(MEConstants.value);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "element".
   * 
   * @param element
   *        the element to be set.
   * @return
   */
  public int setElement(String element) {
    return setAttribute(MEConstants.element, element);
  }


  /**
   * Sets the value of the attribute "value".
   * 
   * @param value
   *        the net contribution of the element
   * @return
   */
  public int setValue(String value) {
    return setAttribute(MEConstants.value, value);
  }


  // ListOf functionality
  /**
   * Create an empty list of ElementContributions.
   * 
   * @return the {@link XMLNode} with the list of ElementContributions
   */
  public static XMLNode ListOfElementContributions() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfElementContributions", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of ElementContributions with a single child.
   * 
   * @param ec
   *        the child ElementContribution
   * @return the {@link XMLNode} with the list of ElementContributions
   */
  public static XMLNode ListOfElementContributions(ElementContribution ec) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfElementContributions", ns, prefix),
        new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
