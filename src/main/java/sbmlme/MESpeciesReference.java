package sbmlme;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods and attributes to create and add lists of reactants and
 * products to {@link SubreactionData} objects and {@link StoichiometricData}
 * objects for the {@link MEProcessData}.
 * 
 * @author Marc A. Voigt
 */
public class MESpeciesReference extends MEAbstractXMLNodePlugin
  implements MEConstants {

  // Species references for stoichiometry in Process Data
  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public MESpeciesReference() {
    super(new XMLTriple(speciesRef, ns, prefix), new XMLAttributes());
  }


  public MESpeciesReference(MESpeciesReference sr) {
    super(sr);
    if (sr.isSetId()) {
      setId(sr.getId());
    }
  }


  /**
   * Create an {@link XMLNode} of the new MESpeciesReference for the given
   * species and stoichiometry.
   * <p>
   * These are used for the lists of products and reactants in the
   * {@link MEProcessData}.
   * </p>
   * 
   * @param speciesReference
   *        the id of the new speciesReference
   * @param stoichiometry
   *        the stoichiometry of the species in the process
   * @return the XMLNode of the MESpeciesReference
   */
  public XMLNode createMESpeciesReference(String speciesReference,
    double stoichiometry) {
    MESpeciesReference speciesRef = new MESpeciesReference();
    speciesRef.setSpecies(createSBMLConformId(speciesReference));
    speciesRef.setStoichiometry(String.valueOf(stoichiometry));
    return speciesRef;
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "species" is set.
   * 
   * @return whether the attribute "species" is set
   */
  public boolean isSetSpecies() {
    return isSetAttribute(MEConstants.species);
  }


  /**
   * Returns whether the attribute "stoichiometry" is set.
   * 
   * @return whether the attribute "stoichiometry" is set
   */
  public boolean isSetStoichiometry() {
    return isSetAttribute(MEConstants.stoichiometry);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "species".
   * 
   * @return the value of the attribute "species"
   */
  public String getSpecies() {
    if (isSetSpecies()) {
      return getAttribute(MEConstants.species);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "stoichiometry".
   * 
   * @return the value of the attribute "stoichiometry"
   */
  public String getStoichiometry() {
    if (isSetStoichiometry()) {
      return getAttribute(MEConstants.stoichiometry);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "species".
   * 
   * @param species
   *        the id of the species reference
   * @return
   */
  public int setSpecies(String species) {
    return setAttribute(MEConstants.species, species);
  }


  /**
   * Sets the value of the attribute "stoichiometry"
   * 
   * @param stoichiometry
   *        the stoichiometry of the species in the process
   * @return
   */
  public int setStoichiometry(String stoichiometry) {
    return setAttribute(MEConstants.stoichiometry, stoichiometry);
  }


  // ListOf functionality
  /**
   * Create an empty list of reactants.
   * 
   * @return the {@link XMLNode} with the list of reactants
   */
  public XMLNode ListOfMEReactants() {
    XMLNode listOf = new XMLNode(new XMLTriple("listOfReactants", ns, prefix),
      new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of reactants with a single child.
   * 
   * @param ec
   *        the child species reference
   * @return the {@link XMLNode} with the list of reactants
   */
  public XMLNode ListOfMEReactants(MESpeciesReference ec) {
    XMLNode listOf = new XMLNode(new XMLTriple("listOfReactants", ns, prefix),
      new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }


  /**
   * Create an empty list of products.
   * 
   * @return the {@link XMLNode} with the list of products
   */
  public XMLNode ListOfMEProducts() {
    XMLNode listOf = new XMLNode(new XMLTriple("listOfProducts", ns, prefix),
      new XMLAttributes());
    return listOf;
  }


  /**
   * Create a list of products with a single child.
   * 
   * @param ec
   *        the child species reference
   * @return the {@link XMLNode} with the list of products
   */
  public XMLNode ListOfMEProducts(MESpeciesReference ec) {
    XMLNode listOf = new XMLNode(new XMLTriple("listOfProducts", ns, prefix),
      new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
