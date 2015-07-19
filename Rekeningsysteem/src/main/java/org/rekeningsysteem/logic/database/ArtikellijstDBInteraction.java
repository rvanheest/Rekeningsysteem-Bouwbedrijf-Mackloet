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

	private static final Func1<String, QueryEnumeration> artNrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE artikelnummer LIKE '" + s + "%';";
	private static final Func1<String, QueryEnumeration> omschrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE omschrijving LIKE '%" + s + "%';";

	public ArtikellijstDBInteraction(Database database) {
		super(database);
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
