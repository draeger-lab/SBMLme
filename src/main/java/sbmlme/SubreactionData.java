package sbmlme;

import java.util.LinkedHashMap;
import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods and attributes for creating SubreactionData for the list
 * of SubreactionData and list of TranslocationData in the
 * {@link MEProcessData}.
 * 
 * @author Marc A. Voigt
 */
public class SubreactionData extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public SubreactionData() {
    super(new XMLTriple(
      StringTools.firstLetterLowerCase(SubreactionData.class.getSimpleName()),
      ns, prefix), new XMLAttributes());
  }


  public SubreactionData(SubreactionData sd) {
    super(sd);
    if (sd.isSetId()) {
      setId(sd.getId());
    }
  }


  /**
   * Create an {@link XMLNode} of the new SubreactionData object for the list of
   * SubreactionData.
   * 
   * @param id
   *        the id of the subreaction
   * @param keff
   *        the effective turnover rate of the enzymes in the subreaction
   * @param enzymeReferences
   *        list with ids of enzymes that catalyze the subreaction, may be null
   * @param elementContributions
   *        map containing net element contributions to a macromolecule, may be
   *        null
   * @param speciesReferences
   *        list with the ids of all species in the reaction
   * @param stoichiometries
   *        list with all species stoichiometries in the reaction, must have
   *        the same order as the list with species ids
   * @return the XMLNode containing the SubreactionData
   */
  public XMLNode createSubreactionData(String id, double keff,
    List<String> enzymeReferences,
    LinkedHashMap<String, Integer> elementContributions,
    List<String> speciesReferences, List<Double> stoichiometries) {
    SubreactionData subreactionData = new SubreactionData();
    subreactionData.setId(createSBMLConformId(id));
    subreactionData.setKeff(String.valueOf(keff));
    if (enzymeReferences != null) {
      EnzymeInformation listEnzymeReferences = new EnzymeInformation();
      subreactionData.addChild(
        listEnzymeReferences.createEnzymeInformationList(enzymeReferences));
    }
    if (elementContributions != null) {
      ElementContribution listElementContributions = new ElementContribution();
      subreactionData.addChild(
        listElementContributions.createListOfElementContributions(
          elementContributions));
    }
    MESpeciesReference meSpecies = new MESpeciesReference();
    XMLNode listReactants = meSpecies.ListOfMEReactants();
    XMLNode listProducts = meSpecies.ListOfMEProducts();
    for (int i = 0; i < stoichiometries.size(); i++) {
      if (stoichiometries.get(i) < 0) {
        // turn list more SBML like
        listReactants.addChild(meSpecies.createMESpeciesReference(
          speciesReferences.get(i), stoichiometries.get(i) * -1));
      } else {
        listProducts.addChild(meSpecies.createMESpeciesReference(
          speciesReferences.get(i), stoichiometries.get(i)));
      }
    }
    subreactionData.addChild(listReactants);
    subreactionData.addChild(listProducts);
    return subreactionData;
  }


  /**
   * Create an {@link XMLNode} of the new SubreactionData object for the list of
   * TranslocationData.
   * 
   * @param id
   *        the id of the subreaction
   * @param keff
   *        the effective turnover rate of the enzymes in the translocation
   * @param enzymeReferences
   *        list with ids of enzymes that catalyze the subreaction, may be null
   * @param fixedKeff
   *        list with information whether an enzyme coupling has a fixed keff in
   *        the translocation process
   * @param listLengthDependent
   *        list with information whether the enzyme coupling is length
   *        dependent in the translocation process
   * @param speciesReferences
   *        list with ids of species used in the translocation process
   * @param stoichiometries
   *        list with stoichiometries of species in the translocation process
   * @param lengthDependentEnergy
   *        whether the ATP cost of the translocation is dependent on the length
   *        of the protein
   * @return the XMLNode containing the TranslocationData
   */
  public XMLNode createTranslocationData(String id, double keff,
    List<String> enzymeReferences, List<Boolean> fixedKeff,
    List<Boolean> listLengthDependent, List<String> speciesReferences,
    List<Double> stoichiometries, boolean lengthDependentEnergy) {
    SubreactionData subreactionData = new SubreactionData();
    subreactionData.setId(createSBMLConformId(id));
    subreactionData.setKeff(String.valueOf(keff));
    subreactionData.setLengthDependent(String.valueOf(lengthDependentEnergy));
    EnzymeInformation listEnzymeReferences = new EnzymeInformation();
    subreactionData.addChild(listEnzymeReferences.createEnzymeInformationList(
      enzymeReferences, fixedKeff, listLengthDependent));
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
    subreactionData.addChild(listReactants);
    subreactionData.addChild(listProducts);
    return subreactionData;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "keff" is set.
   * 
   * @return whether the attribute "keff" is set
   */
  public boolean isSetKeff() {
    return isSetAttribute(MEConstants.keff);
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
   * Returns the value of the attribute "keff".
   * 
   * @return the value of the attribute "keff"
   */
  public String getKeff() {
    if (isSetKeff()) {
      return getAttribute(MEConstants.keff);
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
   * Sets the value of the attribute "keff".
   * 
   * @param keff
   *        the turnover rate
   * @return
   */
  public int setKeff(String keff) {
    return setAttribute(MEConstants.keff, keff);
  }


  /**
   * Sets the value of the attribute "lengthDependentEnergy".
   * <p>
   * Should only be used for SubreactionData objects that will be added to the
   * list of TranslocationData.
   * 
   * @param lengthDependent
   *        whether the ATP cost of the translocation is dependent on the length
   *        of the protein
   * @return
   */
  public int setLengthDependent(String lengthDependent) {
    return setAttribute(MEConstants.lengthDependent, lengthDependent);
  }
}
