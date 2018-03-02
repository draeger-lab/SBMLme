package sbmlme;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
   * 
   * @param id
   * @return
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
   * @param key
   * @param value
   * @return
   */
  public int setAttribute(String key, String value) {
    if (attributes.hasAttribute(key)) {
      attributes.remove(attributes.getIndex(key));
    }
    return attributes.add(key, value);
  }


  public boolean isSetAttribute(String key) {
    return attributes.hasAttribute(key);
  }


  public String getAttribute(String key) {
    return attributes.getValue(key);
  }


  // functions for checking if a certain attribute is set
  public boolean isSetId() {
    return isSetAttribute(MEConstants.id);
  }


  public boolean isSetName() {
    return isSetAttribute(MEConstants.name);
  }


  // getter functions for attributes
  public String getId() {
    if (isSetId()) {
      return getAttribute(MEConstants.id);
    }
    return "";
  }


  @Override
  public String getName() {
    if (isSetName()) {
      return getAttribute(MEConstants.name);
    }
    return "";
  }


  // Setter functions
  public int setId(String id) {
    return setAttribute(MEConstants.id, id);
  }


  public int setName(String name) {
    return setAttribute(MEConstants.name, name);
  }
}
