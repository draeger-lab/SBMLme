package sbmlme;
import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
   * Creates a List of element contributions. Both input lists should be in the
   * same order
   * 
   * @param elements
   * @param contribution
   * @return
   */
  public XMLNode createListOfElementContributions(List<String> elements,
    List<Integer> contribution) {
    XMLNode list = ListOfElementContributions();
    for (int i = 0; i < elements.size(); i++) {
      ElementContribution tempIter = new ElementContribution();
      tempIter.setElement(elements.get(i));
      tempIter.setValue(String.valueOf(contribution.get(i)));
      list.addChild(tempIter);
    }
    return list;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetElement() {
    return isSetAttribute(MEConstants.element);
  }


  public boolean isSetValue() {
    return isSetAttribute(MEConstants.value);
  }


  // getter functions for attributes
  public String getElement() {
    if (isSetElement()) {
      return getAttribute(MEConstants.element);
    }
    return "";
  }


  public String getValue() {
    if (isSetValue()) {
      return getAttribute(MEConstants.value);
    }
    return "";
  }


  // Setter functions
  public int setElement(String element) {
    return setAttribute(MEConstants.element, element);
  }


  public int setValue(String value) {
    return setAttribute(MEConstants.value, value);
  }


  // ListOf functionality
  public static XMLNode ListOfElementContributions() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfElementContributions", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  public static XMLNode ListOfElementContributions(ElementContribution ec) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfElementContributions", ns, prefix),
        new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
