package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * this class represents the JSON schema for the inner attributes of the
 * different reaction types
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonReactionTypeAttributes {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  transcription_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  translation_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  tRNA_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  posttranslation_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complex_data_id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  stoichiometric_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complexId;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complex_data;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double  keff;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean reverse;


  public MEJsonReactionTypeAttributes() {
    super();
  }


  public void setKeff(double keff) {
    this.keff = keff;
  }


  /**
   * get the keff of a metabolic reaction
   * 
   * @return
   */
  public Double getKeff() {
    return keff;
  }


  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }


  /**
   * return false if the metabolic reaction is in forward direction, return true
   * if the reaction is in reverse direction
   * 
   * @return
   */
  public Boolean getReverse() {
    return reverse;
  }


  // getter and setter functions
  /**
   * set the id of the process data object of a transcription reaction
   * 
   * @return
   */
  @JsonSetter("transcription_data")
  public void setTranscriptionDataId(String dataId) {
    transcription_data = dataId;
  }


  public String getTranscriptionDataId() {
    return transcription_data;
  }


  /**
   * set the id of the process data object of a translation reaction
   * 
   * @return
   */
  @JsonSetter("translation_data")
  public void setTranslationDataId(String dataId) {
    translation_data = dataId;
  }


  public String getTranslationDataId() {
    return translation_data;
  }


  /**
   * set the id of the process data object of a tRNA charging reaction
   * 
   * @return
   */
  @JsonSetter("tRNA_data")
  public void settRNADataId(String dataId) {
    tRNA_data = dataId;
  }


  public String gettRNADataId() {
    return tRNA_data;
  }


  /**
   * set the id of the process data object of a posttranslation reaction
   * 
   * @return
   */
  @JsonSetter("posttranslation_data")
  public void setPostTranslationDataId(String dataId) {
    posttranslation_data = dataId;
  }


  public String getPostTranslationDataId() {
    return posttranslation_data;
  }


  /**
   * set the id of the process data object of a complex formation reaction
   * 
   * @return
   */
  @JsonSetter("complex_data_id")
  public void setComplexDataId(String dataId) {
    complex_data_id = dataId;
  }


  public String getComplexDataId() {
    return complex_data_id;
  }


  /**
   * set the id of the process data object of a metabolic reaction
   * 
   * @return
   */
  @JsonSetter("stoichiometric_data")
  public void setStoichiometricDataId(String dataId) {
    stoichiometric_data = dataId;
  }


  public String getStoichiometricDataId() {
    return stoichiometric_data;
  }


  /**
   * set the id of the complex created by an complex formation reaction
   * 
   * @return
   */
  @JsonSetter("_complex_id")
  public void setComplexFormationComplexId(String complexId) {
    this.complexId = complexId;
  }


  public String getComplexFormationComplexId() {
    return complexId;
  }


  /**
   * set the id of the complex data object in a metabolic reaction
   * 
   * @return
   */
  @JsonSetter("complex_data")
  public void setMetabolicReactionComplexData(String complexId) {
    complex_data = complexId;
  }


  public String getMetabolicReactionComplexData() {
    return complex_data;
  }
}
