package org.rekeningsysteem.data.util.header;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import org.rekeningsysteem.exception.DatumException;

/**
 * <p>
 * Klasse voor het representeren van een datum. Hiertoe wordt gecontroleerd of een ingegeven (dag,
 * maand, jaar)-tupel kan worden gerepresenteerd als een datum naar de maatstaf van de <a
 * href="http://nl.wikipedia.org/wiki/Gregoriaanse_kalender">Gregoriaanse kalender</a>. Zie
 * {@link #checkDatum(int, int, int)} voor meer informatie.
 * </p>
 * <p>
 * Nadat een tupel goedgekeurd is, wordt het als Datum-object aangemaakt. Ook bij het wijzigen van
 * bestaande objecten wordt gecontroleerd of het (nieuwe) tupel nog steeds voldoet aan de eerder
 * genoemde maatstaf.
 * </p>
 * <p>
 * Tevens wordt de interface {@link IxmlConvertable} geïmplementeerd, zodat objecten kunnen worden
 * omgezet naar XML.
 * </p>
 * 
 * @author Richard van Heest
 */
public final class Datum {

	private final int dag;
	private final int maand;
	private final int jaar;

	/**
	 * Controleert of de datum geldig is door de volgende zaken te onderzoeken:
	 * <ol>
	 * <li>maand is een positief getal.</li>
	 * <li>maand is kleiner of gelijk aan 12.</li>
	 * <li>dag is een positief getal.</li>
	 * <li>dag is kleiner dan of gelijk aan het maximaal aantal dagen van de <em>maand</em>. Zie
	 * hiervoor tevens de functie {@link #maakDagenArray}.</li>
	 * </ol>
	 * Indien één dezer zaken niet in orde is, wordt een IllegalArgumentException geworpen.
	 * 
	 * @param dag De dag van de datum.
	 * @param maand De maand van de datum.
	 * @param jaar Het jaar van de datum.
	 * @throws DatumException Als het (dag, maand, jaar)-tupel niet geldig is.
	 */
	private static boolean checkDatum(int dag, int maand, int jaar) {
		return maand > 0 && maand <= 12 && dag > 0 && dag <= Datum.maakDagenArray(jaar)[maand - 1];
	}

	/**
	 * Retourneert een array met het aantal dagen per maand in een ingegeven jaar. Hierbij is staat
	 * het aantal dagen in maand <code>n</code> op plaats <code>n-1</code>.
	 * 
	 * @param jaar Het jaar waarvoor het aantal dagen per maand wordt opgevraagd.
	 * @return Een array met het aantal dagen per maand in een gegeven jaar.
	 */
	private static int[] maakDagenArray(int jaar) {
		int[] dagen = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (Datum.isSchrikkelJaar(jaar)) {
			dagen[1] = 29;
		}
		return dagen;
	}

	/**
	 * Bepaald of een jaar een schrikkeljaar is. Dit wordt gedaan volgens het volgende algoritme:
	 * <ol>
	 * <li>jaar % 400 = 0 => <b>true</b></li>
	 * <li>jaar % 100 = 0 => <b>false</b></li>
	 * <li>jaar % 4 = 0 => <b>true</b></li>
	 * <li>anders => <b>false</b></li>
	 * </ol>
	 * 
	 * @param jaar Het te onderzoeken jaartal.
	 * @return true indien jaar een schrikkeljaar is; anders wordt false geretourneerd.
	 */
	private static boolean isSchrikkelJaar(int jaar) {
		return (jaar % 400 == 0) ? true : (jaar % 100 == 0) ? false : (jaar % 4 == 0) ? true
				: false;
	}

	/**
	 * Constructor voor de Datum van vandaag.
	 */
	public Datum() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy",
				Locale.getDefault(Locale.Category.FORMAT));
		String datum = sdf.format(Calendar.getInstance().getTime());
		int[] array = this.scanDatum(datum);

		this.dag = array[0];
		this.maand = array[1];
		this.jaar = array[2];
	}

	/**
	 * Constructor voor een Datum met dag, maand en jaar. Hiervoor wordt eerst gecontroleerd of de
	 * datum correct is.
	 * 
	 * @param dag De dag van de datum.
	 * @param maand De maand van de datum.
	 * @param jaar Het jaar van de datum.
	 * @throws DatumException Wanneer de datum niet kan bestaan.
	 */
	public Datum(int dag, int maand, int jaar) throws DatumException {
		if (Datum.checkDatum(dag, maand, jaar)) {
			this.dag = dag;
			this.maand = maand;
			this.jaar = jaar;
		}
		else {
			throw new DatumException("Het tupel (" + dag + ", " + maand + ", " + jaar
					+ ") kan niet in een datum worden omgezet.");
		}
	}

	/**
	 * Constructor voor een datum die een String van de vorm [<em>dag</em>]-[<em>maand</em>]-[
	 * <em>jaar</em>] omzet naar een Datum-object.
	 * 
	 * @param datum De datum die moet worden uitgelezen.
	 * @throws DatumException Wanneer de datum niet bestaat.
	 * @throws ParseException Wanneer de String <em>datum</em> niet kan worden omgezet naar een
	 *             datum.
	 */
	public Datum(String datum) throws DatumException {
		int[] array = this.scanDatum(datum);
		if (Datum.checkDatum(array[0], array[1], array[2])) {
			this.dag = array[0];
			this.maand = array[1];
			this.jaar = array[2];
		}
		else {
			throw new DatumException("De String \"" + datum + "\" kan niet in een Datum-object"
					+ "worden omgezet.");
		}
	}

	private int[] scanDatum(String datum) {
		try (Scanner scan = new Scanner(datum)) {
			scan.useDelimiter("-");

			int[] array = new int[3];
			array[0] = scan.nextInt();
			array[1] = scan.nextInt();
			array[2] = scan.nextInt();

			return array;
		}
	}

	/**
	 * Retourneert de dag behorende bij de datum.
	 * 
	 * @return de dag
	 */
	public int getDag() {
		return this.dag;
	}

	/**
	 * Retourneert de maand behorende bij de datum.
	 * 
	 * @return de maand, weergegeven als getal; met januari = 1, februari = 2, enz.
	 */
	public int getMaand() {
		return this.maand;
	}

	/**
	 * Retourneert het jaar behorende bij de datum.
	 * 
	 * @return het jaar
	 */
	public int getJaar() {
		return this.jaar;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Datum) {
			Datum that = (Datum) other;
			return Objects.equals(this.dag, that.dag)
					&& Objects.equals(this.maand, that.maand)
					&& Objects.equals(this.jaar, that.jaar);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.dag, this.maand, this.jaar);
	}

	@Override
	public String toString() {
		return "<Datum[" + this.printable() + "]>";
	}

	/**
	 * Retourneert een String-representatie van de datum volgens het standaardprincipe dd-mm-jjjj.
	 * 
	 * @return een String-representatie van de datum.
	 */
	public String printable() {
		String dag = String.valueOf(this.dag);
		String maand = String.valueOf(this.maand);
		String jaar = String.valueOf(this.jaar);

		if (dag.length() == 1) {
			dag = "0" + dag;
		}

		if (maand.length() == 1) {
			maand = "0" + maand;
		}

		return dag + "-" + maand + "-" + jaar;
	}
}
