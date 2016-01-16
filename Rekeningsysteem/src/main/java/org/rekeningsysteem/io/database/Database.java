package org.rekeningsysteem.io.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import rx.Observable;

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

	public Observable<Integer> update(QueryEnumeration query) {
		return Observable.<Integer> create(subscriber -> {
			try (Statement statement = this.connection.createStatement()) {
				subscriber.onNext(statement.executeUpdate(query.getQuery()));
				subscriber.onCompleted();
			}
			catch (SQLException e) {
				subscriber.onError(e);
			}
		}).cache();
	}

	public <A> Observable<A> query(QueryEnumeration query, ExFunc1<ResultSet, A> resultComposer) {
		return Observable.<A> create(subscriber -> {
			try (Statement statement = this.connection.createStatement();
					ResultSet result = statement.executeQuery(query.getQuery())) {
				while (!subscriber.isUnsubscribed() && result.next()) {
					subscriber.onNext(resultComposer.call(result));
				}
				subscriber.onCompleted();
			}
			catch (Exception e) {
				subscriber.onError(e);
			}
		}).cache();
	}
}
