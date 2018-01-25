import java.util.List;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
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
    super(new XMLTriple("MEProcessData", ns, prefix), new XMLAttributes());
  }


  public MEProcessData(MEProcessData pd) {
    super(pd);
    if (pd.isSetId()) {
      setId(pd.getId());
    }
  }


  /**
   * create a MEProcessData object which is conform with the MEProcessData
   * adding functions
   */
  public MEProcessData createMEProcessData() {
    MEProcessData processData = new MEProcessData();
    processData.addChild(ListOfSubreactionData());
    processData.addChild(ListOfTranslocationData());
    processData.addChild(ListOfStoichiometricData());
    return processData;
  }


  /**
   * create and add a new SubreactionData object to the list of SubreactionData
   */
  public void addSubreactionData(MEProcessData processData, String id,
    double keff, List<String> enzymeReferences, List<String> speciesReferences,
    List<Integer> stoichiometries) {
    SubreactionData nodeToBeAdded = new SubreactionData();
    // get first child of the MEProcessData object which should be the list of
    // SubreactionData
    processData.getChild(0).addChild(nodeToBeAdded.createSubreactionData(id,
      keff, enzymeReferences, speciesReferences, stoichiometries));
  }


  /**
   * create and add a new SubreactionData object to the list of SubreactionData
   */
  public void addSubreactionData(MEProcessData processData, String id,
    double keff, List<String> enzymeReferences, List<String> elements,
    List<Integer> contribution, List<String> speciesReferences,
    List<Integer> stoichiometries) {
    SubreactionData nodeToBeAdded = new SubreactionData();
    // get first child of the MEProcessData object which should be the list of
    // SubreactionData
    processData.getChild(0).addChild(
      nodeToBeAdded.createSubreactionData(id, keff, enzymeReferences, elements,
        contribution, speciesReferences, stoichiometries));
  }


  /**
   * create and add a new TranslocationData object to the list of
   * TranslocationnData
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
   * create and add a new StoichiometricData object to the list of
   * StoichiometricData
   */
  public void addStoichiometricData(MEProcessData processData, String id,
    Double lowerFluxBound, Double upperFluxBound,
    List<String> listSubreactionReferences, List<Double> listCoefficients,
    List<String> speciesReferences, List<Double> stoichiometries) {
    StoichiometricData nodeToBeAdded = new StoichiometricData();
    // get third child of the MEProcessData object which should be the list of
    // StoichiometricData
    processData.getChild(2)
               .addChild(nodeToBeAdded.createStoichiometricData(id,
                 lowerFluxBound, upperFluxBound, listSubreactionReferences,
                 listCoefficients, speciesReferences, stoichiometries));
  }


  // ListOf for different subtypes of ProcessData
  public XMLNode ListOfSubreactionData() {
    XMLNode listOf = new XMLNode(
      new XMLTriple("ListOfSubreactionData", ns, prefix), new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfTranslocationData() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfTranslocationData", ns, prefix),
        new XMLAttributes());
    return listOf;
  }


  public XMLNode ListOfStoichiometricData() {
    XMLNode listOf =
      new XMLNode(new XMLTriple("ListOfStoichiometricData", ns, prefix),
        new XMLAttributes());
    return listOf;
  }
}
