package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the different types of reactions in a COBRAme JSON schema. Any
 * reactions in a COBRAme model needs to have one and only one of the declared
 * types in this class.
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonReactionType {

  /**
   * The type of reactions that apply a global constraint to the model.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes SummaryVariable;
  /**
   * The type for all ME exclusive reactions that can not be assigned to another
   * type.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes MEReaction;
  /**
   * All reactions that create {@link MEJsonMetaboliteType#GenericComponent
   * generic components}.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes GenericFormationReaction;
  /**
   * All reactions that transcribe a gene.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes TranscriptionReaction;
  /**
   * All reactions that translate a mRNA.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes TranslationReaction;
  /**
   * All reactions that charge a {@link MEJsonMetaboliteType#GenerictRNA generic
   * tRNA}.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes tRNAChargingReaction;
  /**
   * All reactions that create a {@link MEJsonMetaboliteType#ProcessedProtein
   * ProcessedProtein}.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes PostTranslationReaction;
  /**
   * All reactions that create a {@link MEJsonMetaboliteType#Complex protein
   * complex}.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes ComplexFormation;
  /**
   * All reactions that were part of the M-model on which the COBRAme model is
   * based.
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private MEJsonReactionTypeAttributes MetabolicReaction;


  public MEJsonReactionType() {
    super();
  }


  // setter and getter functions for different reaction types
  @JsonSetter("SummaryVariable")
  public void setSummaryVariable(MEJsonReactionTypeAttributes reactionType) {
    SummaryVariable = reactionType;
  }


  @JsonProperty("SummaryVariable")
  public MEJsonReactionTypeAttributes getSummaryVariable() {
    return SummaryVariable;
  }


  @JsonSetter("MEReaction")
  public void setMEReaction(MEJsonReactionTypeAttributes reactionType) {
    MEReaction = reactionType;
  }


  @JsonProperty("MEReaction")
  public MEJsonReactionTypeAttributes getMEReaction() {
    return MEReaction;
  }


  @JsonSetter("GenericFormationReaction")
  public void setGenericFormationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    GenericFormationReaction = reactionType;
  }


  @JsonProperty("GenericFormationReaction")
  public MEJsonReactionTypeAttributes getGenericFormationReaction() {
    return GenericFormationReaction;
  }


  @JsonSetter("TranscriptionReaction")
  public void setTranscriptionReaction(
    MEJsonReactionTypeAttributes reactionType) {
    TranscriptionReaction = reactionType;
  }


  @JsonProperty("TranscriptionReaction")
  public MEJsonReactionTypeAttributes getTranscriptionReaction() {
    return TranscriptionReaction;
  }


  @JsonSetter("TranslationReaction")
  public void setTranslationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    TranslationReaction = reactionType;
  }


  @JsonProperty("TranslationReaction")
  public MEJsonReactionTypeAttributes getTranslationReaction() {
    return TranslationReaction;
  }


  @JsonSetter("tRNAChargingReaction")
  public void settRNAChargingReaction(
    MEJsonReactionTypeAttributes reactionType) {
    tRNAChargingReaction = reactionType;
  }


  @JsonProperty("tRNAChargingReaction")
  public MEJsonReactionTypeAttributes gettRNAChargingReaction() {
    return tRNAChargingReaction;
  }


  @JsonSetter("PostTranslationReaction")
  public void setPostTranslationReaction(
    MEJsonReactionTypeAttributes reactionType) {
    PostTranslationReaction = reactionType;
  }


  @JsonProperty("PostTranslationReaction")
  public MEJsonReactionTypeAttributes getPostTranslationReaction() {
    return PostTranslationReaction;
  }


  @JsonSetter("ComplexFormation")
  public void setComplexFormation(MEJsonReactionTypeAttributes reactionType) {
    ComplexFormation = reactionType;
  }


  @JsonProperty("ComplexFormation")
  public MEJsonReactionTypeAttributes getComplexFormation() {
    return ComplexFormation;
  }


  @JsonSetter("MetabolicReaction")
  public void setMetabolicReaction(MEJsonReactionTypeAttributes reactionType) {
    MetabolicReaction = reactionType;
  }


  @JsonProperty("MetabolicReaction")
  public MEJsonReactionTypeAttributes getMetabolicReaction() {
    return MetabolicReaction;
  }
}
