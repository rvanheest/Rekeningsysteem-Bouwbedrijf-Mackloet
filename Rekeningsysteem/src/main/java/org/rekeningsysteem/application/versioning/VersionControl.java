package org.rekeningsysteem.application.versioning;

import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class VersionControl {

	private static final Func1<String, QueryEnumeration> insertVersion = version -> () ->
			"INSERT INTO Metadata VALUES ('" + version + "');";

	private final Database database;

	public VersionControl(Database database) {
		this.database = database;
	}

	public Observable<Integer> checkDBVersioning() {
		return this.versionTableExists()
				.flatMap(this::existsToVersion)
				.flatMap(this::updateDatabase);
	}

	private Observable<String> existsToVersion(boolean exists) {
		if (exists) { // Database already existed with version table
			return this.getVersion();
		}
		return this.getTableCount().map(count -> count == 0
				? "" // Database is newly created
				: "v0.3"); // version v0.3 or less applies, which doesn't have a version table
	}

	private Observable<Integer> updateDatabase(String dbVersion) {
		String mavenVersion = getMavenVersion();
		if (dbVersion.equals(mavenVersion)) {
			// db is up to date
			return Observable.empty();
		}
		else if (dbVersion.isEmpty()) {
			// Database is newly created
			return this.initDatabase(mavenVersion);
		}
		else if ("v0.3".equals(dbVersion)) {
			QueryEnumeration query = getV04Queries()
					.append(insertVersion.call(mavenVersion));
			return this.database.update(query).subscribeOn(Schedulers.io());
		}
		return Observable.error(new IllegalArgumentException("the version of the database "
				+ "is not valid"));
	}

	public static String getMavenVersion() {
		// return "v0.4";
		// TODO switch lines
		return VersionControl.class.getPackage().getImplementationVersion();
	}

	public Observable<Boolean> versionTableExists() {
		return this.database.query(V04Queries.VERSION_TABLE_EXISTS,
				result -> result.getString("name"))
				.subscribeOn(Schedulers.io())
				.isEmpty()
				.map(b -> !b);
	}

	public Observable<String> getVersion() {
		return this.database.query(V04Queries.GET_DB_VERSION,
				result -> result.getString("version"))
				.subscribeOn(Schedulers.io());
	}

	public Observable<Integer> getTableCount() {
		return this.database.query(V04Queries.TABLE_COUNT,
				result -> result.getInt("count"))
				.subscribeOn(Schedulers.io());
	}

	public Observable<Integer> initDatabase(String version) {
		QueryEnumeration query = V03Queries.CREATE_ARTIKELLIJST
				.append(getV04Queries())
				.append(insertVersion.call(version));

		return this.database.update(query);
	}

	public static QueryEnumeration getV03Queries() {
		return V03Queries.CREATE_ARTIKELLIJST
				.append(V03Queries.CREATE_DEBITEUR)
				.append(V03Queries.CREATE_ART_NR_INDEX)
				.append(V03Queries.CREATE_NAME_SEARCH);
	}

	public static QueryEnumeration getV04Queries() {
		return V04Queries.DROP_ART_NR_INDEX
				.append(V04Queries.DROP_NAME_SEARCH_INDEX)
				.append(V04Queries.DROP_DEBITEUR_TABLE)
				.append(V04Queries.CREATE_ARTIKELNUMMER_INDEX)
				.append(V04Queries.CREATE_OMSCHRIJVING_INDEX)
				.append(V04Queries.CREATE_DEBITEUR)
				.append(V04Queries.CREATE_BTW_DEBITEUR)
				.append(V04Queries.CREATE_TOTAAL_DEBITEUR_VIEW)
				.append(V04Queries.CREATE_TOTAAL_DEBITEUR_INSERT_TRIGGER)
				.append(V04Queries.CREATE_TOTAAL_DEBITEUR_DELETE_TRIGGER)
				.append(V04Queries.CREATE_TOTAAL_DEBITEUR_UPDATE_TRIGGER)
				.append(V04Queries.CREATE_METADATA);
	}
}
