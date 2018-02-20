package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent the different possible types of ProcessData
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonProcessDataType {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranscriptionData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranslationData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes GenericData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes ComplexData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes tRNAData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes PostTranslationData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes SubreactionData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranslocationData;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes StoichiometricData;


  public MEJsonProcessDataType() {
    super();
  }


  @JsonProperty("TranscriptionData")
  public MEJsonProcessDataTypeAttributes getTranscriptionData() {
    return TranscriptionData;
  }


  @JsonSetter("TranscriptionData")
  public void setTranscriptionData(MEJsonProcessDataTypeAttributes value) {
    TranscriptionData = value;
  }


  @JsonProperty("TranslationData")
  public MEJsonProcessDataTypeAttributes getTranslationData() {
    return TranslationData;
  }


  @JsonSetter("TranslationData")
  public void setTranslationData(MEJsonProcessDataTypeAttributes value) {
    TranslationData = value;
  }


  @JsonProperty("GenericData")
  public MEJsonProcessDataTypeAttributes getGenericData() {
    return GenericData;
  }


  @JsonSetter("GenericData")
  public void setGenericData(MEJsonProcessDataTypeAttributes value) {
    GenericData = value;
  }


  @JsonProperty("ComplexData")
  public MEJsonProcessDataTypeAttributes getComplexData() {
    return ComplexData;
  }


  @JsonSetter("ComplexData")
  public void setComplexData(MEJsonProcessDataTypeAttributes value) {
    ComplexData = value;
  }


  public MEJsonProcessDataTypeAttributes gettRNAData() {
    return tRNAData;
  }


  public void settRNAData(MEJsonProcessDataTypeAttributes value) {
    tRNAData = value;
  }


  @JsonProperty("PostTranslationData")
  public MEJsonProcessDataTypeAttributes getPostTranslationData() {
    return PostTranslationData;
  }


  @JsonSetter("PostTranslationData")
  public void setPostTranslationData(MEJsonProcessDataTypeAttributes value) {
    PostTranslationData = value;
  }


  @JsonProperty("SubreactionData")
  public MEJsonProcessDataTypeAttributes getSubreactionData() {
    return SubreactionData;
  }


  @JsonSetter("SubreactionData")
  public void setSubreactionData(MEJsonProcessDataTypeAttributes value) {
    SubreactionData = value;
  }


  @JsonProperty("TranslocationData")
  public MEJsonProcessDataTypeAttributes getTranslocationData() {
    return TranslocationData;
  }


  @JsonSetter("TranslocationData")
  public void setTranslocationData(MEJsonProcessDataTypeAttributes value) {
    TranslocationData = value;
  }


  @JsonProperty("StoichiometricData")
  public MEJsonProcessDataTypeAttributes getStoichiometricData() {
    return StoichiometricData;
  }


  @JsonSetter("StoichiometricData")
  public void setStoichiometricData(MEJsonProcessDataTypeAttributes value) {
    StoichiometricData = value;
  }
}
