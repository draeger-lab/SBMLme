package sbmlme;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods and attributes for adding rate constants to the annotation
 * of a reaction of type "PostTranslationReaction".
 * 
 * @author Marc A. Voigt
 */
public class RateConstants extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public RateConstants() {
    super(new XMLTriple(
      StringTools.firstLetterLowerCase(RateConstants.class.getSimpleName()), ns,
      prefix), new XMLAttributes());
  }


  public RateConstants(RateConstants rc) {
    super(rc);
    if (rc.isSetId()) {
      setId(rc.getId());
    }
  }


  /**
   * Create an {@link XMLNode} of the new RateConstant for the given temperature
   * and rate.
   * 
   * @param temperature
   *        the temperature for protein folding
   * @param rate
   *        the rate at which the protein is folded
   * @return the XMLNode of the finished RateConstant
   */
  public XMLNode createRateConstant(String temperature, double rate) {
    RateConstants temp = new RateConstants();
    temp.setTemperature(temperature);
    temp.setRate(String.valueOf(rate));
    return temp;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "temperature" is set.
   * 
   * @return whether the attribute "temperature" is set
   */
  public boolean isSetTemperature() {
    return isSetAttribute(MEConstants.temperature);
  }


  /**
   * Returns whether the attribute "rate" is set.
   * 
   * @return whether the attribute "rate" is set
   */
  public boolean isSetRate() {
    return isSetAttribute(MEConstants.rate);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "temperature".
   * 
   * @return the value of the attribute "temperature"
   */
  public String getTemperature() {
    if (isSetTemperature()) {
      return getAttribute(MEConstants.temperature);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "rate".
   * 
   * @return the value of the attribute "rate"
   */
  public String getRate() {
    if (isSetRate()) {
      return getAttribute(MEConstants.rate);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "temperature".
   * 
   * @param temperature
   *        the temperature for protein
   * @return
   */
  public int setTemperature(String temperature) {
    return setAttribute(MEConstants.temperature, temperature);
  }


  /**
   * Sets the value of the attribute "rate".
   * 
   * @param rate
   *        the rate of folding
   * @return
   */
  public int setRate(String rate) {
    return setAttribute(MEConstants.rate, rate);
  }


  // ListOf functionality
  /**
   * Create an empty list of RateConstants.
   * 
   * @return the {@link XMLNode} with the list of RateConstants
   */
  public XMLNode ListOfRateConstants() {
    XMLNode listOf = new XMLNode(
      new XMLTriple("listOfRateConstants", ns, prefix), new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of RateConstants with a single child.
   * 
   * @param rc
   *        the child RateConstant
   * @return the {@link XMLNode} with the list of RateConstants
   */
  public XMLNode ListOfRateConstants(RateConstants rc) {
    XMLNode listOf = new XMLNode(
      new XMLTriple("listOfRateConstants", ns, prefix), new XMLAttributes());
    listOf.addChild(rc);
    return listOf;
  }


  /**
   * Create an empty list of EquilibriumConstants.
   * 
   * @return the {@link XMLNode} with the list of EquilibriumConstants
   */
  public XMLNode ListOfEquilibriumConstants() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEquilibriumConstants", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of EquilibriumConstants with a single child.
   * 
   * @param rc
   *        the child EquilibriumConstants
   * @return the {@link XMLNode} with the list of EquilibriumConstants
   */
  public XMLNode ListOfEquilibriumConstants(RateConstants rc) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEquilibriumConstants", ns, prefix),
        new XMLAttributes());
    listOf.addChild(rc);
    return listOf;
  }
}
