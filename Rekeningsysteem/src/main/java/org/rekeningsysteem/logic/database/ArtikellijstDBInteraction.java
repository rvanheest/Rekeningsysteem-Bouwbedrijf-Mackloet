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

	private static final Func1<EsselinkArtikel, String> artikelToString = ea ->
			"('" + ea.getArtikelNummer() + "', '" + ea.getOmschrijving().replace("\'", "\'\'")
					+ "', '" + ea.getPrijsPer() + "', '" + ea.getEenheid()
					+ "', '" + ea.getVerkoopPrijs().getBedrag() + "')";
	private static final Func1<EsselinkArtikel, QueryEnumeration> insert = ea -> () ->
			"INSERT INTO Artikellijst VALUES " + artikelToString.call(ea) + ";";
	private static final Func1<Observable<EsselinkArtikel>, Observable<QueryEnumeration>> insertAll =
			eas -> eas.map(artikelToString).reduce((s1, s2) -> s1 + ", " + s2)
					.map(s -> () -> "INSERT INTO Artikellijst VALUES " + s + ";");

	public ArtikellijstDBInteraction(Database database) {
		super(database);
	}

	public Observable<Integer> clearData() {
		return this.update(clearTable);
	}

	public Observable<Integer> insert(EsselinkArtikel ea) {
		return this.update(insert.call(ea));
	}

	public Observable<Integer> insertAll(Observable<EsselinkArtikel> eas) {
		return insertAll.call(eas)
				.flatMap(this::update)
				.onErrorResumeNext(e -> Observable.just(0));
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
