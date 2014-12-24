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
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Database implements AutoCloseable {

	private static Database __instance;

	public static Database getInstance() throws SQLException {
		if (__instance == null) {
			__instance = new Database();
		}
		return __instance;
	}

	public static void closeInstance() throws SQLException {
		if (__instance != null) {
			__instance.close();
		}
	}

	private final Connection connection;

	private Database() throws SQLException {
		this(PropertiesWorker.getInstance());
	}

	private Database(PropertiesWorker worker) throws SQLException {
		try {
			Class.forName("org.sqlite.JDBC");
			
			File file = worker.getProperty(PropertyModelEnum.DATABASE).map(File::new).orElseThrow(() -> new SQLException("Did not find the database location."));
			
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
		}
		catch (ClassNotFoundException e) {
			throw new SQLException("Class \"org.sqlite.JDBC\" was not found", e);
		}
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

	public Observable<Integer> update(String query) {
		String s = "UPDATE: " + query;
		System.out.println(s);
		return Observable.create((Subscriber<? super Integer> subscriber) -> {
			System.out.println(s + " - start: " + Thread.currentThread().getName());
			try (Statement statement = this.connection.createStatement()) {
				System.out.println(s + " - try 1: " + Thread.currentThread().getName());
				subscriber.onNext(statement.executeUpdate(query));
				System.out.println(s + " - try 2: " + Thread.currentThread().getName());
				subscriber.onCompleted();
				System.out.println(s + " - try 3: " + Thread.currentThread().getName());
			}
			catch (SQLException e) {
				subscriber.onError(e);
			}
			System.out.println(s + " - end: " + Thread.currentThread().getName());
		}).subscribeOn(Schedulers.io());
	}

	public <A> Observable<A> query(String query, ExFunc1<ResultSet, A> resultComposer) {
		String s = "UPDATE: " + query;
		System.out.println(s);
		return Observable.create((Subscriber<? super A> subscriber) -> {
			System.out.println(s + " - start: " + Thread.currentThread().getName());
			try (Statement statement = this.connection.createStatement();
					ResultSet result = statement.executeQuery(query)) {
				while (result.next()) {
					System.out.println(s + " - try 1: " + Thread.currentThread().getName());
					try {
						subscriber.onNext(resultComposer.call(result));
					}
					catch (Exception e) {
						subscriber.onError(e);
					}
				}
				System.out.println(s + " - try 2: " + Thread.currentThread().getName());
				subscriber.onCompleted();
				System.out.println(s + " - try 3: " + Thread.currentThread().getName());
			}
			catch (SQLException e) {
				subscriber.onError(e);
			}
			System.out.println(s + " - end: " + Thread.currentThread().getName());
		}).subscribeOn(Schedulers.io());
	}
}
