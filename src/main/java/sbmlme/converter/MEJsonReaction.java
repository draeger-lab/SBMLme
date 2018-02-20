package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * represent JSON format of a single reaction entry in the list of reactions in
 * a COBRAme JSON file
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"reaction_type", "name", "upper_bound", "lower_bound",
  "metabolites", "objective_coefficient", "variable_kind", "id"})
public class MEJsonReaction {

  private MEJsonReactionType reaction_type;
  private String             name;
  private String             upper_bound;
  private String             lower_bound;
  // metabolites in COBRAme may have a value of type int, double or String. To
  // represent this the metabolites should be added as fields to a single
  // ObjectNode
  private ObjectNode         metabolites;
  private double             objective_coefficient;
  private String             variable_kind;
  private String             id;


  public MEJsonReaction() {
    super();
  }


  // standard getters and setters
  @JsonProperty("reaction_type")
  public void setReactionType(MEJsonReactionType reaction_type) {
    this.reaction_type = reaction_type;
  }


  @JsonProperty("reaction_type")
  public MEJsonReactionType getReactionType() {
    return reaction_type;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getName() {
    return name;
  }


  @JsonProperty("upper_bound")
  public void setUpperBound(String upper_bound) {
    this.upper_bound = upper_bound;
  }


  @JsonProperty("upper_bound")
  public String getUpperBound() {
    return upper_bound;
  }


  @JsonProperty("lower_bound")
  public void setLowerBound(String lower_bound) {
    this.lower_bound = lower_bound;
  }


  @JsonProperty("lower_bound")
  public String getLowerBound() {
    return lower_bound;
  }


  public void setMetabolites(ObjectNode metabolites) {
    this.metabolites = metabolites;
  }


  public ObjectNode getMetabolites() {
    return metabolites;
  }


  @JsonProperty("objective_coefficient")
  public void setObjectiveCoefficient(double objective_coefficient) {
    this.objective_coefficient = objective_coefficient;
  }


  @JsonProperty("objective_coefficient")
  public double getObjectiveCoefficient() {
    return objective_coefficient;
  }


  @JsonProperty("variable_kind")
  public void setVariableKind(String variable_kind) {
    this.variable_kind = variable_kind;
  }


  @JsonProperty("variable_kind")
  public String getVariableKind() {
    return variable_kind;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getId() {
    return id;
  }
}
