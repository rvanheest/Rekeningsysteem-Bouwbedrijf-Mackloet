package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func1;

public abstract class DBInteraction<T> {

	private final Database database;

	DBInteraction(Database database) {
		this.database = database;
	}

	Observable<T> getFromDatabase(Func1<String, QueryEnumeration> factory, String text) {
		return this.getFromDatabase(factory.call(text.replace("\'", "\'\'")));
	}

	Observable<T> getFromDatabase(QueryEnumeration query) {
		return this.database.query(query, this::resultExtractor);
	};

	abstract T resultExtractor(ResultSet result) throws SQLException;

	Observable<Integer> update(QueryEnumeration query) {
		return this.database.update(query);
	}
}
