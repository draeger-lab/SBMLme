package sbmlme;
import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * @author Marc A. Voigt
 */
public class Subreaction extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public Subreaction() {
    super(new XMLTriple(
      StringTools.firstLetterLowerCase(Subreaction.class.getSimpleName()), ns,
      prefix), new XMLAttributes());
  }


  public Subreaction(Subreaction sd) {
    super(sd);
    if (sd.isSetId()) {
      setId(sd.getId());
    }
  }


  /**
   * create a List of Subreactions for a StoichiometricData object
   * 
   * @param listSubreactionReferences
   * @param listCoefficients
   * @return
   */
  public XMLNode createListOfSubreactions(
    List<String> listSubreactionReferences, List<Double> listCoefficients) {
    XMLNode list = ListOfSubreactions();
    for (int i = 0; i < listSubreactionReferences.size(); i++) {
      Subreaction temp = new Subreaction();
      temp.setSubreaction(
        createSBMLConformId(listSubreactionReferences.get(i)));
      temp.setCoefficient(String.valueOf(listCoefficients.get(i)));
      list.addChild(temp);
    }
    return list;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetSubreaction() {
    return isSetAttribute(MEConstants.subreaction);
  }


  public boolean isSetCoefficient() {
    return isSetAttribute(MEConstants.coefficient);
  }


  // getter functions for attributes
  public String getSubreaction() {
    if (isSetSubreaction()) {
      return getAttribute(MEConstants.subreaction);
    }
    return "";
  }


  public String getCoefficient() {
    if (isSetCoefficient()) {
      return getAttribute(MEConstants.coefficient);
    }
    return "";
  }


  // Setter functions
  public int setSubreaction(String subreaction) {
    return setAttribute(MEConstants.subreaction, subreaction);
  }


  public int setCoefficient(String coefficient) {
    return setAttribute(MEConstants.coefficient, coefficient);
  }


  // ListOf functionality
  public static XMLNode ListOfSubreactions() {
    XMLNode listOf = new XMLNode(
      new XMLTriple("ListOfSubreactions", ns, prefix), new XMLAttributes());
    return listOf;
  }


  public static XMLNode ListOfSubreactions(Subreaction sd) {
    XMLNode listOf = new XMLNode(
      new XMLTriple("ListOfSubreactions", ns, prefix), new XMLAttributes());
    listOf.addChild(sd);
    return listOf;
  }
}
