package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent JSON format of a COBRAme metabolite
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"compartment", "formula", "metabolite_type", "name", "id"})
public class MEJsonMetabolite {

  private String               compartment;
  private String               formula;
  private MEJsonMetaboliteType metabolite_type;
  private String               name;
  private String               id;


  public MEJsonMetabolite() {
    super();
  }
  // getter and setter methods


  public void setCompartment(String Compartment) {
    compartment = Compartment;
  }


  public String getCompartment() {
    return compartment;
  }


  public void setFormula(String Formula) {
    formula = Formula;
  }


  public String getFormula() {
    return formula;
  }


  @JsonSetter("metabolite_type")
  public void setMetaboliteType(MEJsonMetaboliteType metabolite_type) {
    this.metabolite_type = metabolite_type;
  }


  public MEJsonMetaboliteType getMetaboliteType() {
    return metabolite_type;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getName() {
    return name;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getId() {
    return id;
  }
}
