package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the JSON schema for the inner attributes of the different reaction
 * types.
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonReactionTypeAttributes {

  /**
   * The id of the {@link MEJsonProcessDataType#TranscriptionData
   * TranscriptionData} object that contains additional information about the
   * {@link MEJsonReactionType#TranscriptionReaction TranscriptionReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  transcription_data;
  /**
   * The id of the {@link MEJsonProcessDataType#TranslationData
   * TranslationData} object that contains additional information about the
   * {@link MEJsonReactionType#TranslationReaction TranslationReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  translation_data;
  /**
   * The id of the {@link MEJsonProcessDataType#tRNAData tRNAData} object that
   * contains additional information about the
   * {@link MEJsonReactionType#tRNAChargingReaction tRNAChargingReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  tRNA_data;
  /**
   * The id of the {@link MEJsonProcessDataType#PostTranslationData
   * PostTranslationData} object that contains additional information about the
   * {@link MEJsonReactionType#PostTranslationReaction PostTranslationReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  posttranslation_data;
  /**
   * The id of the {@link MEJsonProcessDataType#ComplexData ComplexData} object
   * that contains additional information about the
   * {@link MEJsonReactionType#ComplexFormation ComplexFormation}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complex_data_id;
  /**
   * The id of the {@link MEJsonMetaboliteType#Complex Complex} created by the
   * {@link MEJsonReactionType#ComplexFormation ComplexFormation}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complexId;
  /**
   * The id of the {@link MEJsonProcessDataType#StoichiometricData
   * StoichiometricData} object that contains additional information about the
   * {@link MEJsonReactionType#MetabolicReaction MetabolicReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  stoichiometric_data;
  /**
   * The id of the {@link MEJsonProcessDataType#ComplexData ComplexData} object
   * that contains additional information about the complexes that take part in
   * the {@link MEJsonReactionType#MetabolicReaction MetabolicReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  complex_data;
  /**
   * The effective turnover rate that couples enzymatic dilution to the flux of
   * the {@link MEJsonReactionType#MetabolicReaction MetabolicReaction}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double  keff;
  /**
   * Whether the instance of the {@link MEJsonReactionType#MetabolicReaction
   * MetabolicReaction} corresponds to the reverse direction of the reversible
   * process or not.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean reverse;


  public MEJsonReactionTypeAttributes() {
    super();
  }


  public void setKeff(double keff) {
    this.keff = keff;
  }


  /**
   * Gets the turnover rate of the enzymes in the metabolic reaction.
   * 
   * @return the turnover rate of the enzymes in the metabolic reaction
   */
  public Double getKeff() {
    return keff;
  }


  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }


  /**
   * Returns false if the metabolic reaction is in forward direction, return
   * true
   * if the reaction is in reverse direction
   * 
   * @return false if the metabolic reaction is in forward direction
   */
  public Boolean getReverse() {
    return reverse;
  }


  // getter and setter functions
  @JsonSetter("transcription_data")
  public void setTranscriptionDataId(String dataId) {
    transcription_data = dataId;
  }


  public String getTranscriptionDataId() {
    return transcription_data;
  }


  @JsonSetter("translation_data")
  public void setTranslationDataId(String dataId) {
    translation_data = dataId;
  }


  public String getTranslationDataId() {
    return translation_data;
  }


  @JsonSetter("tRNA_data")
  public void settRNADataId(String dataId) {
    tRNA_data = dataId;
  }


  public String gettRNADataId() {
    return tRNA_data;
  }


  @JsonSetter("posttranslation_data")
  public void setPostTranslationDataId(String dataId) {
    posttranslation_data = dataId;
  }


  public String getPostTranslationDataId() {
    return posttranslation_data;
  }


  @JsonSetter("complex_data_id")
  public void setComplexDataId(String dataId) {
    complex_data_id = dataId;
  }


  public String getComplexDataId() {
    return complex_data_id;
  }


  @JsonSetter("stoichiometric_data")
  public void setStoichiometricDataId(String dataId) {
    stoichiometric_data = dataId;
  }


  public String getStoichiometricDataId() {
    return stoichiometric_data;
  }


  @JsonSetter("_complex_id")
  public void setComplexFormationComplexId(String complexId) {
    this.complexId = complexId;
  }


  public String getComplexFormationComplexId() {
    return complexId;
  }


  @JsonSetter("complex_data")
  public void setMetabolicReactionComplexData(String complexId) {
    complex_data = complexId;
  }


  public String getMetabolicReactionComplexData() {
    return complex_data;
  }
}
