package org.rekeningsysteem.exception;

/**
 * Deze klasse bevat de construct voor een Exception die wordt geworpen als er iets fout gaat met
 * het aanmaken en/of wijzigen van een datum. Dit is mogelijk indien een datum wordt opgegeven die
 * niet kan bestaan.
 * 
 * @author Richard van Heest
 * @see klassen.Datum
 */
public class DatumException extends Exception {

	private static final long serialVersionUID = -6638828942955535187L;

	/**
	 * Constructor voor een DatumException.
	 * 
	 * @param err De errorString die de oorzaak van de exception aangeeft.
	 */
	public DatumException(String err) {
		super(err);
	}
}
