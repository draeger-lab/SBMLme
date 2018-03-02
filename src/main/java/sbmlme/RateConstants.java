package sbmlme;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
   * @param temperature
   * @param rate
   * @return
   */
  public XMLNode createRateConstant(String temperature, double rate) {
    RateConstants temp = new RateConstants();
    temp.setTemperature(temperature);
    temp.setRate(String.valueOf(rate));
    return temp;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetTemperature() {
    return isSetAttribute(MEConstants.temperature);
  }


  public boolean isSetRate() {
    return isSetAttribute(MEConstants.rate);
  }


  // getter functions for attributes
  public String getTemperature() {
    if (isSetTemperature()) {
      return getAttribute(MEConstants.temperature);
    }
    return "";
  }


  public String getRate() {
    if (isSetRate()) {
      return getAttribute(MEConstants.rate);
    }
    return "";
  }


  // Setter functions
  public int setTemperature(String temperature) {
    return setAttribute(MEConstants.temperature, temperature);
  }


  public int setRate(String rate) {
    return setAttribute(MEConstants.rate, rate);
  }


  // ListOf functionality
  public XMLNode ListOfRateConstants() {
    XMLNode listOf = new XMLNode(
      new XMLTriple("listOfRateConstants", ns, prefix), new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfRateConstants(RateConstants rc) {
    XMLNode listOf = new XMLNode(
      new XMLTriple("listOfRateConstants", ns, prefix), new XMLAttributes());
    listOf.addChild(rc);
    return listOf;
  }


  public XMLNode ListOfEquilibriumConstants() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEquilibriumConstants", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfEquilibriumConstants(RateConstants rc) {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfEquilibriumConstants", ns, prefix),
        new XMLAttributes());
    listOf.addChild(rc);
    return listOf;
  }
}
