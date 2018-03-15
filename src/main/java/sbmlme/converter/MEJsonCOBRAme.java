package sbmlme.converter;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents JSON schema of COBRAme models for SBML to JSON conversion.
 * 
 * @author Marc A. Voigt
 */
public class MEJsonCOBRAme {

  /**
   * The list of reactions in the model.
   */
  private List<MEJsonReaction>    reactions;
  /**
   * The list of species in the model.
   */
  private List<MEJsonMetabolite>  metabolites;
  /**
   * The list of process data objects in the model.
   */
  private List<MEJsonProcessData> processData;
  /**
   * The map of global information of the model.
   */
  private Map<String, Double>     global_info;


  public MEJsonCOBRAme() {
    super();
  }


  /**
   * Sets the list of reactions for the JSON schema.
   * 
   * @param reactions
   *        the list of {@link MEJsonReaction}
   */
  public void setReactions(List<MEJsonReaction> reactions) {
    this.reactions = reactions;
  }


  /**
   * Returns the list of reactions.
   * 
   * @return the list of {@link MEJsonReaction}
   */
  public List<MEJsonReaction> getReactions() {
    return reactions;
  }


  /**
   * Sets the list of species for the JSON schema.
   * 
   * @param metabolites
   *        the list of {@link MEJsonMetabolite}
   */
  public void setMetabolites(List<MEJsonMetabolite> metabolites) {
    this.metabolites = metabolites;
  }


  /**
   * Returns the list of species.
   * 
   * @return the list of {@link MEJsonMetabolite}
   */
  public List<MEJsonMetabolite> getMetabolites() {
    return metabolites;
  }


  /**
   * Sets the list of process data objects for the JSON schema.
   * 
   * @param processData
   *        the list of {@link MEJsonProcessData}
   */
  @JsonSetter("process_data")
  public void setProcessData(List<MEJsonProcessData> processData) {
    this.processData = processData;
  }


  /**
   * Returns the list of process data objects.
   * 
   * @return the list of {@link MEJsonProcessData}
   */
  @JsonProperty("process_data")
  public List<MEJsonProcessData> getProcessData() {
    return processData;
  }


  /**
   * Sets the global information for the JSON schema.
   * 
   * @param global_info
   *        the map with the {@link global_info}
   */
  @JsonSetter("global_info")
  public void setGlobalInfo(Map<String, Double> global_info) {
    this.global_info = global_info;
  }


  /**
   * Returns the global information of the schema.
   * 
   * @return the map of {@link global_info}
   */
  @JsonProperty("global_info")
  public Map<String, Double> getGlobalInfo() {
    return global_info;
  }
}
