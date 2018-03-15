package sbmlme;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * implements basic methods for adding attributes to the XMLNode representation
 * of the annotation of an SBML object.
 * 
 * @author Marc A. Voigt
 */
public class MEAbstractXMLNodePlugin extends XMLNode implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public MEAbstractXMLNodePlugin() {
    super();
  }


  public MEAbstractXMLNodePlugin(XMLTriple xmlTriple,
    XMLAttributes xmlAttributes) {
    super(xmlTriple, xmlAttributes);
  }


  public MEAbstractXMLNodePlugin(MEAbstractXMLNodePlugin plugin) {
    super(plugin);
  }


  /**
   * turn COBRA id into a SBML conform id
   * <p>
   * A COBRA id may contain characters that are forbidden in an SBML id. There
   * are also lesser restriction as to what characters may start an id in COBRA.
   * This method is therefore used to transform a COBRA id into a valid SBML id.
   * </p>
   * 
   * @param id
   *        a string representing an id
   * @return the SBML conform id
   */
  public String createSBMLConformId(String id) {
    id = id.replace(":", "__meCOLONme__");
    id = id.replace("-", "__meMINUSme__");
    id = id.replace("/", "__meSLASHme__");
    if (id.matches("^\\d.*")) {
      id = "__meSTARTme__" + id;
    }
    return id;
  }


  /**
   * Set the attribute of the given key to the given value.
   * 
   * @param key
   *        the visible name of the attribute to be added
   * @param value
   *        the value that the attribute should take
   * @return
   */
  public int setAttribute(String key, String value) {
    if (attributes.hasAttribute(key)) {
      attributes.remove(attributes.getIndex(key));
    }
    return attributes.add(key, value);
  }


  /**
   * Returns whether the attribute of the given key is set.
   * 
   * @param key
   *        the visible name of the attribute
   * @return
   */
  public boolean isSetAttribute(String key) {
    return attributes.hasAttribute(key);
  }


  /**
   * Return the value of the attribute of the given key.
   * 
   * @param key
   *        the visible name of the attribute
   * @return
   */
  public String getAttribute(String key) {
    return attributes.getValue(key);
  }


  // functions for checking if a certain attribute is set
  /**
   * Returns whether the attribute "id" is set.
   * 
   * @return whether the attribute "id" is set.
   */
  public boolean isSetId() {
    return isSetAttribute(MEConstants.id);
  }


  /**
   * Returns whether the attribute "name" is set.
   * 
   * @return whether the attribute "name" is set.
   */
  public boolean isSetName() {
    return isSetAttribute(MEConstants.name);
  }


  // getter functions for attributes
  /**
   * Returns the value of the attribute "id".
   * 
   * @return the value of the attribute "id".
   */
  public String getId() {
    if (isSetId()) {
      return getAttribute(MEConstants.id);
    }
    return "";
  }


  /**
   * Returns the value of the attribute "name".
   * 
   * @return the value of the attribute "name".
   */
  @Override
  public String getName() {
    if (isSetName()) {
      return getAttribute(MEConstants.name);
    }
    return "";
  }


  // Setter functions
  /**
   * Sets the value of the attribute "id".
   * 
   * @param id
   *        the id by which the XMLNode should be referred to
   * @return
   */
  public int setId(String id) {
    return setAttribute(MEConstants.id, id);
  }


  /**
   * Sets the value of the attribute "name".
   * 
   * @param name
   *        the name by which the XMLNode should be referred to
   * @return
   */
  public int setName(String name) {
    return setAttribute(MEConstants.name, name);
  }
}
