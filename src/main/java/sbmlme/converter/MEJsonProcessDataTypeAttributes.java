package sbmlme.converter;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * represent attributes of Processdata types in COBRAme
 * 
 * @author Marc A. Voigt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MEJsonProcessDataTypeAttributes {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            nucleotide_sequence;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               subreactions;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String>                      RNA_products;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            RNA_polymerase;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            protein;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            mRNA;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String>                      component_list;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               stoichiometry;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            complex_id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            synthetase_keff;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            synthetase;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            amino_acid;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            codon;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            RNA;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            unprocessed_protein_id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            processed_protein_id;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String                            biomass_type;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            aggregation_propensity;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            propensity_scaling;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               translocation_multipliers;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               keq_folding;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               k_folding;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               surface_area;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String>                      translocation;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            keff;
  // COBRAme accepts null/String/List<String>
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String>                      enzyme;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Integer>              element_contribution;
  // check if this works
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Map<String, Boolean>> enzyme_dict;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Boolean                           length_dependent_energy;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            lower_bound;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Double                            upper_bound;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, Double>               stoichiometry_;


  public MEJsonProcessDataTypeAttributes() {
    super();
  }


  @JsonProperty("nucleotide_sequence")
  public String getNucleotideSequence() {
    return nucleotide_sequence;
  }


  @JsonSetter("nucleotide_sequence")
  public void setNucleotideSequence(String value) {
    nucleotide_sequence = value;
  }


  @JsonProperty("subreactions")
  public Map<String, Double> getSubreactions() {
    return subreactions;
  }


  @JsonSetter("subreactions")
  public void setSubreactions(Map<String, Double> value) {
    subreactions = value;
  }


  @JsonProperty("RNA_products")
  public List<String> getRNAProducts() {
    return RNA_products;
  }


  @JsonSetter("RNA_products")
  public void setRNAProducts(List<String> value) {
    RNA_products = value;
  }


  @JsonProperty("RNA_polymerase")
  public String getRNA_polymerase() {
    return RNA_polymerase;
  }


  @JsonSetter("RNA_polymerase")
  public void setRNA_polymerase(String value) {
    RNA_polymerase = value;
  }


  @JsonProperty("protein")
  public String getProtein() {
    return protein;
  }


  @JsonSetter("protein")
  public void setProtein(String value) {
    protein = value;
  }


  @JsonProperty("mRNA")
  public String getmRNA() {
    return mRNA;
  }


  @JsonSetter("mRNA")
  public void setmRNA(String value) {
    mRNA = value;
  }


  @JsonProperty("component_list")
  public List<String> getComponent_list() {
    return component_list;
  }


  @JsonSetter("component_list")
  public void setComponent_list(List<String> value) {
    component_list = value;
  }


  @JsonProperty("stoichiometry")
  public Map<String, Double> getStoichiometry() {
    return stoichiometry;
  }


  @JsonSetter("stoichiometry")
  public void setStoichiometry(Map<String, Double> value) {
    stoichiometry = value;
  }


  @JsonProperty("complex_id")
  public String getComplex_id() {
    return complex_id;
  }


  @JsonSetter("complex_id")
  public void setComplex_id(String value) {
    complex_id = value;
  }


  @JsonProperty("synthetase_keff")
  public Double getSynthetase_keff() {
    return synthetase_keff;
  }


  @JsonSetter("synthetase_keff")
  public void setSynthetase_keff(double value) {
    synthetase_keff = value;
  }


  @JsonProperty("synthetase")
  public String getSynthetase() {
    return synthetase;
  }


  @JsonSetter("synthetase")
  public void setSynthetase(String value) {
    synthetase = value;
  }


  @JsonProperty("amino_acid")
  public String getAmino_acid() {
    return amino_acid;
  }


  @JsonSetter("amino_acid")
  public void setAmino_acid(String value) {
    amino_acid = value;
  }


  @JsonProperty("codon")
  public String getCodon() {
    return codon;
  }


  @JsonSetter("codon")
  public void setCodon(String value) {
    codon = value;
  }


  @JsonProperty("RNA")
  public String getRNA() {
    return RNA;
  }


  @JsonSetter("RNA")
  public void setRNA(String value) {
    RNA = value;
  }


  @JsonProperty("unprocessed_protein_id")
  public String getUnprocessed_protein_id() {
    return unprocessed_protein_id;
  }


  @JsonSetter("unprocessed_protein_id")
  public void setUnprocessed_protein_id(String value) {
    unprocessed_protein_id = value;
  }


  @JsonProperty("processed_protein_id")
  public String getProcessed_protein_id() {
    return processed_protein_id;
  }


  @JsonSetter("processed_protein_id")
  public void setProcessed_protein_id(String value) {
    processed_protein_id = value;
  }


  @JsonProperty("biomass_type")
  public String getBiomass_type() {
    return biomass_type;
  }


  @JsonSetter("biomass_type")
  public void setBiomass_type(String value) {
    biomass_type = value;
  }


  @JsonProperty("aggregation_propensity")
  public Double getAggregation_propensity() {
    return aggregation_propensity;
  }


  @JsonSetter("aggregation_propensity")
  public void setAggregation_propensity(double value) {
    aggregation_propensity = value;
  }


  @JsonProperty("propensity_scaling")
  public Double getPropensity_scaling() {
    return propensity_scaling;
  }


  @JsonSetter("propensity_scaling")
  public void setPropensity_scaling(double value) {
    propensity_scaling = value;
  }


  @JsonProperty("translocation_multipliers")
  public Map<String, Double> getTranslocation_multipliers() {
    return translocation_multipliers;
  }


  @JsonSetter("translocation_multipliers")
  public void setTranslocation_multipliers(Map<String, Double> value) {
    translocation_multipliers = value;
  }


  @JsonProperty("keq_folding")
  public Map<String, Double> getKeq_folding() {
    return keq_folding;
  }


  @JsonSetter("keq_folding")
  public void setKeq_folding(Map<String, Double> value) {
    keq_folding = value;
  }


  @JsonProperty("k_folding")
  public Map<String, Double> getK_folding() {
    return k_folding;
  }


  @JsonSetter("k_folding")
  public void setK_folding(Map<String, Double> value) {
    k_folding = value;
  }


  @JsonProperty("surface_area")
  public Map<String, Double> getSurface_area() {
    return surface_area;
  }


  @JsonSetter("surface_area")
  public void setSurface_area(Map<String, Double> value) {
    surface_area = value;
  }


  @JsonProperty("translocation")
  public List<String> getTranslocation() {
    return translocation;
  }


  @JsonSetter("translocation")
  public void setTranslocation(List<String> value) {
    translocation = value;
  }


  @JsonProperty("keff")
  public Double getKeff() {
    return keff;
  }


  @JsonSetter("keff")
  public void setKeff(double value) {
    keff = value;
  }


  @JsonProperty("enzyme")
  public List<String> getEnzyme() {
    return enzyme;
  }


  @JsonSetter("enzyme")
  public void setEnzyme(List<String> value) {
    enzyme = value;
  }


  @JsonProperty("element_contribution")
  public Map<String, Integer> getElement_contribution() {
    return element_contribution;
  }


  @JsonSetter("element_contribution")
  public void setElement_contribution(Map<String, Integer> value) {
    element_contribution = value;
  }


  @JsonProperty("enzyme_dict")
  public Map<String, Map<String, Boolean>> getEnzyme_dict() {
    return enzyme_dict;
  }


  @JsonSetter("enzyme_dict")
  public void setEnzyme_dict(Map<String, Map<String, Boolean>> value) {
    enzyme_dict = value;
  }


  @JsonProperty("length_dependent_energy")
  public Boolean getLength_dependent_energy() {
    return length_dependent_energy;
  }


  @JsonSetter("length_dependent_energy")
  public void setLength_dependent_energy(boolean value) {
    length_dependent_energy = value;
  }


  @JsonProperty("lower_bound")
  public Double getLower_bound() {
    return lower_bound;
  }


  @JsonSetter("lower_bound")
  public void setLower_bound(double value) {
    lower_bound = value;
  }


  @JsonProperty("upper_bound")
  public Double getUpper_bound() {
    return upper_bound;
  }


  @JsonSetter("upper_bound")
  public void setUpper_bound(double value) {
    upper_bound = value;
  }


  @JsonProperty("_stoichiometry")
  public Map<String, Double> get_stoichiometry() {
    return stoichiometry_;
  }


  @JsonSetter("_stoichiometry")
  public void set_stoichiometry(Map<String, Double> value) {
    stoichiometry_ = value;
  }
}
