package sbmlme;
import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * @author Marc A. Voigt
 */
public class TranslocationReference extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  public TranslocationReference() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        TranslocationReference.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  public TranslocationReference(TranslocationReference tr) {
    super(tr);
    if (tr.isSetId()) {
      setId(tr.getId());
    }
  }


  /**
   * functions for creating common TranslocationReferences
   * 
   * @param id
   * @param name
   * @param translocation
   * @param multiplier
   * @return
   */
  public TranslocationReference createTranslocationReference(String id,
    String name, String translocation, Double multiplier) {
    TranslocationReference tr = new TranslocationReference();
    tr.setId(createSBMLConformId(id));
    tr.setName(name);
    tr.setTranslocation(translocation);
    tr.setMultiplier(Double.toString(multiplier));
    return tr;
  }


  /**
   * @param translocation
   * @return
   */
  public static TranslocationReference createTranslocationReference(
    String translocation) {
    TranslocationReference tr = new TranslocationReference();
    tr.setTranslocation(translocation);
    return tr;
  }


  /**
   * @param translocation
   * @param multiplier
   * @return
   */
  public XMLNode createTranslocationReference(String translocation,
    Double multiplier) {
    TranslocationReference tr = new TranslocationReference();
    tr.setTranslocation(translocation);
    tr.setMultiplier(Double.toString(multiplier));
    return tr;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetTranslocation() {
    return isSetAttribute(MEConstants.translocation);
  }


  public boolean isSetMultiplier() {
    return isSetAttribute(MEConstants.multiplier);
  }


  // getter functions for attributes
  public String getTranslocation() {
    if (isSetTranslocation()) {
      return getAttribute(MEConstants.translocation);
    }
    return "";
  }


  public String getMultiplier() {
    if (isSetMultiplier()) {
      return getAttribute(MEConstants.multiplier);
    }
    return "";
  }


  // Setter functions
  public int setTranslocation(String translocation) {
    return setAttribute(MEConstants.translocation, translocation);
  }


  public int setMultiplier(String multiplier) {
    return setAttribute(MEConstants.multiplier, multiplier);
  }


  // ListOf functionality
  public XMLNode ListOfTranslocationReference() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfTranslocationReferences", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfTranslocationReference(TranslocationReference tr) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfTranslocationReferences", ns, prefix),
        new XMLAttributes());
    listOf.addChild(tr);
    return listOf;
  }
}
