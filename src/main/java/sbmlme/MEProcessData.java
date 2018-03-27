package sbmlme;

import java.util.LinkedHashMap;
import java.util.List;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Implements methods for creating ProcessData objects for the annotation of the
 * SBML model. These objects contain data which is used in COBRAme in several
 * different reactions and is stored in its own objects to reduce redundancy.
 * <p>
 * In SBMLme only StoichiometricData, TranslocationData and SubreactionData are
 * stored in the MEProcessData. The other ProcessData objects from COBRAme are
 * directly added to the annotations of the specific reactions since they are in
 * a 1:1 relation with the reactions.
 * </p>
 * 
 * @author Marc A. Voigt
 */
public class MEProcessData extends MEAbstractXMLNodePlugin
  implements MEConstants {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;


  // constructors
  public MEProcessData() {
    super(new XMLTriple("meProcessData", ns, prefix), new XMLAttributes());
  }


  public MEProcessData(MEProcessData pd) {
    super(pd);
    if (pd.isSetId()) {
      setId(pd.getId());
    }
  }


  /**
   * create a MEProcessData object which is conform with the MEProcessData
   * adding functions by adding the MEProcessData lists in the correct order.
   * 
   * @return the MEProcessData object with the lists in the correct order
   */
  public MEProcessData createMEProcessData() {
    MEProcessData processData = new MEProcessData();
    processData.addChild(ListOfSubreactionData());
    processData.addChild(ListOfTranslocationData());
    processData.addChild(ListOfStoichiometricData());
    return processData;
  }


  /**
   * Create and add a new {@link SubreactionData} object to the list of
   * SubreactionData.
   * <p>
   * For correct function the given MEProcessData object needs to have the
   * list of SubreactionData as its first child.
   * </p>
   * 
   * @param processData
   *        the MEProcessData object that the subreaction should be added to
   * @param id
   *        the id of the new subreaction
   * @param keff
   *        the effective turnover rate of enzymes in the subreaction
   * @param enzymeReferences
   *        list with references to enzymes used in the subreaction, may be null
   * @param elementContributions
   *        map with elements and their net contributions to a macromolecule by
   *        the subreaction, may be null
   * @param speciesReferences
   *        list with ids of species used in the subreaction process
   * @param stoichiometries
   *        list with stoichiometries of species in the subreaction
   */
  public void addSubreactionData(MEProcessData processData, String id,
    double keff, List<String> enzymeReferences,
    LinkedHashMap<String, Integer> elementContributions,
    List<String> speciesReferences, List<Integer> stoichiometries) {
    SubreactionData nodeToBeAdded = new SubreactionData();
    // get first child of the MEProcessData object which should be the list of
    // SubreactionData
    processData.getChild(0).addChild(
      nodeToBeAdded.createSubreactionData(id, keff, enzymeReferences,
        elementContributions, speciesReferences, stoichiometries));
  }


  /**
   * Create and add a new {@link SubreactionData} object to the list of
   * TranslocationData.
   * <p>
   * For correct function the given MEProcessData object needs to have the
   * list of TranslocationData as its second child.
   * </p>
   * 
   * @param processData
   *        the MEProcessData object that the translocation should be added to
   * @param id
   *        the id of the new translocation
   * @param keff
   *        the effective turnover rate of enzymes in the translocation
   * @param enzymeReferences
   *        list with the ids of enzymes used in the translocation
   * @param fixedKeff
   *        list with information whether an enzyme coupling has a fixed keff in
   *        the translocation process
   * @param listLengthDependent
   *        list with information whether the enzyme coupling is length
   *        dependent in the translocation process
   * @param speciesReferences
   *        list with ids of species used in the translocation process
   * @param stoichiometries
   *        list with stoichiometries of species in the translocation process
   * @param lengthDependentEnergy
   *        whether the ATP cost of the translocation is dependent on the length
   *        of the protein
   */
  public void addTranslocationData(MEProcessData processData, String id,
    double keff, List<String> enzymeReferences, List<Boolean> fixedKeff,
    List<Boolean> listLengthDependent, List<String> speciesReferences,
    List<Double> stoichiometries, boolean lengthDependentEnergy) {
    SubreactionData nodeToBeAdded = new SubreactionData();
    // get second child of the MEProcessData object which should be the list of
    // TranslocationData
    processData.getChild(1)
               .addChild(nodeToBeAdded.createTranslocationData(id, keff,
                 enzymeReferences, fixedKeff, listLengthDependent,
                 speciesReferences, stoichiometries, lengthDependentEnergy));
  }


  /**
   * Create and add a new {@link StoichiometricData} object to the list of
   * StoichiometricData.
   * <p>
   * For correct function the given MEProcessData object needs to have the
   * list of StoichiometricData as its third child.
   * </p>
   * 
   * @param processData
   *        the MEProcessData object that the StoichiometricData object should
   *        be added to
   * @param id
   *        the id of the new StoichiometricData object
   * @param lowerFluxBound
   *        the lower flux bound of the metabolic reaction
   * @param upperFluxBound
   *        the upper flux bound of the metabolic reaction
   * @param subreactionMap
   *        the map with the ids and coefficients of the subreactions that take
   *        part in the reaction, may be null
   * @param speciesReferences
   *        list with ids of species used in the metabolic reaction
   * @param stoichiometries
   *        list with stoichiometries of species in the metabolic reaction
   */
  public void addStoichiometricData(MEProcessData processData, String id,
    Double lowerFluxBound, Double upperFluxBound,
    LinkedHashMap<String, Double> subreactionMap,
    List<String> speciesReferences, List<Double> stoichiometries) {
    StoichiometricData nodeToBeAdded = new StoichiometricData();
    // get third child of the MEProcessData object which should be the list of
    // StoichiometricData
    processData.getChild(2)
               .addChild(nodeToBeAdded.createStoichiometricData(id,
                 lowerFluxBound, upperFluxBound, subreactionMap,
                 speciesReferences, stoichiometries));
  }


  // ListOf for different subtypes of ProcessData
  /**
   * Create an empty list of {@link SubreactionData} for the SubreactionData.
   * 
   * @return the {@link XMLNode} with the list of SubreactionData
   */
  public XMLNode ListOfSubreactionData() {
    XMLNode listOf = new XMLNode(
      new XMLTriple("listOfSubreactionData", ns, prefix), new XMLAttributes());
    return listOf;
  }


  /**
   * Create an empty list of {@link SubreactionData} for the TranslocationData.
   * 
   * @return the {@link XMLNode} with the list of TranslocationData
   */
  public XMLNode ListOfTranslocationData() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfTranslocationData", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  /**
   * Create an empty list of {@link StoichiometricData}.
   * 
   * @return the {@link XMLNode} with the list of StoichiometricData
   */
  public XMLNode ListOfStoichiometricData() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("listOfStoichiometricData", ns, prefix),
        new XMLAttributes());
    return listOf;
  }
}
