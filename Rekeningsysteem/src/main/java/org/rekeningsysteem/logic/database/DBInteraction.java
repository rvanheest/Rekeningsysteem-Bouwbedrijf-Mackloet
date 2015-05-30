package org.rekeningsysteem.logic.database;

import java.sql.ResultSet;

import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.ExFunc1;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

public abstract class DBInteraction<T> {

	private final Database database;

	DBInteraction(Database database) {
		this.database = database;
	}

	Func1<Func0<QueryEnumeration>, Observable<T>> with() {
		return factory -> this.getFromDatabase(factory.call());
	}

	Func1<Func1<String, QueryEnumeration>, Observable<T>> with(String text) {
		return factory -> this.getFromDatabase(factory.call(text.replace("\'", "\'\'")));
	}

	Observable<T> getFromDatabase(QueryEnumeration query) {
		return this.database.query(query, this.resultExtractor());
	};

	abstract ExFunc1<ResultSet, T> resultExtractor();

	Observable<Integer> update(QueryEnumeration query) {
		return this.database.update(query);
	}
}
