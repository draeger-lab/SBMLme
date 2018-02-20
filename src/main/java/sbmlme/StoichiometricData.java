package sbmlme;
import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * @author Marc A. Voigt
 */
public class StoichiometricData extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public StoichiometricData() {
    super(
      new XMLTriple(StringTools.firstLetterLowerCase(
        StoichiometricData.class.getSimpleName()), ns, prefix),
      new XMLAttributes());
  }


  public StoichiometricData(StoichiometricData sd) {
    super(sd);
    if (sd.isSetId()) {
      setId(sd.getId());
    }
  }


  /**
   * create a stoichiometricData object
   * 
   * @param id
   * @param lowerFluxBound
   * @param upperFluxBound
   * @param listSubreactionReferences
   * @param listCoefficients
   * @param speciesReferences
   * @param stoichiometries
   * @return
   */
  public XMLNode createStoichiometricData(String id, Double lowerFluxBound,
    Double upperFluxBound, List<String> listSubreactionReferences,
    List<Double> listCoefficients, List<String> speciesReferences,
    List<Double> stoichiometries) {
    StoichiometricData stoichiometricData = new StoichiometricData();
    stoichiometricData.setId(createSBMLConformId(id));
    stoichiometricData.setLowerFluxBound(String.valueOf(lowerFluxBound));
    stoichiometricData.setUpperFluxBound(String.valueOf(upperFluxBound));
    // create and add list of subreactions objects
    if (listSubreactionReferences != null) {
      Subreaction subreaction = new Subreaction();
      XMLNode listOfSubreactions = subreaction.createListOfSubreactions(
        listSubreactionReferences, listCoefficients);
      stoichiometricData.addChild(listOfSubreactions);
    }
    // create and add lists for reactants and products
    MESpeciesReference meSpecies = new MESpeciesReference();
    XMLNode listReactants = meSpecies.ListOfMEReactants();
    XMLNode listProducts = meSpecies.ListOfMEProducts();
    for (int i = 0; i < stoichiometries.size(); i++) {
      if (stoichiometries.get(i) < 0) {
        listReactants.addChild(meSpecies.createMESpeciesReference(
          speciesReferences.get(i), stoichiometries.get(i)));
      } else {
        listProducts.addChild(meSpecies.createMESpeciesReference(
          speciesReferences.get(i), stoichiometries.get(i)));
      }
    }
    stoichiometricData.addChild(listReactants);
    stoichiometricData.addChild(listProducts);
    return stoichiometricData;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetLowerFluxBound() {
    return isSetAttribute(MEConstants.lowerFluxBound);
  }


  public boolean isSetUpperFluxBound() {
    return isSetAttribute(MEConstants.upperFluxBound);
  }


  // getter functions for attributes
  public String getLowerFluxBound() {
    if (isSetLowerFluxBound()) {
      return getAttribute(MEConstants.lowerFluxBound);
    }
    return "";
  }


  public String getUpperFluxBound() {
    if (isSetUpperFluxBound()) {
      return getAttribute(MEConstants.upperFluxBound);
    }
    return "";
  }


  // Setter functions
  public int setLowerFluxBound(String value) {
    return setAttribute(MEConstants.lowerFluxBound, value);
  }


  public int setUpperFluxBound(String value) {
    return setAttribute(MEConstants.upperFluxBound, value);
  }
}
