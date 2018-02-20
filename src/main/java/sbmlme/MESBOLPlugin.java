package sbmlme;
import java.net.URI;

import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.OrientationType;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;
import org.sbolstandard.core2.SystemsBiologyOntology;

/**
 * @author Marc A. Voigt
 */
public class MESBOLPlugin implements MEConstants {

  public MESBOLPlugin() {
  }


  /**
   * @param sbol
   * @param id
   * @param seq
   * @param role
   * @param ori
   * @throws SBOLValidationException
   */
  public void createSBOLSequenceWithAnnotation(SBOLDocument sbol, String id,
    String sequence, String role, OrientationType ori)
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
    sbol.getComponentDefinition(id, versionOne).createSequenceAnnotation(
      id + sbolAnnotation, id + sbolLoc, 1, seq.length(), ori);
  }


  /**
   * @param sbol
   * @param id
   * @param seq
   * @param role
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
