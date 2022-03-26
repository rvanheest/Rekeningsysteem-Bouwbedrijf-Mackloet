package org.rekeningsysteem.application.versioning;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

public class VersionControl {

	private final Database database;

	public VersionControl(Database database) {
		this.database = database;
	}

	public Maybe<Integer> getTableCount() {
		return this.database
			.query(V04AlphaQueries.TABLE_COUNT, result -> result.getInt("count"))
			.firstElement();
	}

	public Maybe<Boolean> metadataExists() {
		return this.database
			.query(V04AlphaQueries.METADATA_EXISTS, result -> result.getInt(1) == 1)
			.firstElement();
	}

	public Maybe<String> getVersion() {
		return this.database
			.query(V04AlphaQueries.GET_DB_VERSION, result -> result.getString("version"))
			.firstElement();
	}

	public Completable checkDBVersioning() {
		return this.getTableCount()
			.cache()
			.flatMap(count -> count <= 0
				// table count == 0 => newly created
				// --> init database
				// --> add version number
				? Maybe.just(getFromEmptyDBQueries())
				// table count > 0 (== 3) => v0.3-alpha or higher
				: this.v03AlphaOrHigher()
			)
			.flatMapCompletable(this.database::update);
	}

	private Maybe<QueryEnumeration> v03AlphaOrHigher() {
		return this.metadataExists()
			.cache()
			.flatMap(exists -> !exists
				// no version table => v0.3-alpha
				? Maybe.just(getFromV03AlphaToV04AlphaQuery())
				// v0.4-alpha
				: Maybe.empty());
	}

	public static String getAppVersion() {
		return "v0.4-alpha";
	}

	public static QueryEnumeration getVersionInsertQuery() {
		return () -> "INSERT INTO Metadata VALUES ('" + getAppVersion() + "');";
	}

	public static QueryEnumeration getFromEmptyDBQueries() {
		return getV04AlphaQueries();
	}

	public static QueryEnumeration getV03AlphaQueries() {
		return V03AlphaQueries.CREATE_ARTIKELLIJST
			.append(V03AlphaQueries.CREATE_DEBITEUR)
			.append(V03AlphaQueries.CREATE_ART_NR_INDEX)
			.append(V03AlphaQueries.CREATE_NAME_SEARCH);
	}

	public static QueryEnumeration getFromV03AlphaToV04AlphaQuery() {
		return V04AlphaQueries.DROP_ART_NR_INDEX
			.append(V04AlphaQueries.DROP_NAME_SEARCH_INDEX)
			.append(V04AlphaQueries.DROP_DEBITEUR_TABLE)
			.append(V04AlphaQueries.CREATE_ARTIKELNUMMER_INDEX)
			.append(V04AlphaQueries.CREATE_OMSCHRIJVING_INDEX)
			.append(V04AlphaQueries.CREATE_DEBITEUR)
			.append(V04AlphaQueries.CREATE_BTW_DEBITEUR)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_VIEW)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_INSERT_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_DELETE_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_COMPLEX_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_SIMPLE_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_COMPLEX_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_SIMPLE_TRIGGER)
			.append(V04AlphaQueries.CREATE_METADATA)
			.append(getVersionInsertQuery());
	}

	public static QueryEnumeration getV04AlphaQueries() {
		return V03AlphaQueries.CREATE_ARTIKELLIJST
			.append(V04AlphaQueries.CREATE_ARTIKELNUMMER_INDEX)
			.append(V04AlphaQueries.CREATE_OMSCHRIJVING_INDEX)
			.append(V04AlphaQueries.CREATE_DEBITEUR)
			.append(V04AlphaQueries.CREATE_BTW_DEBITEUR)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_VIEW)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_INSERT_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_DELETE_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_COMPLEX_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_COMPLEX_TO_SIMPLE_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_COMPLEX_TRIGGER)
			.append(V04AlphaQueries.CREATE_TOTAAL_DEBITEUR_UPDATE_SIMPLE_TO_SIMPLE_TRIGGER)
			.append(V04AlphaQueries.CREATE_METADATA)
			.append(getVersionInsertQuery());
	}

	/*
	 * newly created:
	 * 		table count = 0
	 * 		--> init database
	 * 		--> add version number
	 * v0.3-alpha:
	 * 		table count > 0 (= 3)
	 * 		no version table
	 * 		--> v04 patch
	 * 		--> add version number
	 * v0.4-alpha:
	 * 		database up to date
	 */
}
