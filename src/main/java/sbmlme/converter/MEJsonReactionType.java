package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * class for representing the reaction type in a JSON serialization
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonReactionType {

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes SummaryVariable;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes MEReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes GenericFormationReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes TranscriptionReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes TranslationReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes tRNAChargingReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes PostTranslationReaction;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes ComplexFormation;
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes MetabolicReaction;


  public MEJsonReactionType() {
    super();
  }


  // setter and getter functions for different reaction types
  /**
   * set reaction type for SummaryVariable
   * 
   * @param reactionType
   */
  @JsonSetter("SummaryVariable")
  public void setSummaryVariable(MEJsonReactionTypeAttributes reactionType) {
    SummaryVariable = reactionType;
  }


  @JsonProperty("SummaryVariable")
  public MEJsonReactionTypeAttributes getSummaryVariable() {
    return SummaryVariable;
  }


  /**
   * set reaction type for MEReaction
   * 
   * @param reactionType
   */
  @JsonSetter("MEReaction")
  public void setMEReaction(MEJsonReactionTypeAttributes reactionType) {
    MEReaction = reactionType;
  }


  @JsonProperty("MEReaction")
  public MEJsonReactionTypeAttributes getMEReaction() {
    return MEReaction;
  }


  /**
   * set reaction type for GenericFormationReaction
   * 
   * @param reactionType
   */
  @JsonSetter("GenericFormationReaction")
  public void setGenericFormationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    GenericFormationReaction = reactionType;
  }


  @JsonProperty("GenericFormationReaction")
  public MEJsonReactionTypeAttributes getGenericFormationReaction() {
    return GenericFormationReaction;
  }


  /**
   * set reaction type for TranscriptionReaction
   * 
   * @param reactionType
   */
  @JsonSetter("TranscriptionReaction")
  public void setTranscriptionReaction(
    MEJsonReactionTypeAttributes reactionType) {
    TranscriptionReaction = reactionType;
  }


  @JsonProperty("TranscriptionReaction")
  public MEJsonReactionTypeAttributes getTranscriptionReaction() {
    return TranscriptionReaction;
  }


  /**
   * set reaction type for TranslationReaction
   * 
   * @param reactionType
   */
  @JsonSetter("TranslationReaction")
  public void setTranslationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    TranslationReaction = reactionType;
  }


  @JsonProperty("TranslationReaction")
  public MEJsonReactionTypeAttributes getTranslationReaction() {
    return TranslationReaction;
  }


  /**
   * set reaction type for tRNAChargingReaction
   * 
   * @param reactionType
   */
  @JsonSetter("tRNAChargingReaction")
  public void settRNAChargingReaction(
    MEJsonReactionTypeAttributes reactionType) {
    tRNAChargingReaction = reactionType;
  }


  @JsonProperty("tRNAChargingReaction")
  public MEJsonReactionTypeAttributes gettRNAChargingReaction() {
    return tRNAChargingReaction;
  }


  /**
   * set reaction type for PostTranslationReaction
   * 
   * @param reactionType
   */
  @JsonSetter("PostTranslationReaction")
  public void setPostTranslationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    PostTranslationReaction = reactionType;
  }


  @JsonProperty("PostTranslationReaction")
  public MEJsonReactionTypeAttributes getPostTranslationReaction() {
    return PostTranslationReaction;
  }


  /**
   * set reaction type for ComplexFormation
   * 
   * @param reactionType
   */
  @JsonSetter("ComplexFormation")
  public void setComplexFormation(MEJsonReactionTypeAttributes reactionType) {
    ComplexFormation = reactionType;
  }


  @JsonProperty("ComplexFormation")
  public MEJsonReactionTypeAttributes getComplexFormation() {
    return ComplexFormation;
  }


  /**
   * set reaction type for MetabolicReaction
   * 
   * @param reactionType
   */
  @JsonSetter("MetabolicReaction")
  public void setMetabolicReaction(MEJsonReactionTypeAttributes reactionType) {
    MetabolicReaction = reactionType;
  }


  @JsonProperty("MetabolicReaction")
  public MEJsonReactionTypeAttributes getMetabolicReaction() {
    return MetabolicReaction;
  }
}
