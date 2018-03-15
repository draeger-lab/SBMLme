package sbmlme.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents attributes of different metabolite types in the COBRAme JSON
 * schema.
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"left_pos", "RNA_type", "right_pos", "strand",
  "nucleotide_sequence"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonMetaboliteTypeAttributes {

  /**
   * The id of the protein that has been modified by a
   * {@link MEJsonReactionType#PostTranslationReaction post translation}
   * reaction. This is a mandatory attribute for species of type
   * {@link MEJsonMetaboliteType#ProcessedProtein ProcessedProtein} and should
   * not be set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  unprocessed_protein_id;
  /**
   * The starting position of the nucleotide sequence on the genome. This is a
   * mandatory attribute for species of type
   * {@link MEJsonMetaboliteType#TranscribedGene TranscribedGene} and should not
   * be set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer left_pos;
  /**
   * The type of RNA that the species represents. This is a
   * mandatory attribute for species of type
   * {@link MEJsonMetaboliteType#TranscribedGene TranscribedGene} and should not
   * be set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  RNA_type;
  /**
   * The ending position of the nucleotide sequence on the genome. This is a
   * mandatory attribute for species of type
   * {@link MEJsonMetaboliteType#TranscribedGene TranscribedGene} and should not
   * be set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer right_pos;
  /**
   * The genome strand on which the nucleotide sequence is encoded. This is a
   * mandatory attribute for species of type "TranscribedGene" and should not be
   * set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  strand;
  /**
   * The nucleotide sequence of the species. This is a mandatory attribute for
   * species of type {@link MEJsonMetaboliteType#TranscribedGene
   * TranscribedGene} and should not be set on other types of species.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  nucleotide_sequence;


  public MEJsonMetaboliteTypeAttributes() {
    super();
  }


  // standard getters and setters
  @JsonSetter("unprocessed_protein_id")
  public void setUnprocessedProteinId(String unprocessed_protein_id) {
    this.unprocessed_protein_id = unprocessed_protein_id;
  }


  @JsonProperty("unprocessed_protein_id")
  public String getUnprocessedProteinId() {
    return unprocessed_protein_id;
  }


  @JsonSetter("left_pos")
  public void setLeftPos(int left_pos) {
    this.left_pos = left_pos;
  }


  @JsonProperty("left_pos")
  public Integer getLeftPos() {
    return left_pos;
  }


  @JsonSetter("right_pos")
  public void setRightPos(int right_pos) {
    this.right_pos = right_pos;
  }


  @JsonProperty("right_pos")
  public Integer getRightPos() {
    return right_pos;
  }


  @JsonSetter("RNA_type")
  public void setRNAType(String RNA_type) {
    this.RNA_type = RNA_type;
  }


  @JsonProperty("RNA_type")
  public String getRNAType() {
    return RNA_type;
  }


  public void setStrand(String Strand) {
    strand = Strand;
  }


  public String getStrand() {
    return strand;
  }


  @JsonSetter("nucleotide_sequence")
  public void setNucleotideSequence(String nucleotide_sequence) {
    this.nucleotide_sequence = nucleotide_sequence;
  }


  @JsonProperty("nucleotide_sequence")
  public String getNucleotideSequence() {
    return nucleotide_sequence;
  }
}
