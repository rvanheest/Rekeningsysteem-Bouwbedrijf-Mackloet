package org.rekeningsysteem.logic.database;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;

public class ArtikellijstDBInteraction {

	private static final Func1<String, QueryEnumeration> artNrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE artikelnummer LIKE '" + s + "%';";
	private static final Func1<String, QueryEnumeration> omschrQuery = s -> () ->
			"SELECT * FROM Artikellijst WHERE omschrijving LIKE '%" + s + "%';";

	private final Database database;

	public ArtikellijstDBInteraction(Database database) {
		this.database = database;
	}

	public Observable<EsselinkArtikel> getWithArtikelnummer(String text) {
		return this.getWith(text).call(artNrQuery);
	}
	
	public Observable<EsselinkArtikel> getWithOmschrijving(String text) {
		return this.getWith(text).call(omschrQuery);
	}
	
	private Func1<Func1<String, QueryEnumeration>, Observable<EsselinkArtikel>> getWith(String text) {
		return factory -> this.getFromDatabase(factory.call(text.replace("\'", "\'\'")));
	}
	
	private Observable<EsselinkArtikel> getFromDatabase(QueryEnumeration query) {
		return this.database.query(query, result -> {
			String artNr = result.getString("artikelnummer");
			String omschr = result.getString("omschrijving");
			int prijsPer = result.getInt("prijsPer");
			String eenheid = result.getString("eenheid");
			Geld verkoopPrijs = new Geld(result.getDouble("verkoopprijs"));

			return new EsselinkArtikel(artNr, omschr, prijsPer, eenheid, verkoopPrijs);
		});
	}
}
