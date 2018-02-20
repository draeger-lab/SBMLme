package sbmlme.converter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent JSON schema of COBRAme for SBML to JSON conversion
 * 
 * @author Marc A. Voigt
 */
public class MEJsonCOBRAme {

  private List<MEJsonReaction>    reactions;
  private List<MEJsonMetabolite>  metabolites;
  private List<MEJsonProcessData> processData;
  private Map<String, Double>     global_info;


  public MEJsonCOBRAme() {
    super();
  }


  public void setReactions(List<MEJsonReaction> reactions) {
    this.reactions = reactions;
  }


  public List<MEJsonReaction> getReactions() {
    return reactions;
  }


  public void setMetabolites(List<MEJsonMetabolite> metabolites) {
    this.metabolites = metabolites;
  }


  public List<MEJsonMetabolite> getMetabolites() {
    return metabolites;
  }


  @JsonSetter("process_data")
  public void setProcessData(List<MEJsonProcessData> processData) {
    this.processData = processData;
  }


  @JsonProperty("process_data")
  public List<MEJsonProcessData> getProcessData() {
    return processData;
  }


  @JsonSetter("global_info")
  public void setGlobalInfo(Map<String, Double> global_info) {
    this.global_info = global_info;
  }


  @JsonProperty("global_info")
  public Map<String, Double> getGlobalInfo() {
    return global_info;
  }
}
