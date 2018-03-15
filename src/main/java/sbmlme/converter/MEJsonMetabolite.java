package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the JSON format of a species in a COBRAme model.
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"compartment", "formula", "metabolite_type", "name", "id"})
public class MEJsonMetabolite {

  /**
   * The compartment where the species is located.
   */
  private String               compartment;
  /**
   * The formula of the species, may be null.
   */
  private String               formula;
  /**
   * The type of the species with additional nested attributes.
   */
  private MEJsonMetaboliteType metabolite_type;
  /**
   * The name of the species, may be null.
   */
  private String               name;
  /**
   * The id of the species.
   */
  private String               id;


  public MEJsonMetabolite() {
    super();
  }
  // getter and setter methods


  /**
   * Sets the compartment of the species.
   * 
   * @param Compartment
   *        the id of the compartment where the species is located
   */
  public void setCompartment(String Compartment) {
    compartment = Compartment;
  }


  /**
   * Returns the compartment of the species.
   * 
   * @return the compartment of the species
   */
  public String getCompartment() {
    return compartment;
  }


  /**
   * Sets the formula of the species.
   * 
   * @param Formula
   *        the formula of the species
   */
  public void setFormula(String Formula) {
    formula = Formula;
  }


  /**
   * Returns the formula of the species.
   * 
   * @return the formula of the species
   */
  public String getFormula() {
    return formula;
  }


  /**
   * Sets the type of the species.
   * 
   * @param metabolite_type
   *        the type of the species
   */
  @JsonSetter("metabolite_type")
  public void setMetaboliteType(MEJsonMetaboliteType metabolite_type) {
    this.metabolite_type = metabolite_type;
  }


  /**
   * Returns the type of the species with its nested attributes.
   * 
   * @return the type of the species
   */
  public MEJsonMetaboliteType getMetaboliteType() {
    return metabolite_type;
  }


  /**
   * Sets the name of the species.
   * 
   * @param name
   *        the name of the species
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Returns the name of the species.
   * 
   * @return the name of the species
   */
  public String getName() {
    return name;
  }


  /**
   * Sets the id of the species.
   * 
   * @param id
   *        the id of the species
   */
  public void setId(String id) {
    this.id = id;
  }


  /**
   * Returns the id of the species.
   * 
   * @return the id of the species
   */
  public String getId() {
    return id;
  }
}
