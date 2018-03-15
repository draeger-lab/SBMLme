package sbmlme;

import java.util.List;
import java.util.Map;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods and attributes for encoding stoichiometric information of
 * metabolic reactions (specifically for reactions of type "MetabolicReaction").
 * 
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
   * Create an {@link XMLNode} of the new StoichiometricData object.
   * <p>
   * In COBRAme reversible metabolic reactions are represented as two
   * irreversible reactions proceeding in opposite directions. A
   * StoichiometricData object may therefore belong to up to two irreversible
   * metabolic reactions and is included in the model annotation instead of the
   * reactions annotation to reduce unnecessary redundancy.
   * </p>
   * 
   * @param id
   *        the id of the StoichiometricData object
   * @param lowerFluxBound
   *        the lower flux bound of the metabolic reaction
   * @param upperFluxBound
   *        the upper flux bound of the metabolic reaction
   * @param subreactionMap
   *        a map containing information of all subreactions used in the
   *        metabolic reaction, may be empty
   * @param speciesReferences
   *        list with ids of species used in the metabolic reaction
   * @param stoichiometries
   *        list with stoichiometries of species in the metabolic reaction
   * @return the XMLNode of the created StoichiometricData object
   */
  public XMLNode createStoichiometricData(String id, Double lowerFluxBound,
    Double upperFluxBound, Map<String, Double> subreactionMap,
    List<String> speciesReferences, List<Double> stoichiometries) {
    StoichiometricData stoichiometricData = new StoichiometricData();
    stoichiometricData.setId(createSBMLConformId(id));
    stoichiometricData.setLowerFluxBound(String.valueOf(lowerFluxBound));
    stoichiometricData.setUpperFluxBound(String.valueOf(upperFluxBound));
    // create and add list of subreactions objects
    if (subreactionMap != null) {
      SubreactionReference subReference = new SubreactionReference();
      XMLNode listSubRef = subReference.ListOfSubreactionReference();
      for (Map.Entry<String, Double> entry : subreactionMap.entrySet()) {
        listSubRef.addChild(subReference.createSubreactionReference(
          entry.getKey(), entry.getValue().intValue()));
      }
      stoichiometricData.addChild(listSubRef);
    }
    // create and add lists for reactants and products
    MESpeciesReference meSpecies = new MESpeciesReference();
    XMLNode listReactants = meSpecies.ListOfMEReactants();
    XMLNode listProducts = meSpecies.ListOfMEProducts();
    for (int i = 0; i < stoichiometries.size(); i++) {
      if (stoichiometries.get(i) < 0) {
        listReactants.addChild(meSpecies.createMESpeciesReference(
          speciesReferences.get(i), stoichiometries.get(i) * -1));
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
  /**
   * Returns whether the attribute "lowerFluxBound" is set.
   * 
   * @return whether the attribute "lowerFluxBound" is set
   */
  public boolean isSetLowerFluxBound() {
    return isSetAttribute(MEConstants.lowerFluxBound);
  }


  /**
   * Returns whether the attribute "upprFluxBound" is set.
   * 
   * @return whether the attribute "upperFluxBound" is set
   */
  public boolean isSetUpperFluxBound() {
    return isSetAttribute(MEConstants.upperFluxBound);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "lowerFluxBound".
   * 
   * @return the value of the attribute "lowerFluxBound"
   */
  public String getLowerFluxBound() {
    if (isSetLowerFluxBound()) {
      return getAttribute(MEConstants.lowerFluxBound);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "upperFluxBound".
   * 
   * @return the value of the attribute "upperFluxBound"
   */
  public String getUpperFluxBound() {
    if (isSetUpperFluxBound()) {
      return getAttribute(MEConstants.upperFluxBound);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "lowerFluxBound".
   * 
   * @param value
   *        the lower flux bound
   * @return
   */
  public int setLowerFluxBound(String value) {
    return setAttribute(MEConstants.lowerFluxBound, value);
  }


  /**
   * Sets the value of the attribute "upperFluxBound".
   * 
   * @param value
   *        the upper flux bound
   * @return
   */
  public int setUpperFluxBound(String value) {
    return setAttribute(MEConstants.upperFluxBound, value);
  }
}
