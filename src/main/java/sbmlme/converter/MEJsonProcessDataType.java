package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the different types of process data in a COBRAme JSON schema. Any
 * process data object in a COBRAme model needs to have one and only one of the
 * declared types in this class.
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonProcessDataType {

  /**
   * The type of process data that encode information of a single
   * {@link MEJsonReactionType#TranscriptionReaction transcription} reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranscriptionData;
  /**
   * The type of process data that encode information of a single
   * {@link MEJsonReactionType#TranslationReaction translation} reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranslationData;
  /**
   * The type of process data that encode information of all
   * {@link MEJsonReactionType#GenericFormationReaction generic formation}
   * reactions that produce the same
   * {@link MEJsonMetaboliteType#GenericComponent generic component}.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes GenericData;
  /**
   * The type of process data that encode information of a single
   * {@link MEJsonReactionType#ComplexFormation complex formation}
   * reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes ComplexData;
  /**
   * The type of process data that encode information of a single
   * {@link MEJsonReactionType#tRNAChargingReaction tRNA charging} reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes tRNAData;
  /**
   * The type of process data that encode information of a single
   * {@link MEJsonReactionType#PostTranslationReaction post translation}
   * reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes PostTranslationData;
  /**
   * The type of process data that encode information of a subreaction process.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes SubreactionData;
  /**
   * The type of process data that encode information of a translocation
   * process.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonProcessDataTypeAttributes TranslocationData;
  /**
   * The type of process data that encode information of up to two
   * {@link MEJsonReactionType#MetabolicReaction metabolic} reactions.
   */
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
