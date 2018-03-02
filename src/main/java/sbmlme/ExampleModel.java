package sbmlme;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.TidySBMLWriter;
import org.sbml.jsbml.ext.fbc.FBCConstants;
import org.sbml.jsbml.ext.fbc.FBCModelPlugin;
import org.sbml.jsbml.ext.fbc.Objective;
import org.sbml.jsbml.ext.groups.GroupsConstants;
import org.sbml.jsbml.ext.groups.GroupsModelPlugin;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;

import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

/**
 * @author Marc A. Voigt
 */
@SuppressWarnings("restriction")
public class ExampleModel implements MEConstants {

  /**
   * @param args
   * @throws SBMLException
   * @throws XMLStreamException
   * @throws SBOLConversionException
   * @throws SBOLValidationException
   * @throws NumberFormatException
   * @throws ParseException
   * @throws IOException
   * @throws JDOMException
   * @throws java.text.ParseException
   * @throws CombineArchiveException
   * @throws URISyntaxException
   * @throws TransformerException
   */
  public static void main(String[] args)
    throws SBMLException, XMLStreamException, SBOLConversionException,
    SBOLValidationException, NumberFormatException, ParseException, IOException,
    JDOMException, java.text.ParseException, CombineArchiveException,
    URISyntaxException, TransformerException {
    new ExampleModel();
  }


  /**
   * create example model and write content to file
   * the example model is very incomplete and only shows how each kind of
   * Species/Reaction/ProcessData will be created and represented in SBML/SBOL
   * 
   * @throws SBMLException
   * @throws XMLStreamException
   * @throws SBOLConversionException
   * @throws SBOLValidationException
   * @throws NumberFormatException
   * @throws ParseException
   * @throws IOException
   * @throws java.text.ParseException
   * @throws CombineArchiveException
   * @throws JDOMException
   * @throws URISyntaxException
   * @throws TransformerException
   */
  public ExampleModel() throws SBMLException, XMLStreamException,
    SBOLConversionException, SBOLValidationException, NumberFormatException,
    ParseException, IOException, JDOMException, java.text.ParseException,
    CombineArchiveException, URISyntaxException, TransformerException {
    // create new SBML and SBOL documents and set their namespaces
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.addDeclaredNamespace(prefix, ns);
    SBOLDocument sbol = new SBOLDocument();
    sbol.setDefaultURIprefix("http://cobramens.url/sbol/");
    // Create a new SBML model, and add compartments to it.
    Model model = doc.createModel("example_model");
    model.initDefaults(2, 4);
    // create basic objective
    // Objective for flux coefficient
    FBCModelPlugin fbcModel =
      (FBCModelPlugin) model.getPlugin(FBCConstants.shortLabel);
    // needs to be false due to InitialAssignments for SpeciesReferences
    fbcModel.setStrict(false);
    Objective objective =
      fbcModel.createObjective("objOne", Objective.Type.MAXIMIZE);
    fbcModel.setActiveObjective(objective);
    // basic groups package Implementation
    GroupsModelPlugin groups =
      (GroupsModelPlugin) model.getPlugin(GroupsConstants.shortLabel);
    // add species to model
    MESpeciesPlugin meSpeciesPlugin = new MESpeciesPlugin();
    meSpeciesPlugin.createMESpecies(model, "biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "protein_biomass", "", "", "c",
      groups, "constraint");
    meSpeciesPlugin.createMESpecies(model, "mRNA_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "tRNA_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "rRNA_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "ncRNA_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "DNA_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "lipid_biomass", "", "", "c", groups,
      "constraint");
    meSpeciesPlugin.createMESpecies(model, "constituent_biomass", "", "", "c",
      groups, "constraint");
    meSpeciesPlugin.createMESpecies(model, "prosthetic_group_biomass", "", "",
      "c", groups, "constraint");
    meSpeciesPlugin.createMESpecies(model, "peptidoglycan_biomass", "", "", "c",
      groups, "constraint");
    meSpeciesPlugin.createMESpecies(model, "unmodeled_protein_biomass", "", "",
      "c", groups, "constraint");
    // forced to add string for turning COBRAme id in SBML conform id
    meSpeciesPlugin.createMESpecies(model, "SBML__" + "10fthf_c", "C20H21N7O7",
      "10-Formyltetrahydrofolate", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "adp_c", "C10H12N5O10P2", "ADP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ade_c", "C5H5N5", "Adenine", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ade_p", "C5H5N5", "Adenine", "p",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ahcys_c", "C14H20N6O5S",
      "S-Adenosyl-L-homocysteine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "amet_c", "C15H23N6O5S",
      "S-Adenosyl-L-methionine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ala__L_c", "C3H7NO2", "L-Alanine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "amp_c", "C10H12N5O7P", "AMP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "arg__L_c", "C6H15N4O2",
      "L-Arginine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "asn__L_c", "C4H8N2O3",
      "L-Asparagine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "Asn_RS_dim",
      "C4698H7210N1284O1396S26", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "atp_c", "C10H12N5O13P3", "ATP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "cmp_c", "C9H12N3O8P", "CMP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ctp_c", "C9H12N3O14P3", "CTP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "datp_c", "C10H12N5O12P3", "dATP",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "dgtp_c", "C10H12N5O13P3", "dGTP",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "dttp_c", "C10H13N2O14P3", "dTTP",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "dctp_c", "C9H12N3O13P3", "dCTP",
      "c", groups, "metabolite");
    // forced to change id due to :
    meSpeciesPlugin.createMESpecies(model,
      "Def_mono_mod_1:fe2".replace(":", "__SBML__"), "C844FeH1394N241O255S6",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "FusA_mono",
      "C3429H5418N942O1049S25", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "Fmt_mono_mod_mg2_mod_k",
      "C1517H2442KMgN414O450S11", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "for_c", "CH1O2", "Formate", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "gdp_c", "C10H12N5O11P2", "GDP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "generic_RF", "", "", "c", groups,
      "generic");
    meSpeciesPlugin.createMESpecies(model, "generic_Dus", "", "", "c", groups,
      "generic");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_AAA_lys__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_AAC_asn__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_AAG_lys__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_AAU_asn__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_ACC_thr__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_ACU_thr__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_AUG_met__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CAA_gln__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CAC_his__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CAG_gln__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CCG_pro__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CGU_arg__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CGC_arg__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_CUG_leu__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GCA_ala__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GCU_ala__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GGC_gly_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GGU_gly_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GUA_val__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_GUU_val__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_START_met__L_c", "",
      "", "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_UCU_ser__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_UGA_cys__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_UUC_phe__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_tRNA_UUU_phe__L_c", "", "",
      "c", groups, "tRNA");
    meSpeciesPlugin.createMESpecies(model, "generic_Tuf", "", "", "c", groups,
      "generic");
    meSpeciesPlugin.createMESpecies(model, "gln__L_c", "C5H10N2O3",
      "L-Glutamine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "gly_c", "C2H5NO2", "Glycine", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "gmp_c", "C10H12N5O8P", "GMP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "gtp_c", "C10H12N5O14P3", "GTP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "gua_c", "C5H5N5O", "Guanine", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "GreA_mono", "C772H1241N215O248S4",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "GreB_mono", "C833H1304N228O245S3",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "h_c", "H", "H+", "c", groups,
      "metabolite");
    meSpeciesPlugin.createMESpecies(model, "h_p", "H", "H+", "p", groups,
      "metabolite");
    meSpeciesPlugin.createMESpecies(model, "h2o_c", "H2O", "H2O", "c", groups,
      "metabolite");
    meSpeciesPlugin.createMESpecies(model, "hco3_c", "CHO3", "Bicarbonate", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "his__L_c", "C6H9N3O2",
      "L-Histidine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "InfA_mono", "C356H589N103O107S3",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "InfB_mono",
      "C4180H6891N1264O1357S24", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "InfC_mono", "C897H1507N263O267S6",
      "", "c", groups, "complex");
    // forced to replace id due to :
    meSpeciesPlugin.createMESpecies(model,
      "IscS_mod_2:pydx5p".replaceAll(":", "__SBML__"),
      "C3980H6334N1130O1210P2S38", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model,
      "IscS_mod_2:pydx5p_mod_1:SH".replaceAll(":", "__SBML__"),
      "C3980H6334N1130O1210P2S39", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "leu__L_c", "C6H13NO2", "L-Leucine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "lys__L_c", "C6H15N2O2", "L-Lysine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "met__L_c", "C5H11NO2S",
      "L-Methionine", "c", groups, "metabolite");
    // forced to replace id due to :
    meSpeciesPlugin.createMESpecies(model,
      "Mfd_mono_mod_1:mg2".replaceAll(":", "__SBML__"),
      "C5779H9165MgN1640O1697S37", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "monocistronic_excision_machinery",
      "C24670H38124Mg2N7775O9308P377S75Zn2", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "nadp_c", "C21H25N7O17P3",
      "Nicotinamide adenine dinucleotide phosphate", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "nadph_c", "C21H26N7O17P3",
      "Nicotinamide adenine dinucleotide phosphate - reduced", "c", groups,
      "metabolite");
    meSpeciesPlugin.createMESpecies(model, "NusA_mono", "C2396H3836N669O775S13",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "NusB_mono", "C705H1130N191O207S3",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "NusG_mono", "C908H1430N255O266S7",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "phe__L_c", "C9H11NO2",
      "L-Phenylalanine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "pi_c", "HO4P", "Phosphate", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ppi_c", "HO7P2", "Diphosphate", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "preq1_c", "C7H10N5O",
      "7-aminomethyl-7-deazaguanine", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "PrfA_mono", "C1737H2775N526O564S14",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "PrfB_mono", "C1783H2797N507O596S11",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "PrfC_mono", "C2637H4165N727O792S22",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "pro__L_c", "C5H9NO2", "L-Proline",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "protein_b0002", "C228H419N90O57S2",
      "", "c", groups, "translated");
    meSpeciesPlugin.createMESpecies(model, "protein_b0002_Inner_Membrane",
      "C228H419N90O57S2", "", "c", groups, "processed");
    meSpeciesPlugin.createMESpecies(model, "QueA_mono", "C1767H2742N472O529S11",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "QueG_mono", "C1929H3065N539O543S18",
      "", "c", groups, "complex");
    // replace id due to -
    meSpeciesPlugin.createMESpecies(model,
      "RED-THIOREDOXIN-MONOMER".replace("-", "_"), "C528H833N132O159S3", "",
      "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model,
      "RED-THIOREDOXIN-MONOMER_mod_Oxidized".replace("-", "_"),
      "C528H831N132O159S3", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "ribosome",
      "C38487H64147Mg171N11545O11295P3S201", "", "c", groups, "Ribosome");
    // replace id due to :
    meSpeciesPlugin.createMESpecies(model,
      "Rho_hexa_mod_3:mg2".replaceAll(":", "__SBML__"),
      "C12444H20268Mg3N3516O3726S102", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "RpmH_mono", "C228H419N90O57S2", "",
      "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model,
      "RpoZ_mono_mod_1:mg2".replaceAll(":", "__SBML__"), "C430H722MgN136O142S",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESequenceSpecies(model, "RNA_b0001", "", "c",
      "C722H820N288O528P76", sbol,
      "TCCTCTGTAGTTCAGTCGGTAGAACGGCGGACTGTTAATCCGTATGTCACTGGTTCGAGTCCAGTCAGAGGAGCCA",
      "tRNA", "+", 2042572, groups, "transcribed");
    meSpeciesPlugin.createMESequenceSpecies(model, "RNA_b0002", "", "c",
      "C1346H1521N547O979P141", sbol,
      "ATGAAACGCACTTTTCAACCGTCTGTACTGAAGCGCAACCGTTCTCACGGCTTCCGTGCTCGTATGGCTACTAAAAATGGTCGTCAGGTTCTGGCACGTCGTCGTGCTAAAGGCCGCGCTCGTCTGACCGTTTCTAAGTAA",
      "mRNA", "+", 3882358, groups, "transcribed");
    meSpeciesPlugin.createMESequenceSpecies(model, "RNA_b0003", "", "c",
      "C722H820N288O528P76", sbol,
      "GTGGTTAAGCTCGCATTTCCCAGGGAGTTACGCTTGTTAACTCCCAGTCAATTCACATTCGTCTTCCAGCAGCCACAACGGGCTGGCACGCCGCAAATTACCATTCTCGGCCGCCTGAATTCGCTGGGGCATCCCCGTATCGGTCTTACAGTCGCCAAGAAAAACGTTCGACGCGCCCATGAACGCAATCGGATTAAACGTCTGACGCGTGAAAGCTTCCGTCTGCGCCAACATGAACTCCCGGCTATGGATTTCGTGGTGGTGGCGAAAAAAGGGGTTGCCGACCTCGATAACCGTGCTCTCTCGGAAGCGTTGGAAAAATTATGGCGCCGCCACTGTCGCCTGGCTCGCGGGTCCTGA",
      "mRNA", "+", 3882515, groups, "transcribed");
    meSpeciesPlugin.createMESpecies(model, "RNA_degradosome",
      "C40683H65406Mg6N11875O12685S200Zn2", "", "c", groups, "complex");
    // replace id due to -
    meSpeciesPlugin.createMESpecies(model, "RNAP70-CPLX".replace("-", "_"),
      "C19713H31813Mg2N5539O6126S141Zn", "", "c", groups, "RNAP");
    meSpeciesPlugin.createMESpecies(model, "RplC_mono", "C979H1617N288O294S4",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "RplD_mono", "C974H1620N283O290S5",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "RpsD_mono", "C1026H1710N315O298S4",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "RpsJ_mono", "C514H866N158O151S2",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "RplM_mono", "C714H1163N212O199S4",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "Rrf_mono", "C888H1494N263O287S6",
      "", "c", groups, "complex");
    // replace id due to -
    meSpeciesPlugin.createMESpecies(model, "Sec-CPLX".replace("-", "_"),
      "C7955H12880N2101O2234S58", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "selnp_c", "H2O3PSe",
      "Selenophosphate", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "ser__L_c", "C3H7NO3", "L-Serine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "SRP-CPLX".replace("-", "_"),
      "C3258H4875N1059O1439P114S29", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model,
      "Tgt_hexa_mod_6:zn2".replaceAll(":", "__SBML__"),
      "C11316H17628N3186O3312S132Zn6", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "ThiI_mono", "C2453H3913N686O719S14",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "thf_c", "C19H21N7O6",
      "5,6,7,8-Tetrahydrofolate", "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "thr__L_c", "C4H9NO3", "L-Threonine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "TrmA_mono", "C1859H2925N517O554S18",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "TruA_dim", "C2720H4222N782O772S18",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "TruB_mono", "C1545H2485N439O470S11",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "Tsf_mono", "C1331H2170N363O417S11",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "ump_c", "C9H11N2O9P", "UMP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "utp_c", "C9H11N2O15P3", "UTP", "c",
      groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "val__L_c", "C5H11NO2", "L-Valine",
      "c", groups, "metabolite");
    meSpeciesPlugin.createMESpecies(model, "YggH_mono", "C1215H1886N348O343S14",
      "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "YICE-MONOMER".replace("-", "_"),
      "C2231H3611N562O612S24", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "YidC_MONOMER",
      "C2805H4331N715O795S23", "", "c", groups, "complex");
    meSpeciesPlugin.createMESpecies(model, "YrdC_mono", "C926H1464N254O276S6",
      "", "c", groups, "complex");
    // create ProcessData
    MEProcessData meProcessData = new MEProcessData();
    meProcessData = meProcessData.createMEProcessData();
    meProcessData.addTranslocationData(meProcessData, "srp_yidC_translocation",
      20.0, Arrays.asList("SRP_CPLX", "Sec_CPLX", "YidC_MONOMER"),
      Arrays.asList(false, false, false), Arrays.asList(true, true, true),
      Arrays.asList("gtp_c", "h2o_c", "pi_c", "gdp_c", "h_c"),
      Arrays.asList(-1.0, -1.0, 1.0, 1.0, 1.0), false);
    meProcessData.addStoichiometricData(meProcessData, "ADEt2rpp", -1000.0,
      1000.0, null, null, Arrays.asList("ade_p", "ade_c", "h_c", "h_p"),
      Arrays.asList(-1.0, 1.0, 1.0, -1.0));
    meProcessData.addSubreactionData(meProcessData, "met_addition_at_AUG", 65.0,
      Arrays.asList("generic_TUF"), null, null,
      Arrays.asList("generic_tRNA_AUG_met__L_c", "h2o_c", "gdp_c", "atp_c",
        "ppi_c", "gtp_c", "h_c", "pi_c", "amp_c"),
      Arrays.asList(-1, -2, 1, -1, 1, -1, 2, 1, 1));
    meProcessData.addSubreactionData(meProcessData, "sec_addition_at_UGA", 65.0,
      Arrays.asList("SelA_deca_mod_10:pydx5p", "SelB_mono"),
      Arrays.asList("O", "Se"), Arrays.asList(-1, 1),
      Arrays.asList("pi_c", "h_c", "generic_tRNA_UGA_cys__L_c", "selnp_c"),
      Arrays.asList(1, 1, -1, -1));
    // create ME Reactions
    MEReactionPlugin meReactionPlugin = new MEReactionPlugin();
    meReactionPlugin.createTranscriptionReaction(model, sbol, groups, objective,
      "transcription_TU0001_from_RpoD_mono", "", "TU0001_from_RpoD_mono",
      Double.valueOf("1000.00000000000"), Double.valueOf("0.0"),
      Arrays.asList("utp_c", "RNA_degradosome", "NusG_mono", "h2o_c",
        "tRNA_biomass", "RpsJ_mono", "RNA_b0001", "h_c", "NusB_mono", "ctp_c",
        "RplD_mono", "ump_c", "RpsD_mono", "atp_c", "cmp_c", "RplC_mono",
        "RNAP70_CPLX", "GreA_mono", "adp_c", "gmp_c",
        "Mfd_mono_mod_1__SBML__mg2", "Rho_hexa_mod_3__SBML__mg2",
        "RpoZ_mono_mod_1__SBML__mg2", "amp_c", "NusA_mono",
        "monocistronic_excision_machinery", "ppi_c", "RplM_mono", "gtp_c",
        "pi_c", "GreB_mono"),
      Arrays.asList("-19", "-8.54700854700855e-6*mu", "-4.27350427350427e-6*mu",
        "-13.0", "24.334014", "-4.27350427350427e-6*mu", "1", "13.0",
        "-4.27350427350427e-6*mu", "-23", "-4.27350427350427e-6*mu", "3",
        "-4.27350427350427e-6*mu", "-26.0", "1", "-4.27350427350427e-6*mu",
        "-0.00040142540337998*mu - 0.000157158045423262",
        "-4.27350427350427e-6*mu", "5.0", "3", "-4.27350427350427e-6*mu",
        "-4.27350427350427e-6*mu", "-4.27350427350427e-6*mu", "1",
        "-4.27350427350427e-6*mu", "-8.54700854700855e-6*mu", "84",
        "-4.27350427350427e-6*mu", "-21", "5.0", "-4.27350427350427e-6*mu"),
      0.0, "continuous",
      "CACACGATTCCTCTGTAGTTCAGTCGGTAGAACGGCGGACTGTTAATCCGTATGTCACTGGTTCGAGTCCAGTCAGAGGAGCCA",
      Arrays.asList("monocistronic_excision",
        "Transcription_stable_rho_dependent", "RNA_degradation_machine",
        "RNA_degradation_atp_requirement"),
      Arrays.asList(2, 1, 2, 8));
    meReactionPlugin.createMEReaction(model, groups, objective, "DM_RNA_b0001",
      "", Double.valueOf("1000.00000000000"), Double.valueOf("0.0"),
      Arrays.asList("RNA_b0001", "tRNA_biomass"),
      Arrays.asList("-1", "-24.3340140000000"), 0.0, "continuous");
    meReactionPlugin.createtRNAChargingReaction(model, groups, objective,
      "charging_tRNA_b10001_AAC", "", "tRNA_b0001_AAC",
      Double.valueOf("1000.00000000000"), Double.valueOf("0.0"), 65.0,
      Arrays.asList("h2o_c", "YrdC_mono", "RNA_b0001", "h_c", "TruA_dim",
        "preq1_c", "YggH_mono", "IscS_mod_2__SBML__pydx5p_mod_1__SBML__SH",
        "QueG_mono", "Tgt_hexa_mod_6__SBML__zn2", "thr__L_c", "ahcys_c",
        "Asn_RS_dim", "ThiI_mono", "QueA_mono", "TruB_mono", "ade_c",
        "generic_Dus", "gua_c", "amp_c", "met__L_c", "amet_c",
        "RED_THIOREDOXIN_MONOMER_mod_Oxidized", "nadp_c", "nadph_c", "atp_c",
        "hco3_c", "ppi_c", "RED_THIOREDOXIN_MONOMER", "TrmA_mono",
        "IscS_mod_2__SBML__pydx5p", "asn__L_c", "generic_tRNA_AAC_asn__L_c"),
      Arrays.asList("0.000232533333333333*mu + 9.10368e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-0.000116266666666667*mu - 4.55184e-5",
        "0.000116266666666667*mu + 4.55184e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-0.000116266666666667*mu - 4.55184e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5) - 0.000116266666666667*mu - 4.55184e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-0.000116266666666667*mu - 4.55184e-5",
        "0.000232533333333333*mu + 9.10368e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 1.0000455184)",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "0.000116266666666667*mu + 4.55184e-5",
        "-8.54700854700855e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "0.000116266666666667*mu + 4.55184e-5",
        "0.000232533333333333*mu + 9.10368e-5",
        "0.000116266666666667*mu + 4.55184e-5", "-0.0003488*mu - 0.0001365552",
        "0.000116266666666667*mu + 4.55184e-5",
        "0.000232533333333333*mu + 9.10368e-5",
        "-0.000232533333333333*mu - 9.10368e-5",
        "-0.000232533333333333*mu - 9.10368e-5",
        "-0.000116266666666667*mu - 4.55184e-5",
        "0.000232533333333333*mu + 9.10368e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5) - 0.000116266666666667*mu - 4.55184e-5",
        "-4.27350427350427e-6*mu*(0.000116266666666667*mu + 4.55184e-5)",
        "0.000116266666666667*mu + 4.55184e-5",
        "-0.000116266666666667*mu - 4.55184e-5", "1"),
      0.0, "continuous",
      Arrays.asList("D_at_16", "s4U_at_8", "m7G_at_46", "t6A_at_37", "Y_at_39",
        "D_at_20", "Y_at_55", "Q_at_34", "m5U_at_54"),
      Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1), "Asn_RS_dim", "AAC",
      "asn__L_c");
    meReactionPlugin.createTranscriptionReaction(model, sbol, groups, objective,
      "transcription_TU0002_from_RpoD_mono", "", "TU0002_from_RpoD_mono",
      Double.valueOf("1000.00000000000"), Double.valueOf("0.0"),
      Arrays.asList("Rho_hexa_mod_3__SBML__mg2", "utp_c",
        "Mfd_mono_mod_1__SBML__mg2", "NusA_mono", "atp_c", "mRNA_biomass",
        "RNAP70_CPLX", "ppi_c", "RpoZ_mono_mod_1__SBML__mg2", "NusG_mono",
        "h2o_c", "GreA_mono", "RNA_b0002", "RNA_b0003", "gtp_c", "h_c", "adp_c",
        "ctp_c", "pi_c", "GreB_mono"),
      Arrays.asList("-4.27350427350427e-6*mu", "-172",
        "-4.27350427350427e-6*mu", "-4.27350427350427e-6*mu", "-187.0",
        "161.160666", "-0.00354592439652316*mu - 0.00138822940123882", "742",
        "-4.27350427350427e-6*mu", "-4.27350427350427e-6*mu", "-3.0",
        "-4.27350427350427e-6*mu", "1", "1", "-202", "3.0", "3.0", "-184",
        "3.0", "-4.27350427350427e-6*mu"),
      0.0, "continuous",
      "CCACCCGGCGCGCCATGCTGGTTTCCACTGGTGTGAGGTCGTACATTTTCCCTGCGAAAAGGTGCGGAAAAGCGCGGTAAATAAGGAAAGAGAATTGACTCCGGAGTGTACAATTATTACAATCCGGCCTCTTTAATCACCCATGGCTTCGGTGTCCATCGTTTCATTTTTCGGCGGATATCCAATAAAGCCATTGAATTTATTCAAGTTTAGGTAGAAATCGCCATGAAACGCACTTTTCAACCGTCTGTACTGAAGCGCAACCGTTCTCACGGCTTCCGTGCTCGTATGGCTACTAAAAATGGTCGTCAGGTTCTGGCACGTCGTCGTGCTAAAGGCCGCGCTCGTCTGACCGTTTCTAAGTAATAAAGCTAACCCCTGAGTGGTTAAGCTCGCATTTCCCAGGGAGTTACGCTTGTTAACTCCCAGTCAATTCACATTCGTCTTCCAGCAGCCACAACGGGCTGGCACGCCGCAAATTACCATTCTCGGCCGCCTGAATTCGCTGGGGCATCCCCGTATCGGTCTTACAGTCGCCAAGAAAAACGTTCGACGCGCCCATGAACGCAATCGGATTAAACGTCTGACGCGTGAAAGCTTCCGTCTGCGCCAACATGAACTCCCGGCTATGGATTTCGTGGTGGTGGCGAAAAAAGGGGTTGCCGACCTCGATAACCGTGCTCTCTCGGAAGCGTTGGAAAAATTATGGCGCCGCCACTGTCGCCTGGCTCGCGGGTCCTGA",
      Arrays.asList("Transcription_normal_rho_dependent"), Arrays.asList(1));
    meReactionPlugin.createTranslationReaction(model, sbol, groups, objective,
      "translation_b0002", "", "b0002", Double.valueOf("1000.00000000000"),
      Double.valueOf("0.0"),
      Arrays.asList("generic_tRNA_ACU_thr__L_c", "h2o_c", "RNA_degradosome",
        "Rrf_mono", "InfA_mono", "generic_tRNA_START_met__L_c",
        "generic_tRNA_CAG_gln__L_c", "mRNA_biomass", "Fmt_mono_mod_mg2_mod_k",
        "arg__L_c", "h_c", "generic_tRNA_CCG_pro__L_c", "protein_biomass",
        "PrfC_mono", "val__L_c", "ppi_c", "ump_c", "generic_tRNA_CAC_his__L_c",
        "gdp_c", "atp_c", "cmp_c", "ribosome", "pro__L_c",
        "generic_tRNA_GGU_gly_c", "ser__L_c", "thf_c", "for_c",
        "generic_tRNA_CUG_leu__L_c", "generic_tRNA_UUC_phe__L_c",
        "generic_tRNA_GGC_gly_c", "thr__L_c", "generic_tRNA_AAG_lys__L_c",
        "gly_c", "leu__L_c", "gmp_c", "lys__L_c", "adp_c", "phe__L_c",
        "protein_b0002", "generic_tRNA_UCU_ser__L_c", "InfC_mono", "ala__L_c",
        "FusA_mono", "generic_tRNA_AAU_asn__L_c", "generic_tRNA_GUA_val__L_c",
        "generic_tRNA_CGU_arg__L_c", "RNA_b0002", "generic_Tuf",
        "generic_tRNA_AAC_asn__L_c", "met__L_c", "Tsf_mono", "InfB_mono",
        "generic_tRNA_GCA_ala__L_c", "gln__L_c", "generic_RF",
        "generic_tRNA_AUG_met__L_c", "generic_tRNA_GUU_val__L_c",
        "generic_tRNA_GCU_ala__L_c", "generic_tRNA_ACC_thr__L_c", "his__L_c",
        "asn__L_c", "Def_mono_mod_1__SBML__fe2", "generic_tRNA_AAA_lys__L_c",
        "generic_tRNA_UUU_phe__L_c", "generic_tRNA_CGC_arg__L_c", "gtp_c",
        "pi_c", "generic_tRNA_CAA_gln__L_c", "SBML__10fthf_c", "amp_c"),
      Arrays.asList("-2.0", "-93.0 - 0.209327846364883*(mu + 0.3915)/mu",
        "-2.55589556001079e-8*mu - 1.00063311174422e-8",
        "-2.62130898398882e-7*mu", "-4.27350427350427e-6*mu", "-1.0", "-1.0",
        "-0.271481219314129*(mu + 0.3915)/mu", "-1.80324164444648e-7*mu", "-11",
        "137.0 + 0.209327846364883*(mu + 0.3915)/mu", "-1.0", "5.396557",
        "-4.27350427350427e-6*mu", "-3", "45.0",
        "0.179423868312757*(mu + 0.3915)/mu", "-1.0", "92.0",
        "-45.0 - 0.209327846364883*(mu + 0.3915)/mu",
        "0.203347050754458*(mu + 0.3915)/mu",
        "-0.00065948459126711*mu - 0.000258188217481074", "-1", "-1.0", "-3",
        "1.0", "1.0", "-3.0", "-1.0", "-2.0", "-3", "-2.0", "-3", "-3",
        "0.233251028806584*(mu + 0.3915)/mu", "-5",
        "0.209327846364883*(mu + 0.3915)/mu", "-2", "1", "-3.0",
        "-4.27350427350427e-6*mu", "-5", "-0.000192307692307692*mu", "-1.0",
        "-1.0", "-8.0",
        "-0.000498399634202103*mu - 0.000195123456790123 - 0.00598079561042524*(mu + 0.3915)/mu",
        "-0.000192307692307692*mu", "-1.0", "-2", "-0.000192307692307692*mu",
        "-4.45382843794892e-6*mu", "-1.0", "-2", "-1.58420211712986e-7*mu",
        "-1.0", "-2.0", "-4.0", "-1.0", "-1", "-2", "-2.72438972852433e-7*mu",
        "-3.0", "-1.0", "-3.0", "-92.0",
        "92.0 + 0.209327846364883*(mu + 0.3915)/mu", "-1.0", "-1.0",
        "45.0 + 0.227270233196159*(mu + 0.3915)/mu"),
      0.0, "continuous",
      "ATGAAACGCACTTTTCAACCGTCTGTACTGAAGCGCAACCGTTCTCACGGCTTCCGTGCTCGTATGGCTACTAAAAATGGTCGTCAGGTTCTGGCACGTCGTCGTGCTAAAGGCCGCGCTCGTCTGACCGTTTCTAAGTAA",
      Arrays.asList("lys_addition_at_AAA", "asn_addition_at_AAU",
        "arg_addition_at_CGU", "phe_addition_at_UUU", "peptide_chain_release",
        "Translation_initiation_factor_InfC",
        "Translation_initiation_factor_InfA", "asn_addition_at_AAC",
        "pro_addition_at_CCG", "ser_addition_at_UCU", "arg_addition_at_CGC",
        "phe_addition_at_UUC", "lys_addition_at_AAG", "val_addition_at_GUU",
        "Translation_gtp_initiation_factor_InfB", "ala_addition_at_GCA",
        "FusA_mono_elongation", "val_addition_at_GUA", "his_addition_at_CAC",
        "peptide_deformylase_processing", "leu_addition_at_CUG",
        "UAA_generic_RF_mediated_termination", "Tuf_gtp_regeneration",
        "ala_addition_at_GCU", "gln_addition_at_CAA", "met_addition_at_AUG",
        "gln_addition_at_CAG", "thr_addition_at_ACC", "gly_addition_at_GGU",
        "ribosome_recycler", "thr_addition_at_ACU", "gly_addition_at_GGC",
        "fmet_addition_at_START"),
      Arrays.asList(3, 1, 8, 1, 1, 1, 1, 1, 1, 3, 3, 1, 2, 2, 1, 1, (int) 45.0,
        1, 1, 1, 3, 1, (int) 45.0, 4, 1, 1, 1, 1, 1, 1, 2, 2, 1));
    meReactionPlugin.createPostTranslationReaction(model, groups, objective,
      "translocation_b0002", "", "translocation_protein_b0002",
      Double.valueOf("1000.00000000000"), Double.valueOf("0.0"),
      Arrays.asList("h2o_c", "gdp_c", "SRP_CPLX",
        "protein_b0002_Inner_Membrane", "protein_b0002", "gtp_c", "h_c", "pi_c",
        "Sec_CPLX", "YidC_MONOMER"),
      Arrays.asList("-1.0", "1.0", "-0.00661111111111111*mu", "1.0", "-1.0",
        "-1.0", "1.0", "1.0", "-0.00661111111111111*mu",
        "-0.00661111111111111*mu"),
      0.0, "continuous", null, null, 0.0,
      Arrays.asList("TatE_MONOMER", "TatA_MONOMER", "srp_yidC_translocation"),
      Arrays.asList(21.0, 21.0, 0.0), 1.0, null, null, null, null, null, null,
      "");
    meReactionPlugin.createComplexFormationReaction(model, groups, objective,
      "formation_RpmH_mono", "", "RpmH_mono", "RpmH_mono",
      Double.valueOf("1000.00000000000"), Double.valueOf("0.0"),
      Arrays.asList("RpmH_mono", "protein_b0002_Inner_Membrane"),
      Arrays.asList("1", "-1.0"), 0.0, "continuous", null, null);
    meReactionPlugin.createMetabolicReaction(model, groups, objective,
      "ADEt2rpp_REV_YICE_MONOMER", "", "ADEt2rpp", 1000.0, 0.0,
      Arrays.asList("ade_p", "YICE_MONOMER", "ade_c", "h_c", "h_p"),
      Arrays.asList("1.0", "-7.1469027074301e-6*mu", "-1.0", "-1.0", "1.0"),
      0.0, "continuous", 38.86687550524414, true, "YICE_MONOMER");
    meReactionPlugin.createMetabolicReaction(model, groups, objective,
      "ADEt2rpp_FWD_YICE_MONOMER", "", "ADEt2rpp", 1000.0, 0.0,
      Arrays.asList("ade_p", "YICE_MONOMER", "ade_c", "h_c", "h_p"),
      Arrays.asList("-1.0", "7.1469027074301e-6*mu", "1.0", "1.0", "-1.0"), 0.0,
      "continuous", 38.86687550524414, false, "YICE_MONOMER");
    // test generic formations
    meReactionPlugin.createGenericFormationReaction(model, groups, objective,
      "PrfA_mono_to_generic_RF", "", 1000.0, 0.0,
      Arrays.asList("generic_RF", "PrfA_mono"), Arrays.asList("1", "-1"), 0.0,
      "continuous");
    meReactionPlugin.createGenericFormationReaction(model, groups, objective,
      "PrfB_mono_to_generic_RF", "", 1000.0, 0.0,
      Arrays.asList("generic_RF", "PrfB_mono"), Arrays.asList("1", "-1"), 0.0,
      "continuous");
    // set reaction for biomass constraints
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "biomass_dilution", "", "mu", "mu", Arrays.asList("biomass"),
      Arrays.asList("-1"), 0.0, "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "protein_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("protein_biomass", "biomass", "unmodeled_protein_biomass"),
      Arrays.asList("-1", "1.56250000000000", "-0.56250000000000"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "mRNA_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("mRNA_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "tRNA_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("tRNA_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "rRNA_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("rRNA_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "ncRNA_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("ncRNA_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "DNA_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("DNA_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "lipid_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("lipid_biomass", "biomass"), Arrays.asList("-1", "1"), 0.0,
      "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "constituent_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("constituent_biomass", "biomass"), Arrays.asList("-1", "1"),
      0.0, "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "prosthetic_group_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("prosthetic_group_biomass", "biomass"),
      Arrays.asList("-1", "1"), 0.0, "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "peptidoglycan_biomass_to_biomass", "", "1000.00000000000", "0.0",
      Arrays.asList("peptidoglycan_biomass", "biomass"),
      Arrays.asList("-1", "1"), 0.0, "continuous");
    meReactionPlugin.createSummaryVariableReaction(model, groups, objective,
      "dna_replication", "",
      "mu*(-0.125041790343836*mu**3.90364139015214/(mu**3.90364139015214 + 0.116858742296566) + 0.192933177925728)".replaceAll(
        "\\*\\*", "\\^"),
      "mu*(-0.125041790343836*mu**3.90364139015214/(mu**3.90364139015214 + 0.116858742296566) + 0.192933177925728)".replaceAll(
        "\\*\\*", "\\^"),
      Arrays.asList("datp_c", "ppi_c", "DNA_biomass", "dctp_c", "dgtp_c",
        "dttp_c"),
      Arrays.asList("-0.246051501452000", "1", "0.307433440943493",
        "-0.253948498548000", "-0.253948498548000", "-0.246051501452000"),
      0.0, "continuous");
    // set global info as parameter
    model.createParameter("global_info__m_nt");
    model.getParameter("global_info__m_nt").initDefaults(2, 4, true);
    model.getParameter("global_info__m_nt").setValue(0.324);
    model.createParameter("global_info__r0");
    model.getParameter("global_info__r0").initDefaults(2, 4, true);
    model.getParameter("global_info__r0").setValue(0.087);
    model.createParameter("global_info__temperature");
    model.getParameter("global_info__temperature").initDefaults(2, 4, true);
    model.getParameter("global_info__temperature").setValue(37);
    model.createParameter("global_info__f_tRNA");
    model.getParameter("global_info__f_tRNA").initDefaults(2, 4, true);
    model.getParameter("global_info__f_tRNA").setValue(0.12);
    model.createParameter("global_info__GC_fraction");
    model.getParameter("global_info__GC_fraction").initDefaults(2, 4, true);
    model.getParameter("global_info__GC_fraction").setValue(0.507896997096);
    model.createParameter("global_info__m_tRNA");
    model.getParameter("global_info__m_tRNA").initDefaults(2, 4, true);
    model.getParameter("global_info__m_tRNA").setValue(25.0);
    model.createParameter("global_info__kt");
    model.getParameter("global_info__kt").initDefaults(2, 4, true);
    model.getParameter("global_info__kt").setValue(4.5);
    model.createParameter("global_info__m_rr");
    model.getParameter("global_info__m_rr").initDefaults(2, 4, true);
    model.getParameter("global_info__m_rr").setValue(1453.0);
    model.createParameter("global_info__f_mRNA");
    model.getParameter("global_info__f_mRNA").initDefaults(2, 4, true);
    model.getParameter("global_info__f_mRNA").setValue(0.02);
    model.createParameter("global_info__propensity_scaling");
    model.getParameter("global_info__propensity_scaling").initDefaults(2, 4,
      true);
    model.getParameter("global_info__propensity_scaling").setValue(0.45);
    model.createParameter("global_info__f_rRNA");
    model.getParameter("global_info__f_rRNA").initDefaults(2, 4, true);
    model.getParameter("global_info__f_rRNA").setValue(0.086);
    model.createParameter("global_info__m_aa");
    model.getParameter("global_info__m_aa").initDefaults(2, 4, true);
    model.getParameter("global_info__m_aa").setValue(0.109);
    model.createParameter("global_info__k_deg");
    model.getParameter("global_info__k_deg").initDefaults(2, 4, true);
    model.getParameter("global_info__k_deg").setValue(12.0);
    // append process data
    model.appendAnnotation(meProcessData);
    //
    // write
    //
    TidySBMLWriter tidySBMLWriter = new TidySBMLWriter();
    tidySBMLWriter.write(doc, "example/ExampleModel.sbml");
    // SBOL Test
    sbol.write("example/ExampleModel.sbol");
    CombineArchive ca = new CombineArchive(new File("ExampleArchive.zip"));
    ca.addEntry(new File("example"), new File("example/ExampleModel.sbml"),
      new URI("http://identifiers.org/combine.specifications/sbml"));
    ca.addEntry(new File("example"), new File("example/ExampleModel.sbol"),
      new URI("http://identifiers.org/combine.specifications/sbol"));
    ca.pack();
    ca.close();
  }
}
