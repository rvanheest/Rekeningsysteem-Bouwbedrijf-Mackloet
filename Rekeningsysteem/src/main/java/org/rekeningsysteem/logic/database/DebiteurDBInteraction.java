package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiFunction;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

public class DebiteurDBInteraction extends DBInteraction<Debiteur> {

	private static final Function<String, QueryEnumeration> naamQuery = s -> () ->
		"SELECT * FROM TotaalDebiteur WHERE naam LIKE '%" + s + "%';";
	private static final Function<Debiteur, QueryEnumeration> debQuery = deb -> () ->
		"SELECT * FROM TotaalDebiteur WHERE naam = '" + deb.naam()
			+ "' AND straat = '" + deb.straat() + "' AND nummer = '" + deb.nummer()
			+ "' AND postcode = '" + deb.postcode() + "' AND plaats = '" + deb.plaats()
			+ deb.btwNummer().map(s -> "' AND btwNummer = '" + s).orElse("") + "';";
	private static final QueryEnumeration allQuery = () -> "SELECT * FROM TotaalDebiteur;";

	private static final Function<Debiteur, QueryEnumeration> debInsert = deb -> () ->
		"INSERT INTO TotaalDebiteur (naam, straat, nummer, postcode, plaats"
			+ deb.btwNummer().map(s -> ", btwNummer").orElse("") + ") VALUES ('"
			+ deb.naam() + "', '" + deb.straat() + "', '"
			+ deb.nummer() + "', '" + deb.postcode() + "', '" + deb.plaats()
			+ deb.btwNummer().map(s -> "', '" + s).orElse("") + "');";
	private static final Function<Integer, QueryEnumeration> debDelete = id -> () ->
		"DELETE FROM TotaalDebiteur WHERE debiteurID = '" + id + "';";
	private static final BiFunction<Integer, Debiteur, QueryEnumeration> debUpdate = (oldID, debNew) -> () ->
		"UPDATE TotaalDebiteur SET naam = '" + debNew.naam()
			+ "', straat = '" + debNew.straat() + "', nummer = '" + debNew.nummer()
			+ "', postcode = '" + debNew.postcode() + "', plaats = '" + debNew.plaats()
			+ "', btwNummer = " + debNew.btwNummer().map(s -> "'" + s + "'").orElse("NULL")
			+ " WHERE debiteurID = " + oldID + ";";

	public DebiteurDBInteraction(Database database) {
		super(database);
	}

	public Completable addDebiteur(Debiteur debiteur) {
		return this.update(debInsert.apply(debiteur));
	}

	public Observable<Debiteur> addAndGetDebiteur(Debiteur debiteur) {
		return this.update(debInsert.apply(debiteur))
			.andThen(Observable.defer(() -> this.getFromDatabase(debQuery.apply(debiteur))));
	}

	public Completable deleteDebiteur(Debiteur debiteur) {
		return this.update(
			debiteur.debiteurID()
				.map(debDelete)
				.orElseThrow(() -> new IllegalArgumentException("This debiteur does not contain an id: " + debiteur))
		);
	}

	public Completable updateDebiteur(Debiteur oldDebiteur, Debiteur newDebiteur) {
		return this.update(
			oldDebiteur.debiteurID()
				.map(id -> debUpdate.apply(id, newDebiteur))
				.orElseThrow(() -> new IllegalArgumentException("This debiteur does not contain an id: " + oldDebiteur))
		);
	}

	public Observable<Debiteur> getAll() {
		return this.getFromDatabase(allQuery);
	}

	public Observable<Debiteur> getWithNaam(String text) {
		return this.getFromDatabase(naamQuery, text);
	}

	@Override
	Debiteur resultExtractor(ResultSet result) throws SQLException {
		int debiteurId = result.getInt("debiteurID");
		String naam = result.getString("naam");
		String straat = result.getString("straat");
		String nummer = result.getString("nummer");
		String postcode = result.getString("postcode");
		String plaats = result.getString("plaats");
		String btwNummer = result.getString("btwNummer");

		return new Debiteur(debiteurId, naam, straat, nummer, postcode, plaats, btwNummer);
	}
}
