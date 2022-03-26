package org.rekeningsysteem.test.application.versioning;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.application.versioning.V04AlphaQueries;
import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.io.database.Database;

public class VersionControlTest {

	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Database database;
	private VersionControl vc;

	@Before
	public void setUp() throws IOException, SQLException {
		File file = this.folder.newFile("database.db");
		this.database = new Database(file);
		this.vc = new VersionControl(this.database);
	}

	@After
	public void tearDown() throws Exception {
		this.database.close();
	}

	@Test
	public void testGetTableCountNoTables() {
		this.vc.getTableCount()
			.test()
			.assertValue(0)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetTableCountNotEmpty() {
		this.database.update(() -> "CREATE TABLE TestTable1 (foo TEXT, bar TEXT);\n"
				+ "CREATE TABLE TestTable2 (tableID INTEGER PRIMARY KEY, test TEXT);")
			.toObservable()
			.concatWith(this.vc.getTableCount())
			.test()
			.assertValue(2)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testMetadataExistsFalse() {
		this.vc.metadataExists()
			.test()
			.assertValue(false)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testMetadataExistsTrue() {
		this.database.update(V04AlphaQueries.CREATE_METADATA.append(() -> "INSERT INTO Metadata VALUES ('abcde');"))
			.toObservable()
			.concatWith(this.vc.metadataExists())
			.test()
			.assertValue(true)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetVersion() {
		this.database.update(V04AlphaQueries.CREATE_METADATA.append(() -> "INSERT INTO Metadata VALUES ('abcde');"))
			.toObservable()
			.concatWith(this.vc.metadataExists())
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(true, "abcde")
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromEmpty() {
		this.vc.checkDBVersioning()
			.toObservable()
			.concatWith(this.vc.getTableCount())
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(5, VersionControl.getAppVersion())
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromEmptyOnDifferentThread() throws InterruptedException {
		this.vc.checkDBVersioning()
			.subscribeOn(Schedulers.io())
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.vc.getTableCount()
			.cast(Object.class)
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(5, VersionControl.getAppVersion())
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromV03Alpha() {
		this.database.update(VersionControl.getV03AlphaQueries())
			.toObservable()
			.cast(Object.class)
			.concatWith(this.vc.metadataExists())
			.concatWith(this.vc.checkDBVersioning())
			.concatWith(this.vc.getTableCount())
			.concatWith(this.vc.getVersion())
			.concatWith(this.vc.metadataExists())
			.test()
			.assertNoErrors()
			.assertValues(false, 5, VersionControl.getAppVersion(), true)
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromV03AlphaOnDifferentThread() throws InterruptedException {
		this.database.update(VersionControl.getV03AlphaQueries())
			.toObservable()
			.concatWith(this.vc.metadataExists())
			.test()
			.await()
			.assertValue(false)
			.assertNoErrors()
			.assertComplete();

		this.vc.checkDBVersioning()
			.subscribeOn(Schedulers.io())
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.vc.getTableCount()
			.cast(Object.class)
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(5, VersionControl.getAppVersion())
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromV04Alpha() {
		this.database.update(VersionControl.getV04AlphaQueries())
			.toObservable()
			.cast(Object.class)
			.concatWith(this.vc.metadataExists())
			.concatWith(this.vc.checkDBVersioning())
			.concatWith(this.vc.getTableCount())
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(true, 5, VersionControl.getAppVersion())
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testCheckDBVersioningFromV04AlphaOnDifferentThread() throws InterruptedException {
		this.database.update(VersionControl.getV04AlphaQueries())
			.toObservable()
			.concatWith(this.vc.metadataExists())
			.test()
			.assertValue(true)
			.assertNoErrors()
			.assertComplete();

		this.vc.checkDBVersioning()
			.subscribeOn(Schedulers.io())
			.test()
			.await()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.vc.getTableCount()
			.cast(Object.class)
			.concatWith(this.vc.getVersion())
			.test()
			.assertValues(5, VersionControl.getAppVersion())
			.assertNoErrors()
			.assertComplete();
	}
}
