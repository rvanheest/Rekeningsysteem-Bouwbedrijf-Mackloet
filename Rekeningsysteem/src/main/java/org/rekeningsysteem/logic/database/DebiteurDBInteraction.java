package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;

public class DebiteurDBInteraction extends DBInteraction<Debiteur> {

	private static final Func1<String, QueryEnumeration> naamQuery = s -> () ->
			"SELECT * FROM TotaalDebiteur WHERE naam LIKE '%" + s + "%';";
	private static final QueryEnumeration allQuery = () -> "SELECT * FROM TotaalDebiteur;";

	private static final Func1<Debiteur, QueryEnumeration> debInsert = deb -> () ->
			"INSERT INTO TotaalDebiteur (naam, straat, nummer, postcode, plaats"
					+ deb.getBtwNummer().map(s -> ", btwNummer").orElse("") + ") VALUES ('"
        			+ deb.getNaam() + "', '" + deb.getStraat() + "', '"
        			+ deb.getNummer() + "', '" + deb.getPostcode() + "', '" + deb.getPlaats()
        			+ deb.getBtwNummer().map(s -> "', '" + s).orElse("") + "');";

	public DebiteurDBInteraction(Database database) {
		super(database);
	}

	public Observable<Integer> addDebiteur(Debiteur debiteur) {
		return this.update(debInsert.call(debiteur));
	}

	public Observable<Debiteur> getAll() {
		return this.getFromDatabase(allQuery);
	}

	public Observable<Debiteur> getWithNaam(String text) {
		return this.getFromDatabase(naamQuery, text);
	}

	@Override
	Debiteur resultExtractor(ResultSet result) throws SQLException {
		String naam = result.getString("naam");
		String straat = result.getString("straat");
		String nummer = result.getString("nummer");
		String postcode = result.getString("postcode");
		String plaats = result.getString("plaats");
		String btwNummer = result.getString("btwNummer");

		return new Debiteur(naam, straat, nummer, postcode, plaats, btwNummer);
	}
}
