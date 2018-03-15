package sbmlme.converter;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.*;
import org.jdom2.JDOMException;
import org.sbml.jsbml.SBMLException;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;

import de.unirostock.sems.cbarchive.CombineArchiveException;

/**
 * Contains the main method for the bidirectional conversion between an SBMLme
 * and a COBRAme model.
 * 
 * @author Marc A. Voigt
 */
@SuppressWarnings("restriction")
public class SBMLmeConverter {

  public static void main(String[] args) throws XMLStreamException, IOException,
    SBOLValidationException, SBOLConversionException, SBMLException,
    org.sbml.jsbml.text.parser.ParseException, JDOMException,
    java.text.ParseException, CombineArchiveException, URISyntaxException,
    TransformerException {
    // create command line arguments
    Options options = new Options();
    Option input = new Option("i", "input", true,
      "(required) input file path of SBML file or COBRAme JSON file containing the model");
    input.setRequired(true);
    Option model =
      new Option("m", "model", true, "name to be used for the SBML model");
    model.setRequired(false);
    options.addOption(model);
    options.addOption(input);
    Option sbol = new Option("s", "sbol", true,
      "input file path of SBOL file when converting from SBML to JSON");
    sbol.setRequired(false);
    options.addOption(sbol);
    Option direction = new Option("d", "direction", false,
      "If set convert from SBML and SBOL to JSON, else convert from JSON to SBML and SBOL");
    direction.setRequired(false);
    options.addOption(direction);
    Option validation = new Option("v", "validation", false,
      "Flag to be used if the created SBML document should be validated");
    validation.setRequired(false);
    options.addOption(validation);
    Option tidy = new Option("t", "tidy", false,
      "Flag to be used if the created files should be printed with intends for easier reading");
    tidy.setRequired(false);
    options.addOption(tidy);
    Option output = new Option("o", "output", true,
      "(required) output path of SBML file or COBRAme JSON file containing the model (file endings will be added autmatically)");
    output.setRequired(true);
    options.addOption(output);
    // read in command line commands if possible
    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      formatter.printHelp("SBMLmeConverter", options);
      System.exit(1);
      return;
    }
    String inputFile = cmd.getOptionValue("input");
    String outputFile = cmd.getOptionValue("output");
    String modelName = "";
    Boolean validate = false;
    Boolean tidyPrint = false;
    Boolean conversionDirection = false;
    String sbolFile = "";
    // if direction of conversion is SBML/SBOL to JSON set true
    if (cmd.hasOption("direction")) {
      conversionDirection = true;
    }
    if (cmd.hasOption("validation")) {
      validate = true;
    }
    if (cmd.hasOption("tidy")) {
      tidyPrint = true;
    }
    if (cmd.hasOption("sbol")) {
      sbolFile = cmd.getOptionValue("sbol");
    }
    if (cmd.hasOption("model")) {
      modelName = cmd.getOptionValue("model");
    }
    // check inputs for validity
    if (conversionDirection && sbolFile.equals("")) {
      System.out.println(
        "A SBOL file is required when converting from SBML to JSON");
      System.exit(1);
      return;
    } else if (!conversionDirection && modelName.equals("")) {
      System.out.println("A name for the SBML model needs to be chosen");
      System.exit(1);
      return;
    }
    // convert file
    long startTime = System.nanoTime();
    if (conversionDirection) {
      new MESBMLToJson(inputFile, sbolFile, outputFile, tidyPrint);
    } else {
      new MEJsonToSBML(inputFile, outputFile, modelName, validate, tidyPrint);
    }
    long endTime = System.nanoTime();
    long duration = (endTime - startTime) / 1000000;
    double stopped = duration / 1000.0;
    System.out.println(
      "Finished conversion after " + String.valueOf(stopped) + " seconds.");
  }
}
