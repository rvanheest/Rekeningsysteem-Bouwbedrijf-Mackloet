package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;

public class ArtikellijstDBInteraction extends DBInteraction<EsselinkArtikel> {

	private static final QueryEnumeration allQuery = () -> "SELECT * FROM Artikellijst;";
	private static final Func1<String, QueryEnumeration> artNrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE artikelnummer LIKE '" + s + "%';";
	private static final Func1<String, QueryEnumeration> omschrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE omschrijving LIKE '%" + s + "%';";

	private static final QueryEnumeration clearTable = () -> "DELETE FROM Artikellijst;";
	private static final Func1<EsselinkArtikel, QueryEnumeration> insert = ea -> () ->
			"INSERT INTO Artikellijst VALUES ('" + ea.getArtikelNummer() + "', '"
					+ ea.getOmschrijving().replace("\'", "\'\'") + "', '"
					+ ea.getPrijsPer() + "', '"
					+ ea.getEenheid() + "', '"
					+ ea.getVerkoopPrijs().getBedrag() + "');";

	public ArtikellijstDBInteraction(Database database) {
		super(database);
	}

	public Observable<Integer> clearData() {
		return this.update(clearTable);
	}

	// TODO add insertAll
	public Observable<Integer> insert(EsselinkArtikel ea) {
		return this.update(insert.call(ea));
	}

	public Observable<EsselinkArtikel> getAll() {
		return this.getFromDatabase(allQuery);
	}

	public Observable<EsselinkArtikel> getWithArtikelnummer(String text) {
		return this.getFromDatabase(artNrQuery, text);
	}

	public Observable<EsselinkArtikel> getWithOmschrijving(String text) {
		return this.getFromDatabase(omschrQuery, text);
	}

	@Override
	EsselinkArtikel resultExtractor(ResultSet result) throws SQLException {
		String artNr = result.getString("artikelnummer");
		String omschr = result.getString("omschrijving");
		int prijsPer = result.getInt("prijsPer");
		String eenheid = result.getString("eenheid");
		Geld verkoopPrijs = new Geld(result.getDouble("verkoopprijs"));

		return new EsselinkArtikel(artNr, omschr, prijsPer, eenheid, verkoopPrijs);
	}
}
