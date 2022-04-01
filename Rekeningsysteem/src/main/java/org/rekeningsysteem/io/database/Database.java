package org.rekeningsysteem.io.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

public class Database implements AutoCloseable {

	private static Database __instance;

	public static Database getInstance() throws SQLException {
		if (__instance == null) {
			__instance = new Database();
		}
		return __instance;
	}

	private final Connection connection;

	private Database() throws SQLException {
		this(PropertiesWorker.getInstance());
	}

	private Database(PropertiesWorker worker) throws SQLException {
		this(worker.getProperty(PropertyModelEnum.DATABASE)
			.map(File::new)
			.orElseThrow(() -> new SQLException("Did not find the database location.")));
	}

	public Database(File file) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
		}
		catch (ClassNotFoundException e) {
			throw new SQLException("Class \"org.sqlite.JDBC\" was not found", e);
		}
	}

	@Override
	public void close() throws SQLException {
		__instance = null;
		this.connection.close();
	}

	public Completable update(QueryEnumeration query) {
		return this.updateAndCount(query).ignoreElement();
	}

	public Single<Integer> updateAndCount(QueryEnumeration query) {
		return Single.using(
			this.connection::createStatement,
			statement -> Single.just(statement.executeUpdate(query.getQuery())),
			Statement::close
		);
	}

	public <A> Observable<A> query(QueryEnumeration query, Function<ResultSet, A> resultComposer) {
		return Observable.using(
			this.connection::createStatement,
			statement -> Observable.using(
				() -> statement.executeQuery(query.getQuery()),
				resultSet -> Observable.generate(
					() -> resultSet,
					(result, emitter) -> {
						if (result.next())
							emitter.onNext(resultComposer.apply(result));
						else
							emitter.onComplete();
					}
				),
				ResultSet::close
			),
			Statement::close
		);
	}
}
