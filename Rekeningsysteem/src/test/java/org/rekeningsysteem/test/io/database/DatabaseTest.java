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

public class DatabaseTest {

	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Database database;

	@Before
	public void setUp() throws IOException, SQLException {
		this.database = new Database(this.folder.newFile("database.db").toPath());
	}

	@After
	public void tearDown() throws Exception {
		this.database.close();
	}

	@Test
	public void testMakeTable() {
		this.database.update(() -> "CREATE TABLE Metadata (version TEXT NOT NULL);")
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testInsertData() {
		this.testMakeTable();

		this.database.update(() -> "INSERT INTO Metadata VALUES ('v0.4');")
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testRetrieveData() {
		this.testInsertData();

		this.database.query(() -> "SELECT version FROM Metadata;", result -> result.getString("version"))
			.test()
			.assertValue("v0.4")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testErrorInUpdate() {
		this.database.update(() -> "CRE ATE TABLE Metadata (version TEXT NOT NULL);")
			.test()
			.assertNoValues()
			.assertError(SQLException.class)
			.assertNotComplete();
	}

	@Test
	public void testErrorInQuery() {
		this.testInsertData();

		this.database.query(() -> "SE LECT version FROM Metadata;", result -> result.getString("version"))
			.test()
			.assertNoValues()
			.assertError(SQLException.class)
			.assertNotComplete();
	}

	@Test
	public void testOpenRealDatabase() throws SQLException {
		this.database = Database.getInstance();

		this.database.query(() -> "SELECT * FROM sqlite_master LIMIT 3;", result -> result.getString(1))
			.test()
			.assertValueCount(3)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testOpenRealDatabaseTwice() throws SQLException {
		this.testOpenRealDatabase();

		Database prev = this.database;
		this.database = Database.getInstance();

		assertEquals(prev, this.database);
	}
}
