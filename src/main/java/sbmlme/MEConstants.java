package sbmlme;

/**
 * @author Marc A. Voigt
 */
public interface MEConstants {

  public static final String ns                       =
    "http://github.com/SBRG/COBRAme/version1";
  public static final String prefix                   = "sbmlme";
  public static final String sbolURI                  =
    "http://github.com/SBRG/COBRAme/version1";
  public static final String key                      = "key";
  // General Attributes
  public static final String id                       = "id";
  public static final String name                     = "name";
  // needed for species and reactions
  public static final String sequence                 = "sequence";
  // needed for reactions and data
  public static final String keff                     = "keff";
  public static final String subreaction              = "subreaction";
  // Attributes for extended Species
  public static final String genomePos                = "genomePosition";
  public static final String unprocessed              = "unprocessedProteinId";
  public static final String speciesRef               = "meSpeciesReference";
  public static final String speciesPlugin            = "meSpeciesPlugin";
  // Attributes for extended Reactions
  public static final String dataId                   = "dataId";
  public static final String complexId                = "complexId";
  public static final String reverse                  = "reverse";
  public static final String codon                    = "codon";
  public static final String aminoAcid                = "aminoAcid";
  public static final String synthetase               = "synthetase";
  public static final String biomassType              = "biomassType";
  public static final String surfaceAreaInner         =
    "surfaceAreaInnerMembrane";
  public static final String surfaceAreaOuter         =
    "surfaceAreaOuterMembrane";
  public static final String aggregationPropensity    = "aggregationPropensity";
  public static final String propensityScaling        = "propensityScaling";
  public static final String variableKind             = "variableKind";
  public static final String reactionPlugin           = "meReactionPlugin";
  // reaction types
  public static final String summaryVariable          = "SummaryVariable";
  public static final String meReaction               = "MEReaction";
  public static final String genericFormation         =
    "GenericFormationReaction";
  public static final String transcription            = "TranscriptionReaction";
  public static final String translation              = "TranslationReaction";
  public static final String tRNACharging             = "tRNAChargingReaction";
  public static final String postTranslationReaction  =
    "PostTranslationReaction";
  public static final String complexFormationReaction =
    "ComplexFormationReaction";
  public static final String metabolicReaction        = "MetabolicReaction";
  // Attributes for elements of ListOfTranslocationReference for extended
  // Reactions
  public static final String translocation            = "translocation";
  public static final String multiplier               = "multiplier";
  // Attributes for elements of ListOfSubreactionReference for extended
  // Reactions and ListOfReactants/Products
  public static final String stoichiometry            = "stoichiometry";
  // Attributes for elements of ListOfEquilibriumConstants/RateConstants
  public static final String temperature              = "temperature";
  public static final String rate                     = "rate";
  // Attributes for data classes
  public static final String lengthDependent          = "lengthDependentEnergy";
  public static final String lowerFluxBound           = "lowerFluxBound";
  public static final String upperFluxBound           = "upperFluxBound";
  // Attributes for ListOfElementContribution & species reference in reactions
  public static final String element                  = "element";
  public static final String value                    = "value";
  // Attributes for ListOfEnzymeInformation
  public static final String enzymeRef                = "enzymeReference";
  public static final String fixedkeff                = "fixedKeff";
  // Attributes for ListOfSubreactions
  public static final String coefficient              = "coefficient";
  // Attributes for ListOfReactants/Products in data classes
  public static final String species                  = "species";
  // Values for SBOL
  public static final String sbolAnnotation           = "_seqAnnotation";
  public static final String sbolLoc                  = "_loc";
  public static final String mRNA                     = "mRNA";
  public static final String tRNA                     = "tRNA";
  public static final String rRNA                     = "rRNA";
  public static final String ncRNA                    = "ncRNA";
  public static final String dna                      = "DNA";
  public static final String protein                  = "Protein";
  public static final String seq                      = "_seq";
  public static final String versionOne               = "1.0";
  public static final String messengerRNA             = "messenger RNA";
  public static final String transferRNA              = "transfer RNA";
  public static final String noncodingRNA             = "non-coding RNA";
  public static final String ribosomalRNA             = "ribosomal RNA";
  public static final String dnaSegment               = "DNA segment";
  public static final String proteinComplex           = "protein complex";
}
