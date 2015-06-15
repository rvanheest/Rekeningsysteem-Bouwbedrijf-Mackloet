package org.rekeningsysteem.application.versioning;

import org.rekeningsysteem.io.database.QueryEnumeration;

public enum V04Queries implements QueryEnumeration {

	VERSION_TABLE_EXISTS("SELECT name FROM sqlite_master WHERE type='table' AND name='Metadata';"),

	GET_DB_VERSION("SELECT version FROM Metadata"),

	TABLE_COUNT("SELECT COUNT(name) AS 'count' FROM sqlite_master WHERE type='table';"),

	DROP_ART_NR_INDEX("DROP INDEX IF EXISTS ArtNrIndex;"),

	DROP_NAME_SEARCH_INDEX("DROP INDEX IF EXISTS NameSearch;"),

	DROP_DEBITEUR_TABLE("DROP TABLE IF EXISTS Debiteur;"), // removes old version of the Debiteur table

	CREATE_ARTIKELNUMMER_INDEX("CREATE INDEX ArtikelnummerIndex ON Artikellijst (artikelnummer);"),

	CREATE_OMSCHRIJVING_INDEX("CREATE INDEX ArtikelOmschrijvingIndex ON Artikellijst (omschrijving);"),

	CREATE_DEBITEUR("CREATE TABLE Debiteur (debiteurID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "naam TEXT NOT NULL, straat TEXT NOT NULL, nummer TEXT NOT NULL, "
			+ "postcode TEXT NOT NULL, plaats TEXT NOT NULL);"),

	CREATE_BTW_DEBITEUR("CREATE TABLE BTWDebiteur (debiteurID INTEGER NOT NULL, "
			+ "btwNummer TEXT, PRIMARY KEY (debiteurID, btwNummer), "
			+ "FOREIGN KEY (debiteurID) REFERENCES Debiteur(debiteurID));"),

	CREATE_TOTAAL_DEBITEUR_VIEW("CREATE VIEW TotaalDebiteur AS SELECT d.naam, d.straat, "
			+ "d.nummer, d.postcode, d.plaats, b.btwNummer "
			+ "FROM Debiteur d NATURAL LEFT JOIN BTWDebiteur b;"),

	CREATE_TOTAAL_DEBITEUR_TRIGGER("CREATE TRIGGER InsertDebiteurBtwDebiteur\n"
			+ "INSTEAD OF INSERT ON TotaalDebiteur\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "INSERT INTO Debiteur(naam, straat, nummer, postcode, plaats)\n"
			+ "SELECT NEW.naam, NEW.straat, NEW.nummer, NEW.postcode, NEW.plaats\n"
			+ "WHERE NOT EXISTS (\n"
			+ "SELECT 1\n"
			+ "FROM Debiteur\n"
    		+ "WHERE naam=NEW.naam\n"
    		+ "AND straat=NEW.straat\n"
    		+ "AND nummer=NEW.nummer\n"
    		+ "AND postcode=NEW.postcode\n"
    		+ "AND plaats=NEW.plaats);\n"
    		+ "\n"
    		+ "INSERT INTO BTWDebiteur (debiteurID, btwNummer)\n"
    		+ "SELECT Debiteur.debiteurID, NEW.btwNummer\n"
    		+ "FROM Debiteur\n"
    		+ "WHERE NEW.btwNummer IS NOT NULL\n"
    		+ "AND naam=NEW.naam\n"
    		+ "AND straat=NEW.straat\n"
    		+ "AND nummer=NEW.nummer\n"
    		+ "AND postcode=NEW.postcode\n"
    		+ "AND plaats=NEW.plaats\n"
    		+ "AND NOT EXISTS (\n"
    		+ "SELECT 1\n"
    		+ "FROM BTWDebiteur\n"
    		+ "WHERE debiteurID=Debiteur.debiteurID\n"
    		+ "AND btwNummer=NEW.btwNummer);\n"
    		+ "\n"
    		+ "END;"),

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
