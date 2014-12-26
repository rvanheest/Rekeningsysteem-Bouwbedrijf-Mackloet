package org.rekeningsysteem.io.database;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import rx.Observable;
import rx.functions.Func5;

public class InitDB {

	public static void main(String[] args) throws IOException {
		try (Database appDB = new Database(new File("AppDB.db"))) {
			Observable<Integer> debDrop = appDB.update("DROP TABLE IF EXISTS Debiteur;");
			Observable<Integer> artDrop = appDB.update("DROP TABLE IF EXISTS Artikellijst;");
			Observable<Integer> debIndexDrop = appDB.update("DROP INDEX IF EXISTS NameSearch;");
			Observable<Integer> artIndexDrop = appDB.update("DROP INDEX IF EXISTS ArtikelnummerIndex;");

			Observable<Integer> debTable = appDB.update("CREATE TABLE Debiteur("
					+ "debiteurID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ "naam TEXT NOT NULL, "
					+ "straat TEXT NOT NULL, "
					+ "nummer TEXT NOT NULL, "
					+ "postcode TEXT NOT NULL, "
					+ "plaats TEXT NOT NULL, "
					+ "btwnummer TEXT);");
			Observable<Integer> artTable = appDB.update("CREATE TABLE Artikellijst("
					+ "artikelnummer TEXT NOT NULL PRIMARY KEY, "
					+ "omschrijving TEXT NOT NULL, "
					+ "prijsPer NUMERIC NOT NULL, "
					+ "eenheid TEXT NOT NULL, "
					+ "verkoopprijs NUMERIC NOT NULL);");

			Observable<Integer> debIndex = appDB.update("CREATE INDEX NameSearch "
					+ "ON Debiteur(naam);");
			Observable<Integer> artIndex = appDB.update("CREATE INDEX ArtNrIndex "
					+ "ON Artikellijst(artikelnummer);");

			Func5<String, String, String, String, String, String> f = (s1, s2, s3, s4, s5) ->
					"INSERT INTO Artikellijst VALUES ('" + s1 + "', '" + s2 + "', '" + s3 + "', '"
							+ s4 + "', '" + s5 + "');";

			Observable<Integer> inserts = Observable.concat(
					appDB.update(f.call("123456", "test123", "13", "foo", "15.93")),
					appDB.update(f.call("456789", "test456", "10", "bar", "15.93")),
					appDB.update(f.call("789123", "test789", "10", "baz", "15.93")),
					appDB.update(f.call("147258", "test147", "17", "qux", "15.93")),
					appDB.update(f.call("258369", "test258", "11", "quux", "15.93")),
					appDB.update(f.call("369147", "test369", "19", "corge", "15.93")),
					appDB.update(f.call("159357", "test159", "19", "grault", "15.93")),
					appDB.update(f.call("357159", "test357", "12", "garply", "15.93")));

			debDrop.map(i -> "old table removed - " + i)
					.concatWith(artDrop.map(i -> "old table removed - " + i))
					.concatWith(debIndexDrop.map(i -> "old index dropped - " + i))
					.concatWith(artIndexDrop.map(i -> "old index dropped - " + i))
					.concatWith(debTable.map(i -> "table created - " + i))
					.concatWith(artTable.map(i -> "table created - " + i))
					.concatWith(debIndex.map(i -> "index created - " + i))
					.concatWith(artIndex.map(i -> "index created - " + i))
					.concatWith(inserts.map(i -> "inserted row - " + i))
					.subscribe(System.out::println, Throwable::printStackTrace,
							() -> System.exit(0));

			System.in.read();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
