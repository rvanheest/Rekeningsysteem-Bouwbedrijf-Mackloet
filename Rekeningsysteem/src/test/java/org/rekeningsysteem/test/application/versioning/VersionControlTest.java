package org.rekeningsysteem.test.application.versioning;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.application.versioning.V04AlphaQueries;
import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.io.database.Database;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

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
	public void tearDown() throws SQLException {
		this.database.close();
	}

	@Test
	public void testGetTableCountNoTables() {
		TestSubscriber<Integer> observer = new TestSubscriber<>();
		this.vc.getTableCount().subscribe(observer);

		observer.assertValue(0);
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testGetTableCountNotEmpty() {
		TestSubscriber<Integer> observer = new TestSubscriber<>();
		this.database.update(() -> "CREATE TABLE TestTable1 (foo TEXT, bar TEXT);\n"
				+ "CREATE TABLE TestTable2 (tableID INTEGER PRIMARY KEY, test TEXT);")
				.ignoreElements()
				.concatWith(this.vc.getTableCount())
				.subscribe(observer);

		observer.assertValue(2);
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testMetadataExistsFalse() {
		TestSubscriber<Boolean> observer = new TestSubscriber<>();
		this.vc.metadataExists().subscribe(observer);

		observer.assertValue(false);
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testMetadataExistsTrue() {
		TestSubscriber<Object> observer = new TestSubscriber<>();
		Observable.concat(
				this.database.update(V04AlphaQueries.CREATE_METADATA
						.append(() -> "INSERT INTO Metadata VALUES ('abcde');"))
						.ignoreElements(),
				this.vc.metadataExists())
				.subscribe(observer);

		observer.assertValue(true);
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testGetVersion() {
		TestSubscriber<Object> observer = new TestSubscriber<>();
		Observable.concat(
				this.database.update(V04AlphaQueries.CREATE_METADATA
						.append(() -> "INSERT INTO Metadata VALUES ('abcde');"))
						.ignoreElements(),
				this.vc.metadataExists(),
				this.vc.getVersion())
				.subscribe(observer);

		observer.assertValues(true, "abcde");
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testCheckDBVersioningFromEmpty() {
		TestSubscriber<Object> observer = new TestSubscriber<>();
		Observable.concat(
				this.vc.checkDBVersioning().ignoreElements(),
				this.vc.getTableCount(),
				this.vc.getVersion())
				.subscribe(observer);
		
		observer.assertValues(5, VersionControl.getAppVersion());
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testCheckDBVersioningFromEmptyOnDifferentThread() {
		TestSubscriber<Integer> observer1 = new TestSubscriber<>();
		TestSubscriber<Object> observer2 = new TestSubscriber<>();
		
		this.vc.checkDBVersioning()
				.subscribeOn(Schedulers.io())
				.subscribe(observer1);
		
		observer1.awaitTerminalEvent();
		observer1.assertValue(1);
		observer1.assertNoErrors();
		observer1.assertCompleted();
		observer1.isUnsubscribed();
		
		Observable.concat(this.vc.getTableCount(), this.vc.getVersion())
				.subscribe(observer2);
		
		observer2.assertValues(5, VersionControl.getAppVersion());
		observer2.assertNoErrors();
		observer2.assertCompleted();
		observer2.assertUnsubscribed();
	}

	@Test
	public void testCheckDBVersioningFromV03Alpha() {
		TestSubscriber<Object> observer = new TestSubscriber<>();
		Observable.concat(
				this.database.update(VersionControl.getV03AlphaQueries()).ignoreElements(),
				this.vc.metadataExists(),
				this.vc.checkDBVersioning().ignoreElements(),
				this.vc.getTableCount(),
				this.vc.getVersion())
				.subscribe(observer);

		observer.assertValues(false, 5, VersionControl.getAppVersion());
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}

	@Test
	public void testCheckDBVersioningFromV03AlphaOnDifferentThread() {
		TestSubscriber<Object> observer0 = new TestSubscriber<>();
		TestSubscriber<Integer> observer1 = new TestSubscriber<>();
		TestSubscriber<Object> observer2 = new TestSubscriber<>();
		
		Observable.concat(
				this.database.update(VersionControl.getV03AlphaQueries()).ignoreElements(),
				this.vc.metadataExists())
				.subscribe(observer0);
		
		observer0.assertValue(false);
		observer0.assertNoErrors();
		observer0.assertCompleted();
		observer0.assertUnsubscribed();
		
		this.vc.checkDBVersioning()
        		.subscribeOn(Schedulers.io())
        		.subscribe(observer1);
        
        observer1.awaitTerminalEvent();
        observer1.assertValue(1);
        observer1.assertNoErrors();
        observer1.assertCompleted();
        observer1.isUnsubscribed();

		Observable.concat(this.vc.getTableCount(), this.vc.getVersion())
				.subscribe(observer2);

		observer2.assertValues(5, VersionControl.getAppVersion());
		observer2.assertNoErrors();
		observer2.assertCompleted();
		observer2.assertUnsubscribed();
	}

	@Test
	public void testCheckDBVersioningFromV04Alpha() {
		TestSubscriber<Object> observer = new TestSubscriber<>();
		Observable.concat(
				this.database.update(VersionControl.getV04AlphaQueries()).ignoreElements(),
				this.vc.metadataExists(),
				this.vc.checkDBVersioning().ignoreElements(),
				this.vc.getTableCount(),
				this.vc.getVersion())
				.subscribe(observer);

		observer.assertValues(true, 5, VersionControl.getAppVersion());
		observer.assertNoErrors();
		observer.assertCompleted();
		observer.assertUnsubscribed();
	}
	
	@Test
	public void testCheckDBVersioningFromV04AlphaOnDifferentThread() {
		TestSubscriber<Object> observer0 = new TestSubscriber<>();
		TestSubscriber<Integer> observer1 = new TestSubscriber<>();
		TestSubscriber<Object> observer2 = new TestSubscriber<>();
		
		Observable.concat(
				this.database.update(VersionControl.getV04AlphaQueries()).ignoreElements(),
				this.vc.metadataExists())
				.subscribe(observer0);
		
		observer0.assertValue(true);
		observer0.assertNoErrors();
		observer0.assertCompleted();
		observer0.assertUnsubscribed();
		
		this.vc.checkDBVersioning()
        		.subscribeOn(Schedulers.io())
        		.subscribe(observer1);
        
        observer1.awaitTerminalEvent();
        observer1.assertNoValues();
        observer1.assertNoErrors();
        observer1.assertCompleted();
        observer1.isUnsubscribed();

		Observable.concat(this.vc.getTableCount(), this.vc.getVersion())
				.subscribe(observer2);

		observer2.assertValues(5, VersionControl.getAppVersion());
		observer2.assertNoErrors();
		observer2.assertCompleted();
		observer2.assertUnsubscribed();
	}
}
