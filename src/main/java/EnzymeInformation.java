import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * @author Marc A. Voigt
 */
public class EnzymeInformation extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public EnzymeInformation() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        EnzymeInformation.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  public EnzymeInformation(EnzymeInformation ei) {
    super(ei);
    if (ei.isSetId()) {
      setId(ei.getId());
    }
  }


  /**
   * create a List of EnzymeInformation with a single child for a
   * SubreactionData object
   * 
   * @param enzymeReference
   * @return
   */
  public XMLNode createEnzymeInformation(String enzymeReference) {
    XMLNode list = ListOfEnzymeInformations();
    EnzymeInformation reference = new EnzymeInformation();
    reference.setEnzymeRef(enzymeReference);
    list.addChild(reference);
    return list;
  }


  /**
   * create a List of EnzymeInformation with a single child for a
   * TranslocationData object
   * 
   * @param enzymeReference
   * @param fixedKeff
   * @param lengthDependent
   * @return
   */
  public XMLNode createEnzymeInformation(String enzymeReference,
    boolean fixedKeff, boolean lengthDependent) {
    XMLNode list = ListOfEnzymeInformations();
    EnzymeInformation reference = new EnzymeInformation();
    reference.setEnzymeRef(enzymeReference);
    reference.setFixedKeff(String.valueOf(fixedKeff));
    reference.setLengthDependent(String.valueOf(lengthDependent));
    list.addChild(reference);
    return list;
  }


  /**
   * create a List of EnzymeInformation from a list of enzymes for a
   * SubreactionData object
   * 
   * @param enzymeReferences
   * @return
   */
  public XMLNode createEnzymeInformationList(List<String> enzymeReferences) {
    XMLNode list = ListOfEnzymeInformations();
    for (int i = 0; i < enzymeReferences.size(); i++) {
      EnzymeInformation reference = new EnzymeInformation();
      reference.setEnzymeRef(enzymeReferences.get(i));
      list.addChild(reference);
    }
    return list;
  }


  /**
   * create a List of EnzymeInformation from a list of enzymes for a
   * TranslocationData object
   * 
   * @param enzymeReferences
   * @param fixedKeff
   * @param lengthDependent
   * @return
   */
  public XMLNode createEnzymeInformationList(List<String> enzymeReferences,
    List<Boolean> fixedKeff, List<Boolean> lengthDependent) {
    XMLNode list = ListOfEnzymeInformations();
    for (int i = 0; i < enzymeReferences.size(); i++) {
      EnzymeInformation reference = new EnzymeInformation();
      reference.setEnzymeRef(enzymeReferences.get(i));
      reference.setFixedKeff(String.valueOf(fixedKeff.get(i)));
      reference.setLengthDependent(String.valueOf(lengthDependent.get(i)));
      list.addChild(reference);
    }
    return list;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetEnzymeRef() {
    return isSetAttribute(MEConstants.enzymeRef);
  }


  public boolean isSetFixedKeff() {
    return isSetAttribute(MEConstants.fixedkeff);
  }


  public boolean isSetLengthDependent() {
    return isSetAttribute(MEConstants.lengthDependent);
  }


  // getter functions for attributes
  public String getEnzymeRef() {
    if (isSetEnzymeRef()) {
      return getAttribute(MEConstants.enzymeRef);
    }
    return "";
  }


  public String getFixedKeff() {
    if (isSetFixedKeff()) {
      return getAttribute(MEConstants.fixedkeff);
    }
    return "";
  }


  public String getLengthDependent() {
    if (isSetLengthDependent()) {
      return getAttribute(MEConstants.lengthDependent);
    }
    return "";
  }


  // Setter functions
  public int setEnzymeRef(String enzymeRef) {
    return setAttribute(MEConstants.enzymeRef, enzymeRef);
  }


  public int setFixedKeff(String fixedkeff) {
    return setAttribute(MEConstants.fixedkeff, fixedkeff);
  }


  public int setLengthDependent(String lengthDependent) {
    return setAttribute(MEConstants.lengthDependent, lengthDependent);
  }


  // ListOf functionality
  public static XMLNode ListOfEnzymeInformations() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfEnzymeInformation", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  public static XMLNode ListOfEnzymeInformations(EnzymeInformation ec) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfEnzymeInformation", ns, prefix),
        new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
