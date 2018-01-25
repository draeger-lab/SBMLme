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
public class MESBOLPlugin {

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
    String seq, String role, OrientationType ori)
    throws SBOLValidationException {
    URI encoding = null;
    URI type = null;
    if ((role == "mRNA") || (role == "tRNA") || (role == "ncRNA")
      || (role == "rRNA")) {
      encoding = Sequence.IUPAC_RNA;
      type = ComponentDefinition.RNA;
    } else if (role == "DNA") {
      encoding = Sequence.IUPAC_DNA;
      type = ComponentDefinition.DNA;
    } else if (role == "Protein") {
      encoding = Sequence.IUPAC_PROTEIN;
      type = ComponentDefinition.PROTEIN;
    }
    sbol.createSequence(id + "_seq", "1.0", seq, encoding);
    sbol.createComponentDefinition(id, "1.0", type);
    sbol.getComponentDefinition(id, "1.0").addSequence(id + "_seq", "1.0");
    // create SBO URI for correct role
    SystemsBiologyOntology sbo = new SystemsBiologyOntology();
    if (role == "mRNA") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("messenger RNA"));
      // change role to other ontology
    } else if (role == "tRNA") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("transfer RNA"));
    } else if (role == "ncRNA") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("non-coding RNA"));
    } else if (role == "rRNA") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("ribosomal RNA"));
    } else if (role == "DNA") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("DNA segment"));
    } else if (role == "Protein") {
      sbol.getComponentDefinition(id, "1.0")
          .addRole(sbo.getURIbyName("protein complex"));
    }
    sbol.getComponentDefinition(id, "1.0").createSequenceAnnotation(
      id + "_seqAnnotation", id + "_loc", 1, seq.length(), ori);
  }


  /**
   * @param sbol
   * @param id
   * @param seq
   * @param role
   * @throws SBOLValidationException
   */
  public void createSBOL(SBOLDocument sbol, String id, String seq, String role)
    throws SBOLValidationException {
    URI encoding = null;
    URI type = null;
    if ((role == "mRNA") || (role == "tRNA") || (role == "ncRNA")
      || (role == "rRNA")) {
      encoding = Sequence.IUPAC_RNA;
      type = ComponentDefinition.RNA;
    } else if (role == "DNA") {
      encoding = Sequence.IUPAC_DNA;
      type = ComponentDefinition.DNA;
    } else if (role == "Protein") {
      encoding = Sequence.IUPAC_PROTEIN;
      type = ComponentDefinition.PROTEIN;
    }
    sbol.createSequence(id + "_seq", "1.0", seq, encoding);
    sbol.createComponentDefinition(id, "1.0", type);
    sbol.getComponentDefinition(id, "1.0").addSequence(id + "_seq", "1.0");
  }
}
