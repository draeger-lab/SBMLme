package sbmlme.converter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent attributes of different metabolite types
 * 
 * @author Marc A. Voigt
 */
@JsonPropertyOrder({"left_pos", "RNA_type", "right_pos", "strand",
  "nucleotide_sequence"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonMetaboliteTypeAttributes {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  unprocessed_protein_id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer left_pos;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  RNA_type;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer right_pos;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String  strand;
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
