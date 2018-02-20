package sbmlme;
import java.util.List;

import org.sbml.jsbml.util.StringTools;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
   * create SubreactionData object
   * 
   * @param id
   * @param keff
   * @param enzymeReferences
   * @param elements
   * @param contribution
   * @param speciesReferences
   * @param stoichiometries
   * @return
   */
  public XMLNode createSubreactionData(String id, double keff,
    List<String> enzymeReferences, List<String> elements,
    List<Integer> contribution, List<String> speciesReferences,
    List<Integer> stoichiometries) {
    SubreactionData subreactionData = new SubreactionData();
    subreactionData.setId(createSBMLConformId(id));
    subreactionData.setKeff(String.valueOf(keff));
    if (enzymeReferences != null) {
      EnzymeInformation listEnzymeReferences = new EnzymeInformation();
      subreactionData.addChild(
        listEnzymeReferences.createEnzymeInformationList(enzymeReferences));
    }
    if (elements != null) {
      ElementContribution listElementContributions = new ElementContribution();
      subreactionData.addChild(
        listElementContributions.createListOfElementContributions(elements,
          contribution));
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
   * create SubreactionData object without ElementContributions
   * outdated
   * 
   * @param id
   * @param keff
   * @param enzymeReferences
   * @param speciesReferences
   * @param stoichiometries
   * @return
   */
  public XMLNode createSubreactionData(String id, double keff,
    List<String> enzymeReferences, List<String> speciesReferences,
    List<Integer> stoichiometries) {
    SubreactionData subreactionData = new SubreactionData();
    subreactionData.setId(id);
    subreactionData.setKeff(String.valueOf(keff));
    EnzymeInformation listEnzymeReferences = new EnzymeInformation();
    subreactionData.addChild(
      listEnzymeReferences.createEnzymeInformationList(enzymeReferences));
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
    subreactionData.addChild(listReactants);
    subreactionData.addChild(listProducts);
    return subreactionData;
  }


  /**
   * create a SubreactionData object for the List of TranslocationData
   * 
   * @param id
   * @param keff
   * @param enzymeReferences
   * @param fixedKeff
   * @param listLengthDependent
   * @param speciesReferences
   * @param stoichiometries
   * @param lengthDependentEnergy
   * @return
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
          speciesReferences.get(i), stoichiometries.get(i)));
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
  public boolean isSetKeff() {
    return isSetAttribute(MEConstants.keff);
  }


  public boolean isSetLengthDependent() {
    return isSetAttribute(MEConstants.lengthDependent);
  }


  // getter functions for attributes
  public String getKeff() {
    if (isSetKeff()) {
      return getAttribute(MEConstants.keff);
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
  public int setKeff(String keff) {
    return setAttribute(MEConstants.keff, keff);
  }


  public int setLengthDependent(String lengthDependent) {
    return setAttribute(MEConstants.lengthDependent, lengthDependent);
  }
}
