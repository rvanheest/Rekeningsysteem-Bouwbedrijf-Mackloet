package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;

public class DebiteurDBInteraction extends DBInteraction<Debiteur> {

	private static final Func1<String, QueryEnumeration> naamQuery = s -> () ->
			"SELECT * FROM TotaalDebiteur WHERE naam LIKE '%" + s + "%';";
	private static final Function<Debiteur, QueryEnumeration> debQuery = deb -> () ->
			"SELECT * FROM TotaalDebiteur WHERE naam = '" + deb.getNaam()
			+ "' AND straat = '" + deb.getStraat() + "' AND nummer = '" + deb.getNummer()
			+ "' AND postcode = '" + deb.getPostcode() + "' AND plaats = '" + deb.getPlaats()
			+ deb.getBtwNummer().map(s -> "' AND btwNummer = '" + s).orElse("") + "';";
	private static final QueryEnumeration allQuery = () -> "SELECT * FROM TotaalDebiteur;";

	private static final Function<Debiteur, QueryEnumeration> debInsert = deb -> () ->
			"INSERT INTO TotaalDebiteur (naam, straat, nummer, postcode, plaats"
					+ deb.getBtwNummer().map(s -> ", btwNummer").orElse("") + ") VALUES ('"
        			+ deb.getNaam() + "', '" + deb.getStraat() + "', '"
        			+ deb.getNummer() + "', '" + deb.getPostcode() + "', '" + deb.getPlaats()
        			+ deb.getBtwNummer().map(s -> "', '" + s).orElse("") + "');";
	private static final Function<Integer, QueryEnumeration> debDelete = id -> () ->
			"DELETE FROM TotaalDebiteur WHERE debiteurID = '" + id + "';";
	private static final BiFunction<Integer, Debiteur, QueryEnumeration> debUpdate = (oldID, debNew) -> () ->
			"UPDATE TotaalDebiteur SET naam = '" + debNew.getNaam()
			+ "', straat = '" + debNew.getStraat() + "', nummer = '" + debNew.getNummer()
			+ "', postcode = '" + debNew.getPostcode() + "', plaats = '" + debNew.getPlaats()
			+ "', btwNummer = " + debNew.getBtwNummer().map(s -> "'" + s + "'").orElse("NULL")
			+ " WHERE debiteurID = " + oldID + ";";

	public DebiteurDBInteraction(Database database) {
		super(database);
	}

	public Observable<Integer> addDebiteur(Debiteur debiteur) {
		return this.update(debInsert.apply(debiteur));
	}

	public Observable<Debiteur> addAndGetDebiteur(Debiteur debiteur) {
		return this.update(debInsert.apply(debiteur))
				.flatMap(i -> this.getFromDatabase(debQuery.apply(debiteur)));
	}

	public Observable<Integer> deleteDebiteur(Debiteur debiteur) {
		return this.update(debiteur.getDebiteurID()
				.map(debDelete)
				.orElseThrow(() -> new IllegalArgumentException("This debiteur does not contain "
						+ "an id: " + debiteur.toString())));
	}

	public Observable<Integer> updateDebiteur(Debiteur oldDebiteur, Debiteur newDebiteur) {
		return this.update(oldDebiteur.getDebiteurID()
				.map(id -> debUpdate.apply(id, newDebiteur))
				.orElseThrow(() -> new IllegalArgumentException("This debiteur does not contain "
						+ "an id: " + oldDebiteur.toString())));
	}

	public Observable<Debiteur> getAll() {
		return this.getFromDatabase(allQuery);
	}

	public Observable<Debiteur> getWithNaam(String text) {
		return this.getFromDatabase(naamQuery, text);
	}

	@Override
	Debiteur resultExtractor(ResultSet result) throws SQLException {
		int debiteurId = result.getInt("debiteurID");
		String naam = result.getString("naam");
		String straat = result.getString("straat");
		String nummer = result.getString("nummer");
		String postcode = result.getString("postcode");
		String plaats = result.getString("plaats");
		String btwNummer = result.getString("btwNummer");

		return new Debiteur(debiteurId, naam, straat, nummer, postcode, plaats, btwNummer);
	}
}
