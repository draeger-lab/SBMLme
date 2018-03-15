package sbmlme;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods for adding references to {@link SubreactionData} of type
 * TranslocationData to the annotation of reactions.
 * 
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
   * Create an {@link XMLNode} of the new TranslocationReference for the given
   * translocation and multiplier.
   * <p>
   * COBRAme possesses two sets of TranslocationReferences one for
   * translocations with multipliers and one for translocations without
   * multipliers. In SBMLme the two lists are combined into one with all
   * translocations without multipliers are given a multiplier of 0.
   * </p>
   * 
   * @param translocation
   *        the id of the translocation
   * @param multiplier
   *        the number of usages
   * @return the XMLNode of the finished TranslocationReference
   */
  public XMLNode createTranslocationReference(String translocation,
    Double multiplier) {
    TranslocationReference tr = new TranslocationReference();
    tr.setTranslocation(createSBMLConformId(translocation));
    tr.setMultiplier(Double.toString(multiplier));
    return tr;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "translocation" is set.
   * 
   * @return whether the attribute "translocation" is set
   */
  public boolean isSetTranslocation() {
    return isSetAttribute(MEConstants.translocation);
  }


  /**
   * Returns whether the attribute "multiplier" is set.
   * 
   * @return whether the attribute "multiplier" is set
   */
  public boolean isSetMultiplier() {
    return isSetAttribute(MEConstants.multiplier);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "translocation".
   * 
   * @return the value of the attribute "translocation"
   */
  public String getTranslocation() {
    if (isSetTranslocation()) {
      return getAttribute(MEConstants.translocation);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "multiplier".
   * 
   * @return the value of the attribute "multiplier"
   */
  public String getMultiplier() {
    if (isSetMultiplier()) {
      return getAttribute(MEConstants.multiplier);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "translocation".
   * <p>
   * The given id must refer to a member of the "ListOfTranslocationData" in the
   * {@link MEProcessData} annotation.
   * </p>
   * 
   * @param translocation
   *        the id of the translocation
   * @return
   */
  public int setTranslocation(String translocation) {
    return setAttribute(MEConstants.translocation, translocation);
  }


  /**
   * Sets the value of the attribute "multiplier".
   * 
   * @param multiplier
   *        the number of usages
   * @return
   */
  public int setMultiplier(String multiplier) {
    return setAttribute(MEConstants.multiplier, multiplier);
  }


  // ListOf functionality
  /**
   * Create an empty list of TranslocationReferences.
   * 
   * @return the {@link XMLNode} with the list of TranslocationReferences
   */
  public XMLNode ListOfTranslocationReference() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfTranslocationReferences", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of TranslocationReferences with a single child.
   * 
   * @param tr
   *        the child TranslocationReference
   * @return the {@link XMLNode} with the list of TranslocationReferences
   */
  public XMLNode ListOfTranslocationReference(TranslocationReference tr) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfTranslocationReferences", ns, prefix),
        new XMLAttributes());
    listOf.addChild(tr);
    return listOf;
  }
}
