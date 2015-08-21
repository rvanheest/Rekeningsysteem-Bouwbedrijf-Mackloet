package org.rekeningsysteem.test.io.database;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.io.database.Database;

import rx.observers.TestSubscriber;

public class DatabaseTest {

	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Database database;

	@Before
	public void setUp() throws IOException, SQLException {
		this.database = new Database(this.folder.newFile("database.db"));
	}

	@After
	public void tearDown() throws SQLException {
		this.database.close();
	}

	@Test
	public void testMakeTable() {
		TestSubscriber<Integer> observer = new TestSubscriber<>();
		this.database.update(() -> "CREATE TABLE Metadata (version TEXT NOT NULL);")
				.subscribe(observer);

		observer.assertValue(0);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testInsertData() {
		this.testMakeTable();

		TestSubscriber<Integer> observer = new TestSubscriber<>();
		this.database.update(() -> "INSERT INTO Metadata VALUES ('v0.4');")
				.subscribe(observer);

		observer.assertValue(1);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testRetrieveData() {
		this.testInsertData();

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.database.query(() -> "SELECT version FROM Metadata;", result -> {
			return result.getString("version");
		}).subscribe(observer);

		observer.assertValue("v0.4");
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testErrorInUpdate() {
		TestSubscriber<Integer> observer = new TestSubscriber<>();
		this.database.update(() -> "CRE ATE TABLE Metadata (version TEXT NOT NULL);")
				.subscribe(observer);

		observer.assertNoValues();
		observer.assertError(SQLException.class);
		observer.assertNotCompleted();
	}

	@Test
	public void testErrorInQuery() {
		this.testInsertData();

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.database.query(() -> "SE LECT version FROM Metadata;",
				result -> result.getString("version"))
				.subscribe(observer);

		observer.assertNoValues();
		observer.assertError(SQLException.class);
		observer.assertNotCompleted();
	}

	@Test
	public void testOpenRealDatabase() throws SQLException {
		this.database = Database.getInstance();

		TestSubscriber<String> observer = new TestSubscriber<>();
		this.database.query(() -> "SELECT * FROM sqlite_master LIMIT 3;",
				result -> result.getString(1))
				.subscribe(observer);

		observer.assertValueCount(3);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	@Test
	public void testOpenRealDatabaseTwice() throws SQLException {
		this.testOpenRealDatabase();
		
		Database prev = this.database;
		this.database = Database.getInstance();
		
		assertEquals(prev, this.database);
	}
}
