package org.rekeningsysteem.application.versioning;

import org.rekeningsysteem.io.database.QueryEnumeration;

public enum V04AlphaQueries implements QueryEnumeration {

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
			+ "btwNummer TEXT, PRIMARY KEY (debiteurID), "
			+ "FOREIGN KEY (debiteurID) REFERENCES Debiteur(debiteurID));"),

	CREATE_TOTAAL_DEBITEUR_VIEW("CREATE VIEW TotaalDebiteur AS SELECT d.debiteurID, d.naam, d.straat, "
			+ "d.nummer, d.postcode, d.plaats, b.btwNummer "
			+ "FROM Debiteur d NATURAL LEFT JOIN BTWDebiteur b;"),

	CREATE_TOTAAL_DEBITEUR_INSERT_TRIGGER("CREATE TRIGGER InsertDebiteurBtwDebiteur\n"
			+ "INSTEAD OF INSERT ON TotaalDebiteur\n"
			+ "WHEN NOT EXISTS (SELECT 1 FROM Debiteur WHERE naam=NEW.naam AND straat=NEW.straat AND nummer=NEW.nummer AND postcode=NEW. postcode AND plaats=NEW.plaats)\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "INSERT INTO Debiteur(naam, straat, nummer, postcode, plaats)\n"
			+ "SELECT NEW.naam, NEW.straat, NEW.nummer, NEW.postcode, NEW.plaats;\n"
			+ "\n"
			+ "INSERT INTO BTWDebiteur (debiteurID, btwNummer)\n"
			+ "SELECT Debiteur.debiteurID, NEW.btwNummer\n"
			+ "FROM Debiteur\n"
			+ "WHERE NEW.btwNummer IS NOT NULL AND naam=NEW.naam AND straat=NEW.straat AND nummer=NEW.nummer AND postcode=NEW.postcode AND plaats=NEW.plaats\n"
			+ "AND NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE debiteurID=Debiteur.debiteurID AND btwNummer=NEW.btwNummer);\n"
			+ "\n"
			+ "END;"),
	
	CREATE_TOTAAL_DEBITEUR_DELETE_TRIGGER("CREATE TRIGGER DeleteDebiteurBtwDebiteur\n"
			+ "INSTEAD OF DELETE ON TotaalDebiteur\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "DELETE FROM BTWDebiteur\n"
			+ "WHERE BTWDebiteur.btwNummer = OLD.btwNummer\n"
			+ "AND BTWDebiteur.btwNummer = (SELECT btwNummer FROM BTWDebiteur WHERE debiteurID = (SELECT debiteurID FROM Debiteur WHERE naam = OLD.naam AND straat = OLD.straat AND nummer = OLD.nummer AND postcode = OLD.postcode AND plaats = OLD.plaats));\n"
			+ "\n"
			+ "DELETE FROM Debiteur\n"
			+ "WHERE naam = OLD.naam AND straat = OLD.straat AND nummer = OLD.nummer AND postcode = OLD.postcode AND plaats = OLD.plaats;\n"
			+ "\n"
			+ "END;"),

	CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_COMPLEX_TRIGGER("CREATE TRIGGER UpdateComplexToComplexDebiteur\n"
			+ "INSTEAD OF UPDATE ON TotaalDebiteur\n"
			+ "WHEN EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)\n"
			+ "AND NEW.btwNummer IS NOT NULL\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "UPDATE BTWDebiteur\n"
			+ "SET btwNummer = NEW.btwNummer\n"
			+ "WHERE BTWDebiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "UPDATE Debiteur\n"
			+ "SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats\n"
			+ "WHERE Debiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "END;"),

	CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_SIMPLE_TRIGGER("CREATE TRIGGER UpdateComplexToSimpleDebiteur\n"
			+ "INSTEAD OF UPDATE ON TotaalDebiteur\n"
			+ "WHEN EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)\n"
			+ "AND NEW.btwNummer IS NULL\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "DELETE FROM BTWDebiteur\n"
			+ "WHERE BTWDebiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "UPDATE Debiteur\n"
			+ "SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats\n"
			+ "WHERE Debiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "END;"),

	CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_COMPLEX_TRIGGER("CREATE TRIGGER UpdateSimpleToComplexDebiteur\n"
			+ "INSTEAD OF UPDATE ON TotaalDebiteur\n"
			+ "WHEN NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)\n"
			+ "AND NEW.btwNummer IS NOT NULL\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "INSERT INTO BTWDebiteur (debiteurID, btwNummer) VALUES (OLD.debiteurID, NEW.btwNummer);\n"
			+ "\n"
			+ "UPDATE Debiteur\n"
			+ "SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats\n"
			+ "WHERE Debiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "END;"),

	CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_SIMPLE_TRIGGER("CREATE TRIGGER UpdateSimpleToSimpleDebiteur\n"
			+ "INSTEAD OF UPDATE ON TotaalDebiteur\n"
			+ "WHEN NOT EXISTS (SELECT 1 FROM BTWDebiteur WHERE BTWDebiteur.debiteurID = OLD.debiteurID)\n"
			+ "AND NEW.btwNummer IS NULL\n"
			+ "\n"
			+ "BEGIN\n"
			+ "\n"
			+ "UPDATE Debiteur\n"
			+ "SET naam = NEW.naam, straat = NEW.straat, nummer = NEW.nummer, postcode = NEW.postcode, plaats = NEW.plaats\n"
			+ "WHERE Debiteur.debiteurID = OLD.debiteurID;\n"
			+ "\n"
			+ "END;"),

	CREATE_METADATA("CREATE TABLE Metadata (version TEXT NOT NULL);");

	private final String query;

	V04AlphaQueries(String query) {
		this.query = query;
	}

	@Override
	public String getQuery() {
		return this.query;
	}
}
