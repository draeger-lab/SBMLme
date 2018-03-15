package sbmlme.converter;

/**
 * Contains constants required for the conversion between the COBRAme JSON
 * schema and SBMLme schema.
 * 
 * @author Marc A. Voigt
 */
public interface MEJsonConstants {

  // COBRAme JSON fields
  public static final String reactionsField              = "reactions";
  public static final String metabolitesField            = "metabolites";
  public static final String processDataField            = "process_data";
  public static final String processDataType             = "process_data_type";
  // COBRAme JSON metabolite types
  public static final String metaboliteType              = "metabolite_type";
  public static final String transcribedGene             = "TranscribedGene";
  public static final String rnaPolymerase               = "RNAP";
  public static final String translatedGene              = "TranslatedGene";
  public static final String gentRNA                     = "GenerictRNA";
  public static final String processedProtein            = "ProcessedProtein";
  public static final String complex                     = "Complex";
  public static final String constraint                  = "Constraint";
  public static final String metabolite                  = "Metabolite";
  public static final String genericComponent            = "GenericComponent";
  public static final String ribosome                    = "Ribosome";
  // COBRAme JSON metabolite attributes
  public static final String compartment                 = "compartment";
  public static final String formula                     = "formula";
  public static final String nucleotide_sequence         =
    "nucleotide_sequence";
  public static final String rna_type                    = "RNA_type";
  public static final String strand                      = "strand";
  public static final String left_pos                    = "left_pos";
  public static final String right_pos                   = "right_pos";
  public static final String unprocessed_protein         =
    "unprocessed_protein_id";
  // COBRAme JSON reaction types
  public static final String reactionType                = "reaction_type";
  public static final String complexFormation            = "ComplexFormation";
  // COBRAme JSON reaction attributes
  public static final String objectiveCoefficient        =
    "objective_coefficient";
  public static final String variable_kind               = "variable_kind";
  // COBRAme ProcessDataTypes
  public static final String SubreactionData             = "SubreactionData";
  public static final String StoichiometricData          = "StoichiometricData";
  public static final String TranslocationData           = "TranslocationData";
  // COBRAme JSON ProcessData attributes
  public static final String subreactions                = "subreactions";
  public static final String stoichiometry_              = "_stoichiometry";
  public static final String lower_Bound                 = "lower_bound";
  public static final String upper_bound                 = "upper_bound";
  public static final String enzyme                      = "enzyme";
  public static final String element_contribution        =
    "element_contribution";
  public static final String enzyme_dict                 = "enzyme_dict";
  public static final String fixed_keff                  = "fixed_keff";
  public static final String lengthDep                   = "length_dependent";
  public static final String synthetaseKeff              = "synthetase_keff";
  public static final String amino_acid                  = "amino_acid";
  public static final String lengthDepEnergy             =
    "length_dependent_energy";
  public static final String stoichiometric_Data         =
    "stoichiometric_data";
  public static final String complex_data                = "complex_data";
  public static final String complex_id_                 = "_complex_id";
  public static final String complex_data_id             = "complex_data_id";
  public static final String complexData                 = "ComplexData";
  public static final String transcription_data          = "transcription_data";
  public static final String transcriptionData           = "TranscriptionData";
  public static final String translation_data            = "translation_data";
  public static final String translationData             = "TranslationData";
  public static final String tRNA_data                   = "tRNA_data";
  public static final String tRNAData                    = "tRNAData";
  public static final String posttranslation_data        =
    "posttranslation_data";
  public static final String posttranslationData         =
    "PostTranslationData";
  public static final String translocation_multiplier    =
    "translocation_multipliers";
  public static final String keq_folding                 = "keq_folding";
  public static final String k_folding                   = "k_folding";
  public static final String surface_area                = "surface_area";
  public static final String aggregation_propensity      =
    "aggregation_propensity";
  public static final String propensity_scaling          = "propensity_scaling";
  public static final String biomass_type                = "biomass_type";
  // SBMLme annotation
  public static final String processDataPlugin           = "meProcessData";
  public static final String listSubreactionReferences   =
    "listOfSubreactionReferences";
  public static final String subreactionRef              =
    "subreactionReference";
  public static final String listTranslocationReferences =
    "listOfTranslocationReferences";
  public static final String translocationRef            =
    "translocationReference";
  public static final String listEquilibrium             =
    "listOfEquilibriumConstants";
  public static final String rateConstant                = "RateConstant";
  public static final String listRateConstant            =
    "listOfRateConstants";
  public static final String saInner                     = "SA_inner_membrane";
  public static final String saOuter                     = "SA_outer_membrane";
  public static final String listOfSubreactionData       =
    "listOfSubreactionData";
  public static final String listOfTranslocationData     =
    "listOfTranslocationData";
  public static final String listOfStoichiometricData    =
    "listOfStoichiometricData";
  public static final String subreactionData             = "subreactionData";
  public static final String stoichiometricData          = "stoichiometricData";
  public static final String listOfEnzymeInformation     =
    "listOfEnzymeInformation";
  public static final String enzymeInformation           = "enzymeInformation";
  public static final String listOfElementContributions  =
    "listOfElementContributions";
  public static final String elementContribution         =
    "elementContribution";
  public static final String listOfReactants             = "listOfReactants";
  public static final String listOfProducts              = "listOfProducts";
  public static final String listOfSubreactions          = "listOfSubreactions";
  // COBRAme GlobalInfo
  public static final String globalInformation           = "GlobalInfo";
  public static final String global_Info                 = "global_info";
  // SBMLme altered Strings
  public static final String genericData                 = "genericData___";
  public static final String coefficientEnd              = "_coefficient";
}
