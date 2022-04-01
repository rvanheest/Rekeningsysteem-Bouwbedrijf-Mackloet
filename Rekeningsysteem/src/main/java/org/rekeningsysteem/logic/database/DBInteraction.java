package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

public abstract class DBInteraction<T> {

	private final Database database;

	DBInteraction(Database database) {
		this.database = database;
	}

	Observable<T> getFromDatabase(Function<String, QueryEnumeration> factory, String text) {
		return this.getFromDatabase(factory.apply(text.replace("\'", "\'\'")));
	}

	Observable<T> getFromDatabase(QueryEnumeration query) {
		return this.database.query(query, this::resultExtractor);
	};

	abstract T resultExtractor(ResultSet result) throws SQLException;

	Completable update(QueryEnumeration query) {
		return this.database.update(query);
	}

	Single<Integer> updateAndCount(QueryEnumeration query) {
		return this.database.updateAndCount(query);
	}
}
