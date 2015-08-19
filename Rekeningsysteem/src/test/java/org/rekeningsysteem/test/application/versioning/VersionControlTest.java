package org.rekeningsysteem.test.application.versioning;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;

import rx.observers.TestSubscriber;

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
	public void testSetupFromEmptyDatabase() throws InterruptedException {
		this.versionTableNotExists();
		this.tableCount(0);
		this.uptodateWithNewestVersion(false);
	}

	@Test
	public void testSetupFromErrorDatabase() throws InterruptedException {
		QueryEnumeration setupQueries = VersionControl.getV03AlphaQueries()
				.append(VersionControl.getV04AlphaQueries())
				.append(() -> "INSERT INTO Metadata VALUES ('v0.2');");
		this.database.update(setupQueries).subscribe();

		this.versionTableExists();
		this.tableCount(5);
		
		CountDownLatch latch = new CountDownLatch(1);
		TestSubscriber<Integer> updateObserver = new TestSubscriber<>();
		this.vc.checkDBVersioning()
				.subscribe(updateObserver::onNext,
				e -> { updateObserver.onError(e); latch.countDown(); },
				() -> { updateObserver.onCompleted(); latch.countDown(); });

		latch.await();
		updateObserver.assertNoValues();
		updateObserver.assertError(IllegalArgumentException.class);
		updateObserver.assertNotCompleted();
	}

	@Test
	public void testSetupFromV03AlphaDatabase() throws InterruptedException {
		QueryEnumeration setupQueries = VersionControl.getV03AlphaQueries();
		this.database.update(setupQueries).subscribe();
		
		this.versionTableNotExists();
		this.tableCount(3);
		this.uptodateWithNewestVersion(false);
	}

	@Test
	public void testSetupFromV04Database() throws InterruptedException {
		QueryEnumeration setupQueries = VersionControl.getV03AlphaQueries()
				.append(VersionControl.getV04AlphaQueries())
				.append(() -> "INSERT INTO Metadata VALUES ('v0.4');");
		this.database.update(setupQueries).subscribe();

		this.versionTableExists();
		this.tableCount(5);
		this.uptodateWithNewestVersion(false);
	}

	@Test
	public void testSetupFromV04AlphaDatabase() throws InterruptedException {
		QueryEnumeration setupQueries = VersionControl.getV03AlphaQueries()
				.append(VersionControl.getV04AlphaQueries())
				.append(() -> "INSERT INTO Metadata VALUES ('v0.4-alpha');");
		this.database.update(setupQueries).subscribe();

		this.versionTableExists();
		this.tableCount(5);
		this.uptodateWithNewestVersion(false);
	}

	@Test
	public void testSetupFromV04bAlphaDatabase() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		this.vc.checkDBVersioning()
			.doOnError(e -> latch.countDown())
			.doOnCompleted(latch::countDown)
			.subscribe();

		latch.await();
		this.uptodateWithNewestVersion(true);
	}

	private void versionTableNotExists() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		TestSubscriber<Boolean> observer = new TestSubscriber<>();

		this.vc.versionTableExists().subscribe(observer::onNext,
				e -> { observer.onError(e); latch.countDown(); },
				() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();
		observer.assertValue(false);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	private void versionTableExists() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		TestSubscriber<Boolean> observer = new TestSubscriber<>();

		this.vc.versionTableExists().subscribe(observer::onNext,
				e -> { observer.onError(e); latch.countDown(); },
				() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();
		observer.assertValue(true);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	private void tableCount(int expected) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		TestSubscriber<Integer> observer = new TestSubscriber<>();

		this.vc.getTableCount().subscribe(observer::onNext,
				e -> { observer.onError(e); latch.countDown(); },
				() -> { observer.onCompleted(); latch.countDown(); });

		latch.await();
		observer.assertValue(expected);
		observer.assertNoErrors();
		observer.assertCompleted();
	}

	private void uptodateWithNewestVersion(boolean alreadyUptodate) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		TestSubscriber<Integer> updateObserver = new TestSubscriber<>();
		this.vc.checkDBVersioning()
				.subscribe(updateObserver::onNext,
				e -> { updateObserver.onError(e); latch.countDown(); },
				() -> { updateObserver.onCompleted(); latch.countDown(); });

		latch.await();
		if (alreadyUptodate)
			updateObserver.assertNoValues();
		else
			updateObserver.assertValue(1);
		updateObserver.assertNoErrors();
		updateObserver.assertCompleted();

		CountDownLatch latch2 = new CountDownLatch(1);
		TestSubscriber<String> versionObserver = new TestSubscriber<>();
		this.vc.getVersion()
				.subscribe(versionObserver::onNext,
				e -> { versionObserver.onError(e); latch2.countDown(); },
				() -> { versionObserver.onCompleted(); latch2.countDown(); });

		latch2.await();
		versionObserver.assertValue("v0.4b-alpha");
		versionObserver.assertNoErrors();
		versionObserver.assertCompleted();

		CountDownLatch latch3 = new CountDownLatch(1);
		TestSubscriber<Integer> tablesObserver = new TestSubscriber<>();
		this.database.query(() -> "SELECT COUNT(name) AS 'count' FROM sqlite_master;",
				result -> result.getInt("count"))
				.subscribe(tablesObserver::onNext,
				e -> { tablesObserver.onError(e); latch3.countDown(); },
				() -> { tablesObserver.onCompleted(); latch3.countDown(); });

		latch3.await();
		tablesObserver.assertValue(13);
		tablesObserver.assertNoErrors();
		tablesObserver.assertCompleted();
	}
}
