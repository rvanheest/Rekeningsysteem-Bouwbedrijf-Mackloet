package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.ExFunc1;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public class DebiteurDBInteraction extends DBInteraction<Debiteur> {

	private static final Func1<String, QueryEnumeration> naamQuery = s -> () ->
			"SELECT * FROM Debiteur NATURAL LEFT JOIN BTWDebiteur WHERE naam LIKE '%" + s + "%';";
	private static final Func0<QueryEnumeration> allQuery = () -> () ->
			"SELECT * FROM Debiteur NATURAL LEFT JOIN BTWDebiteur;";
	
	public DebiteurDBInteraction(Database database) {
		super(database);
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
