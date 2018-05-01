package sbmlme;

import java.net.URI;

import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SystemsBiologyOntology;

/**
 * implements methods to add sequences with additional information to an
 * external SBOL document. This class is intended to reduce the file size of the
 * SBML model while ensuring that the resulting SBOL document follows a schema
 * that can be interpreted by the SBMLme converter.
 * 
 * @author Marc A. Voigt
 */
public class MESBOLPlugin implements MEConstants {

  public MESBOLPlugin() {
  }


  /**
   * Create and add a sequence with type and position specific data to the SBOL
   * document.
   * <p>
   * This method creates a new sequence object in the SBOL document and then
   * adds this to a newly created ComponentDefinition. A SequenceAnnotation
   * object will be created with position specific data.
   * </p>
   * 
   * @param sbol
   *        the SBOL document
   * @param id
   *        the id of the reaction/species that the sequence belongs to
   * @param sequence
   *        the nucleotide sequence
   * @param role
   *        the type of molecule that the sequence encodes, e.g. "mRNA",
   *        "DNA",...
   * @param ori
   *        the orientation of the sequence on the genome
   * @param left
   *        start position of the sequence in the genome
   * @param right
   *        end position of the sequence in the genome
   * @throws SBOLValidationException
   */
  public void createSBOLSequenceWithAnnotation(SBOLDocument sbol, String id,
    String sequence, String role, OrientationType ori, int left, int right)
    throws SBOLValidationException {
    URI encoding = null;
    URI type = null;
    if ((role.equals(mRNA)) || (role.equals(tRNA)) || (role.equals(ncRNA))
      || (role.equals(rRNA))) {
      encoding = Sequence.IUPAC_RNA;
      type = ComponentDefinition.RNA;
    } else if (role.equals(dna)) {
      encoding = Sequence.IUPAC_DNA;
      type = ComponentDefinition.DNA;
    } else if (role.equals(protein)) {
      encoding = Sequence.IUPAC_PROTEIN;
      type = ComponentDefinition.PROTEIN;
    }
    sbol.createSequence(id + seq, versionOne, sequence, encoding);
    sbol.createComponentDefinition(id, versionOne, type);
    sbol.getComponentDefinition(id, versionOne).addSequence(id + seq,
      versionOne);
    // create SBO URI for correct role
    SystemsBiologyOntology sbo = new SystemsBiologyOntology();
    if (role.equals(mRNA)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(messengerRNA));
      // change role to other ontology
    } else if (role.equals(tRNA)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(transferRNA));
    } else if (role.equals(ncRNA)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(noncodingRNA));
    } else if (role.equals(rRNA)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(ribosomalRNA));
    } else if (role.equals(dna)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(dnaSegment));
    } else if (role.equals(protein)) {
      sbol.getComponentDefinition(id, versionOne)
          .addRole(sbo.getURIbyName(proteinComplex));
    }
    // COBRAme allows null values for left and and right, these are invalid in
    // SBOL and need to be changed
    if (right == 0) {
      left = 1;
      right = sequence.length();
    }
    if (ori != null) {
      sbol.getComponentDefinition(id, versionOne).createSequenceAnnotation(
        id + sbolAnnotation, id + sbolLoc, 1, right - left, ori);
    } else {
      sbol.getComponentDefinition(id, versionOne).createSequenceAnnotation(
        id + sbolAnnotation, id + sbolLoc, 1, right - left);
    }
  }


  /**
   * Create and add a sequence to the SBOL document.
   * <p>
   * This method creates a new sequence object in the SBOL document and then
   * adds this to a newly created ComponentDefinition. This method is intended
   * to be used for adding sequence information that does not require position
   * or type specific data.
   * </p>
   * 
   * @param sbol
   *        the SBOL document
   * @param id
   *        the id of the reaction/species that the sequence belongs to
   * @param sequence
   *        the nucleotide sequence
   * @param role
   *        the type of molecule that the sequence encodes, e.g. "mRNA",
   *        "DNA",...
   * @throws SBOLValidationException
   */
  public void createSBOL(SBOLDocument sbol, String id, String sequence,
    String role) throws SBOLValidationException {
    URI encoding = null;
    URI type = null;
    if ((role.equals(mRNA)) || (role.equals(tRNA)) || (role.equals(ncRNA))
      || (role.equals(rRNA))) {
      encoding = Sequence.IUPAC_RNA;
      type = ComponentDefinition.RNA;
    } else if (role.equals(dna)) {
      encoding = Sequence.IUPAC_DNA;
      type = ComponentDefinition.DNA;
    } else if (role.equals(protein)) {
      encoding = Sequence.IUPAC_PROTEIN;
      type = ComponentDefinition.PROTEIN;
    }
    sbol.createSequence(id + seq, versionOne, sequence, encoding);
    sbol.createComponentDefinition(id, versionOne, type);
    sbol.getComponentDefinition(id, versionOne).addSequence(id + seq,
      versionOne);
  }
}
