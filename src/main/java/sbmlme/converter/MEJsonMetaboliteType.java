package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents the species type in a COBRAme model. Any species in a COBRAme
 * model needs to have one and only one of the declared types in this class.
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonMetaboliteType {

  /**
   * The type for species that represent a type of constraint in the model, e.g.
   * the different kinds of biomass.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Constraint;
  /**
   * The type for simple metabolites.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Metabolite;
  /**
   * The type for protein complexes.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Complex;
  /**
   * The type for the generic loaded tRNAs of a COBRAme model.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes GenerictRNA;
  /**
   * The type for species representing a protein created by a
   * {@link MEJsonReactionType#TranslationReaction translation} reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes TranslatedGene;
  /**
   * The type for ribosomes
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Ribosome;
  /**
   * The type for RNA polymerases.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes RNAP;
  /**
   * The type for the generic components of a COBRAme model. These components
   * are used to represent species that can be interchangeably used in certain
   * reactions.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes GenericComponent;
  /**
   * The type for species representing a protein created by a
   * {@link MEJsonReactionType#PostTranslationReaction post translation}
   * reaction.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes ProcessedProtein;
  /**
   * The type for species representing RNAs created by
   * {@link MEJsonReactionType#TranscriptionReaction transcription} reactions.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes TranscribedGene;


  public MEJsonMetaboliteType() {
    super();
  }


  // getter and setter methods
  @JsonSetter("Constraint")
  public void setConstraint(MEJsonMetaboliteTypeAttributes Metabolite) {
    Constraint = Metabolite;
  }


  @JsonProperty("Constraint")
  public MEJsonMetaboliteTypeAttributes getConstraint() {
    return Constraint;
  }


  @JsonSetter("Metabolite")
  public void setMetabolite(MEJsonMetaboliteTypeAttributes Metabolite) {
    this.Metabolite = Metabolite;
  }


  @JsonProperty("Metabolite")
  public MEJsonMetaboliteTypeAttributes getMetabolite() {
    return Metabolite;
  }


  @JsonSetter("Complex")
  public void setComplex(MEJsonMetaboliteTypeAttributes Metabolite) {
    Complex = Metabolite;
  }


  @JsonProperty("Complex")
  public MEJsonMetaboliteTypeAttributes getComplex() {
    return Complex;
  }


  @JsonSetter("GenerictRNA")
  public void setGenerictRNA(MEJsonMetaboliteTypeAttributes Metabolite) {
    GenerictRNA = Metabolite;
  }


  @JsonProperty("GenerictRNA")
  public MEJsonMetaboliteTypeAttributes getGenerictRNA() {
    return GenerictRNA;
  }


  @JsonSetter("TranslatedGene")
  public void setTranslatedGene(MEJsonMetaboliteTypeAttributes Metabolite) {
    TranslatedGene = Metabolite;
  }


  @JsonProperty("TranslatedGene")
  public MEJsonMetaboliteTypeAttributes getTranslatedGene() {
    return TranslatedGene;
  }


  @JsonSetter("Ribosome")
  public void setRibosome(MEJsonMetaboliteTypeAttributes Metabolite) {
    Ribosome = Metabolite;
  }


  @JsonProperty("Ribosome")
  public MEJsonMetaboliteTypeAttributes getRibosome() {
    return Ribosome;
  }


  @JsonSetter("RNAP")
  public void setRNAP(MEJsonMetaboliteTypeAttributes Metabolite) {
    RNAP = Metabolite;
  }


  @JsonProperty("RNAP")
  public MEJsonMetaboliteTypeAttributes getRNAP() {
    return RNAP;
  }


  @JsonSetter("GenericComponent")
  public void setGenericComponent(MEJsonMetaboliteTypeAttributes Metabolite) {
    GenericComponent = Metabolite;
  }


  @JsonProperty("GenericComponent")
  public MEJsonMetaboliteTypeAttributes getGenericComponent() {
    return GenericComponent;
  }


  @JsonSetter("ProcessedProtein")
  public void setProcessedProtein(MEJsonMetaboliteTypeAttributes Metabolite) {
    ProcessedProtein = Metabolite;
  }


  @JsonProperty("ProcessedProtein")
  public MEJsonMetaboliteTypeAttributes getProcessedProtein() {
    return ProcessedProtein;
  }


  @JsonSetter("TranscribedGene")
  public void setTranscribedGene(MEJsonMetaboliteTypeAttributes Metabolite) {
    TranscribedGene = Metabolite;
  }


  @JsonProperty("TranscribedGene")
  public MEJsonMetaboliteTypeAttributes getTranscribedGene() {
    return TranscribedGene;
  }
}
