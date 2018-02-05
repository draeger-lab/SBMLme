import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
    super(new XMLTriple("meSpeciesReference", ns, prefix), new XMLAttributes());
  }


  public MESpeciesReference(MESpeciesReference sr) {
    super(sr);
    if (sr.isSetId()) {
      setId(sr.getId());
    }
  }


  /**
   * @param speciesReference
   * @param stoichiometry
   * @return
   */
  public XMLNode createMESpeciesReference(String speciesReference,
    double stoichiometry) {
    MESpeciesReference speciesRef = new MESpeciesReference();
    speciesRef.setSpecies(createSBMLConformId(speciesReference));
    speciesRef.setStoichiometry(String.valueOf(stoichiometry));
    return speciesRef;
  }


  // functions for checking if a certain attribute is set
  public boolean isSetSpecies() {
    return isSetAttribute(MEConstants.species);
  }


  public boolean isSetStoichiometry() {
    return isSetAttribute(MEConstants.stoichiometry);
  }


  // getter functions for attributes
  public String getSpecies() {
    if (isSetSpecies()) {
      return getAttribute(MEConstants.species);
    }
    return "";
  }


  public String getStoichiometry() {
    if (isSetStoichiometry()) {
      return getAttribute(MEConstants.stoichiometry);
    }
    return "";
  }


  // Setter functions
  public int setSpecies(String species) {
    return setAttribute(MEConstants.species, species);
  }


  public int setStoichiometry(String stoichiometry) {
    return setAttribute(MEConstants.stoichiometry, stoichiometry);
  }


  // ListOf functionality
  public XMLNode ListOfMEReactants() {
    XMLNode listOf = new XMLNode(new XMLTriple("ListOfReactants", ns, prefix),
      new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfMEReactants(MESpeciesReference ec) {
    XMLNode listOf = new XMLNode(new XMLTriple("ListOfReactants", ns, prefix),
      new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }


  public XMLNode ListOfMEProducts() {
    XMLNode listOf = new XMLNode(new XMLTriple("ListOfProducts", ns, prefix),
      new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfMEProducts(MESpeciesReference ec) {
    XMLNode listOf = new XMLNode(new XMLTriple("ListOfProducts", ns, prefix),
      new XMLAttributes());
    listOf.addChild(ec);
    return listOf;
  }
}
