package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent ProcessData object in aCOBRAme JSON file
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"process_data_type", "id"})
public class MEJsonProcessData {

  private MEJsonProcessDataType process_data_type;
  private String                id;


  public MEJsonProcessData() {
    super();
  }


  @JsonProperty("process_data_type")
  public MEJsonProcessDataType getProcessDataType() {
    return process_data_type;
  }


  @JsonSetter("process_data_type")
  public void setProcessDataType(MEJsonProcessDataType value) {
    process_data_type = value;
  }


  public void setId(String id) {
    this.id = id;
  }


  public String getId() {
    return id;
  }
}
