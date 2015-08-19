package org.rekeningsysteem.application.versioning;

import org.rekeningsysteem.io.database.QueryEnumeration;

public enum V03AlphaQueries implements QueryEnumeration {

	CREATE_ARTIKELLIJST("CREATE TABLE Artikellijst (artikelnummer TEXT NOT NULL, "
			+ "omschrijving TEXT NOT NULL, prijsPer NUMERIC NOT NULL, "
			+ "eenheid TEXT NOT NULL, verkoopprijs NUMERIC NOT NULL, "
			+ "PRIMARY KEY(artikelnummer));"),

	CREATE_DEBITEUR("CREATE TABLE Debiteur ("
			+ "debiteurID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "naam TEXT NOT NULL, straat TEXT NOT NULL, nummer TEXT NOT NULL, "
			+ "postcode TEXT NOT NULL, plaats TEXT NOT NULL, btwnummer TEXT);"),

	CREATE_ART_NR_INDEX("CREATE INDEX ArtNrIndex ON Artikellijst (artikelnummer);"),

	CREATE_NAME_SEARCH("CREATE INDEX NameSearch ON Debiteur (naam);");

	private final String query;

	V03AlphaQueries(String query) {
		this.query = query;
	}

	@Override
	public String getQuery() {
		return this.query;
	}
}
