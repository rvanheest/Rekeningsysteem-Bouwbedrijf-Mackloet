package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.ExFunc1;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

public class DebiteurDBInteraction extends DBInteraction<Debiteur> {

	private static final Func1<String, QueryEnumeration> naamQuery = s -> () ->
			"SELECT * FROM Debiteur NATURAL LEFT JOIN BTWDebiteur WHERE naam LIKE '%" + s + "%';";
	private static final Func0<QueryEnumeration> allQuery = () -> () ->
			"SELECT * FROM Debiteur NATURAL LEFT JOIN BTWDebiteur;";
	private static final Func1<Debiteur, String> idQuery = deb -> "SELECT Debiteur.debiteurID "
			+ "FROM Debiteur WHERE naam = '" + deb.getNaam() + "' AND straat = '" + deb.getStraat()
			+ "' AND nummer = '" + deb.getNummer() + "' AND postcode = '" + deb.getPostcode()
			+ "' AND plaats = '" + deb.getPlaats() + "'";

	private static final Func1<Debiteur, QueryEnumeration> debInsertFunc = deb -> () ->
			"INSERT INTO Debiteur (naam, straat, nummer, postcode, plaats) VALUES ('"
					+ deb.getNaam() + "', '" + deb.getStraat() + "', '"
					+ deb.getNummer() + "', '" + deb.getPostcode() + "', '"
					+ deb.getPlaats() + "');";
	private static final Func2<Debiteur, String, QueryEnumeration> btwInsert = (deb, num) -> () ->
			"INSERT INTO BTWDebiteur (debiteurID, btwNummer) VALUES (("
					+ idQuery.call(deb) + "), '" + num + "');";

	public DebiteurDBInteraction(Database database) {
		super(database);
	}

	public void addDebiteur(Debiteur debiteur) {
		Func0<QueryEnumeration> debInsert = () -> debInsertFunc.call(debiteur);
		QueryEnumeration insert = debiteur.getBtwNummer()
				.map(num -> btwInsert.call(debiteur, num))
				.map(debInsert.call()::append)
				.orElseGet(() -> debInsert.call());

		this.update(insert);
	}

	public Observable<Debiteur> getAll() {
		return this.with().call(allQuery);
	}

	public Observable<Debiteur> getWithNaam(String text) {
		return this.with(text).call(naamQuery);
	}

	@Override
	ExFunc1<ResultSet, Debiteur> resultExtractor() {
		return result -> {
			Integer id = result.getInt("debiteurID");
			String naam = result.getString("naam");
			String straat = result.getString("straat");
			String nummer = result.getString("nummer");
			String postcode = result.getString("postcode");
			String plaats = result.getString("plaats");
			String btwNummer = result.getString("btwNummer");

			return new Debiteur(id, naam, straat, nummer, postcode, plaats, btwNummer);
		};
	}
}
