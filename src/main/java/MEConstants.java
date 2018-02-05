/**
 * @author Marc A. Voigt
 */
public interface MEConstants {

  public static final String ns                    =
    "http://cobramens.url/sbml";
  public static final String prefix                = "cobrame";
  public static final String sbolURI               =
    "http://cobramens.url/sbol/";
  public static final String key                   = "key";
  // General Attributes
  public static final String id                    = "id";
  public static final String name                  = "name";
  // needed for species and reactions
  public static final String sequence              = "sequence";
  // needed for reactions and data
  public static final String keff                  = "keff";
  public static final String subreaction           = "subreaction";
  // Attributes for extended Species
  public static final String genomePos             = "genomePosition";
  public static final String unprocessed           = "unprocessedProteinId";
  // Attributes for extended Reactions
  public static final String dataId                = "dataId";
  public static final String complexId             = "complexId";
  public static final String stoichiometricRef     = "stoichiometricDataId";
  public static final String reverse               = "reverse";
  public static final String codon                 = "codon";
  public static final String synthetase            = "synthetase";
  public static final String surfaceAreaInner      = "surfaceAreaInnerMembrane";
  public static final String surfaceAreaOuter      = "surfaceAreaOuterMembrane";
  public static final String aggregationPropensity = "aggregationPropensity";
  public static final String propensityScaling     = "propensityScaling";
  public static final String variableKind          = "variableKind";
  // Attributes for elements of ListOfTranslocationReference for extended
  // Reactions
  public static final String translocation         = "translocation";
  public static final String multiplier            = "multiplier";
  // Attributes for elements of ListOfSubreactionReference for extended
  // Reactions and ListOfReactants/Products
  public static final String stoichiometry         = "stoichiometry";
  // Attributes for elements of ListOfEquilibriumConstants/RateConstants
  public static final String temperature           = "temperature";
  public static final String rate                  = "rate";
  // Attributes for data classes
  public static final String lengthDependent       = "lengthDependentEnergy";
  public static final String lowerFluxBound        = "lowerFluxBound";
  public static final String upperFluxBound        = "upperFluxBound";
  // Attributes for ListOfElementContribution & species reference in reactions
  public static final String element               = "element";
  public static final String value                 = "value";
  // Attributes for ListOfEnzymeInformation
  public static final String enzymeRef             = "enzymeReference";
  public static final String fixedkeff             = "fixedKeff";
  // Attributes for ListOfSubreactions
  public static final String coefficient           = "coefficient";
  // Attributes for ListOfReactants/Products in data classes
  public static final String species               = "species";
}
