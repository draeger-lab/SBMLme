/**
 * The SBMLme package extends JSBML to encode ME-models, specifically to encode
 * <a href="https://github.com/SBRG/cobrame">COBRAme</a> models.
 * <p>
 * A COBRAme model consists of a functional part and a data storage part. The
 * functional part contains species and reactions and is used for solving the
 * model. Both species and reactions are further divided into several classes
 * representing the different types of species and reactions like
 * "TranscriptionReaction" or "tRNAChargingReaction". Some reaction specific
 * data is not stored in the reactions themselves but in process data objects in
 * the data storage part of the model. This is done to simplify the creation and
 * editing of reactions while removing possible error sources. Process data
 * objects are also divided into different types. Most reaction types refer to a
 * reaction type specific process data type and the members of these reaction
 * types then only refer to a single process data object with the correct
 * reaction specific data type. The only exception to this are process data
 * objects of the type "StoichiometricData". These objects may be referred by up
 * two reactions which represent the opposite directions of a single reversible
 * reaction. The remaining types of process data objects are not referred by
 * reactions but by reaction-dependent process data objects. An example are
 * SubreactionData objects which contain information about a sub process that
 * occurs in a larger reaction or in several different reactions, like the
 * addition of a specific amino acid during translation when encountering a
 * specific codon. At last the data storage part contains global information
 * like GC content about the modeled organism.
 * </p>
 * <p>
 * SBMLme is intended to encode all information encountered in a COBRAme model.
 * As a basis SBML core and the fbc package are used to encode many attributes
 * of the functional part of the model. The groups package is used to encode the
 * different types of species, reactions and process data. The remaining
 * attributes of a COBRAme model are added to the annotations of the model, the
 * species and the reactions respectively by SBMLme. To simplify the model
 * representation in SBML the reaction-related process data objects are encoded
 * in the annotation of their reactions whenever the objects are only referable
 * by a single reaction. The other process data objects are added as lists to
 * the model’s annotation. The global information is added to the list of
 * parameters. A COBRAme model contains nucleotide sequences for its translation
 * and transcription reactions and its transcribed genes. These sequences are
 * stored in a separate SBOL document and are referred in the SBML document by
 * their URIs.
 * </p>
 */
package sbmlme;
