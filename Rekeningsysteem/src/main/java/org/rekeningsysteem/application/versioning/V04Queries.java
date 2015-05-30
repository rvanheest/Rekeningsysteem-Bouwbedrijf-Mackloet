package org.rekeningsysteem.application.versioning;

import org.rekeningsysteem.io.database.QueryEnumeration;

public enum V04Queries implements QueryEnumeration {

	VERSION_TABLE_EXISTS("SELECT name FROM sqlite_master WHERE type='table' AND name='Metadata';"),

	GET_DB_VERSION("SELECT version FROM Metadata"),

	TABLE_COUNT("SELECT COUNT(name) AS 'count' FROM sqlite_master WHERE type='table';"),

	DROP_ART_NR_INDEX("DROP INDEX ArtNrIndex;"),

	DROP_NAME_SEARCH_INDEX("DROP INDEX NameSearch;"),

	DROP_DEBITEUR_TABLE("DROP TABLE Debiteur;"),

	CREATE_ARTIKELNUMMER_INDEX("CREATE INDEX ArtikelnummerIndex ON Artikellijst (artikelnummer);"),

	CREATE_OMSCHRIJVING_INDEX("CREATE INDEX ArtikelOmschrijvingIndex ON Artikellijst (omschrijving);"),

	CREATE_DEBITEUR("CREATE TABLE Debiteur (debiteurID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "naam TEXT NOT NULL, straat TEXT NOT NULL, nummer TEXT NOT NULL, "
			+ "postcode TEXT NOT NULL, plaats TEXT NOT NULL);"),

	CREATE_BTW_DEBITEUR("CREATE TABLE BTWDebiteur (debiteurID INTEGER NOT NULL, "
			+ "btwNummer TEXT, PRIMARY KEY (debiteurID, btwNummer), "
			+ "FOREIGN KEY (debiteurID) REFERENCES Debiteur(debiteurID));"),

	CREATE_METADATA("CREATE TABLE Metadata (version TEXT NOT NULL);");

	private final String query;

	V04Queries(String query) {
		this.query = query;
	}

	@Override
	public String getQuery() {
		return this.query;
	}
}
