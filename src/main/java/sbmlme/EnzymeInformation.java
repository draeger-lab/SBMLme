package sbmlme;

import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements method and attributes for creating and adding EnzymeInformation to
 * {@link SubreactionData}.
 * 
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
   * Create a list of EnzymeInformation with a single child for a
   * {@link SubreactionData} object in the list of SubreactionData.
   * 
   * @param enzymeReference
   *        the id of the enzyme
   * @return the XMLNode that contains the list of enzyme information
   */
  public XMLNode createEnzymeInformation(String enzymeReference) {
    XMLNode list = ListOfEnzymeInformations();
    EnzymeInformation reference = new EnzymeInformation();
    reference.setEnzymeRef(createSBMLConformId(enzymeReference));
    list.addChild(reference);
    return list;
  }


  /**
   * Create a List of EnzymeInformation with a single child for a
   * {@link SubreactionData} object in the list of TranslocationData.
   * 
   * @param enzymeReference
   *        the id of the enzyme
   * @param fixedKeff
   *        whether the enzyme has a fixed turnover rate
   * @param lengthDependent
   *        whether the energy cost of the coupling is dependent on the protein
   *        length
   * @return the XMLNode that contains the list of enzyme information
   */
  public XMLNode createEnzymeInformation(String enzymeReference,
    boolean fixedKeff, boolean lengthDependent) {
    XMLNode list = ListOfEnzymeInformations();
    EnzymeInformation reference = new EnzymeInformation();
    reference.setEnzymeRef(createSBMLConformId(enzymeReference));
    reference.setFixedKeff(String.valueOf(fixedKeff));
    reference.setLengthDependent(String.valueOf(lengthDependent));
    list.addChild(reference);
    return list;
  }


  /**
   * Create a List of EnzymeInformation from a list of enzymes for a
   * {@link SubreactionData} object in the list of SubreactionData.
   * 
   * @param enzymeReferences
   *        list with ids of enzymes
   * @return the XMLNode that contains the list of enzyme information
   */
  public XMLNode createEnzymeInformationList(List<String> enzymeReferences) {
    XMLNode list = ListOfEnzymeInformations();
    for (int i = 0; i < enzymeReferences.size(); i++) {
      EnzymeInformation reference = new EnzymeInformation();
      reference.setEnzymeRef(createSBMLConformId(enzymeReferences.get(i)));
      list.addChild(reference);
    }
    return list;
  }


  /**
   * Create a List of EnzymeInformation from a list of enzymes for a
   * {@link SubreactionData} object in the list of TranslocationData.
   * 
   * @param enzymeReferences
   *        list with ids of enzymes
   * @param fixedKeff
   *        list containing information whether the enzyme has a fixed turnover
   *        rate
   * @param lengthDependent
   *        list containing information whether the energy cost of the coupling
   *        is dependent on the protein length
   * @return the XMLNode that contains the list of enzyme information
   */
  public XMLNode createEnzymeInformationList(List<String> enzymeReferences,
    List<Boolean> fixedKeff, List<Boolean> lengthDependent) {
    XMLNode list = ListOfEnzymeInformations();
    for (int i = 0; i < enzymeReferences.size(); i++) {
      EnzymeInformation reference = new EnzymeInformation();
      reference.setEnzymeRef(createSBMLConformId(enzymeReferences.get(i)));
      reference.setFixedKeff(String.valueOf(fixedKeff.get(i)));
      reference.setLengthDependent(String.valueOf(lengthDependent.get(i)));
      list.addChild(reference);
    }
    return list;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "enzymeReference" is set.
   * 
   * @return whether the attribute "enzymeReference" is set
   */
  public boolean isSetEnzymeRef() {
    return isSetAttribute(MEConstants.enzymeRef);
  }


  /**
   * Returns whether the attribute "fixedKeff" is set.
   * 
   * @return whether the attribute "fixedKeff" is set
   */
  public boolean isSetFixedKeff() {
    return isSetAttribute(MEConstants.fixedkeff);
  }


  /**
   * Returns whether the attribute "lengthDependentEnergy" is set.
   * 
   * @return whether the attribute "lengthDependentEnergy" is set
   */
  public boolean isSetLengthDependent() {
    return isSetAttribute(MEConstants.lengthDependent);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "enzymeReference".
   * 
   * @return the value of the attribute "enzymeReference"
   */
  public String getEnzymeRef() {
    if (isSetEnzymeRef()) {
      return getAttribute(MEConstants.enzymeRef);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "fixedKeff".
   * 
   * @return the value of the attribute "fixedKeff"
   */
  public String getFixedKeff() {
    if (isSetFixedKeff()) {
      return getAttribute(MEConstants.fixedkeff);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "lengthDependentEnergy".
   * 
   * @return the value of the attribute "lengthDependentEnergy"
   */
  public String getLengthDependent() {
    if (isSetLengthDependent()) {
      return getAttribute(MEConstants.lengthDependent);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "enzymeReference".
   * 
   * @param enzymeRef
   *        the id of the enzyme
   * @return
   */
  public int setEnzymeRef(String enzymeRef) {
    return setAttribute(MEConstants.enzymeRef, enzymeRef);
  }


  /**
   * Sets the value of the attribute "fixedKeff".
   * 
   * @param fixedkeff
   *        whether the turnover rate of the enzymeis fixed
   * @return
   */
  public int setFixedKeff(String fixedkeff) {
    return setAttribute(MEConstants.fixedkeff, fixedkeff);
  }


  /**
   * Sets the value of the attribute "lengthDependentEnergy".
   * 
   * @param lengthDependent
   *        whether the energy cost of the coupling is dependent on the protein
   *        length
   * @return
   */
  public int setLengthDependent(String lengthDependent) {
    return setAttribute(MEConstants.lengthDependent, lengthDependent);
  }


  // ListOf functionality
  /**
   * Create an empty list of EnzymeInformation.
   * 
   * @return the {@link XMLNode} with the list of enzyme information
   */
  public static XMLNode ListOfEnzymeInformations() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEnzymeInformation", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of enzyme information with a single child.
   * 
   * @param ec
   *        the child EnzymeInformation
   * @return the {@link XMLNode} with the list of enzyme information
   */
  public static XMLNode ListOfEnzymeInformations(EnzymeInformation ec) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEnzymeInformation", ns, prefix),
        new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
