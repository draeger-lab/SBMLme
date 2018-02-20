package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represents the type of a Metabolite
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonMetaboliteType {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Constraint;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Metabolite;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Complex;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes GenerictRNA;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes TranslatedGene;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes Ribosome;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes RNAP;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes GenericComponent;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private MEJsonMetaboliteTypeAttributes ProcessedProtein;
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
