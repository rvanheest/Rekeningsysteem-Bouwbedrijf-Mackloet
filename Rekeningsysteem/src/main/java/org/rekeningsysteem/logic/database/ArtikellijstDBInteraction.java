package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

public class ArtikellijstDBInteraction extends DBInteraction<EsselinkArtikel> {

	private static final QueryEnumeration allQuery = () -> "SELECT * FROM Artikellijst;";
	private static final Function<String, QueryEnumeration> artNrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE artikelnummer LIKE '" + s + "%';";
	private static final Function<String, QueryEnumeration> omschrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE omschrijving LIKE '%" + s + "%';";

	private static final QueryEnumeration clearTable = () -> "DELETE FROM Artikellijst;";

	private static final Function<EsselinkArtikel, String> artikelToString = ea ->
			"('" + ea.artikelNummer() + "', '" + ea.omschrijving().replace("\'", "\'\'")
					+ "', '" + ea.prijsPer() + "', '" + ea.eenheid()
					+ "', '" + ea.verkoopPrijs().bedrag() + "')";
	private static final Function<EsselinkArtikel, QueryEnumeration> insert = ea -> () ->
			"INSERT INTO Artikellijst VALUES " + artikelToString.apply(ea) + ";";
	private static final Function<Observable<EsselinkArtikel>, Maybe<QueryEnumeration>> insertAll =
			eas -> eas.map(artikelToString::apply).reduce((s1, s2) -> s1 + ", " + s2)
					.map(s -> () -> "INSERT INTO Artikellijst VALUES " + s + ";");

	public ArtikellijstDBInteraction(Database database) {
		super(database);
	}

	public Completable clearData() {
		return this.update(clearTable);
	}

	public Single<Integer> insert(EsselinkArtikel ea) {
		return this.updateAndCount(insert.apply(ea));
	}

	public Single<Integer> insertAll(Observable<EsselinkArtikel> eas) {
		return insertAll.apply(eas)
				.flatMapSingle(this::updateAndCount)
				.defaultIfEmpty(0)
				.onErrorResumeNext(e -> Single.just(0));
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
