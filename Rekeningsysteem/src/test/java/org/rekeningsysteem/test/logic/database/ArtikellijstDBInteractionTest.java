package org.rekeningsysteem.test.logic.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;

import rx.observers.TestSubscriber;

public class ArtikellijstDBInteractionTest {

	private static EsselinkArtikel ea1 = new EsselinkArtikel("art1", "oms1", 1, "e1", new Geld(1));
	private static EsselinkArtikel ea2 = new EsselinkArtikel("art2", "oms2", 2, "e2", new Geld(2));
	private static EsselinkArtikel ea3 = new EsselinkArtikel("art3", "oms3", 3, "e3", new Geld(3));
	private static EsselinkArtikel ea4 = new EsselinkArtikel("art4", "oms4", 4, "e4", new Geld(4));
	private static EsselinkArtikel ea5 = new EsselinkArtikel("art5", "oms5", 5, "e5", new Geld(5));

	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Database database;
	private ArtikellijstDBInteraction art;

	@Before
	public void setUp() throws SQLException, IOException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		this.database = new Database(this.folder.newFile("database.db"));
		this.art = new ArtikellijstDBInteraction(this.database);
		new VersionControl(this.database)
				.checkDBVersioning()
				.doOnCompleted(latch::countDown)
				.subscribe();
		latch.await();
	}

	@After
	public void tearDown() throws SQLException {
		this.database.close();
	}

	private void assertEsselinkArtikels(EsselinkArtikel... expected) {
		TestSubscriber<EsselinkArtikel> testDebiteurObserver = new TestSubscriber<>();
		this.art.getAll().subscribe(testDebiteurObserver);

		testDebiteurObserver.assertValues(expected);
		testDebiteurObserver.assertNoErrors();
		testDebiteurObserver.assertCompleted();
	}

	@Test
	public void testInsert() {
		TestSubscriber<Integer> testInsertObserver = new TestSubscriber<>();
		this.art.insert(ea1)
				.flatMap((Integer i) -> this.art.insert(ea2), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea3), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea4), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea5), Math::addExact)
				.subscribe(testInsertObserver);

		testInsertObserver.assertValue(5);
		testInsertObserver.assertNoErrors();
		testInsertObserver.assertCompleted();

		this.assertEsselinkArtikels(ea1, ea2, ea3, ea4, ea5);
	}

	@Test
	public void testClearData() {
		this.testInsert();

		TestSubscriber<Integer> testInsertObserver = new TestSubscriber<>();
		this.art.clearData().subscribe(testInsertObserver);

		testInsertObserver.assertValue(5);
		testInsertObserver.assertNoErrors();
		testInsertObserver.assertCompleted();

		this.assertEsselinkArtikels();
	}

	@Test
	public void testGetWithArtikelnummerNone() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithArtikelnummer("foo").subscribe(testQueryObserver);

		testQueryObserver.assertNoValues();
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithArtikelnummerSingle() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithArtikelnummer("art1").subscribe(testQueryObserver);

		testQueryObserver.assertValue(ea1);
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithArtikelnummerMultiple() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithArtikelnummer("art").subscribe(testQueryObserver);

		testQueryObserver.assertValues(ea1, ea2, ea3, ea4, ea5);
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithOmschrijvingNone() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithOmschrijving("foo").subscribe(testQueryObserver);

		testQueryObserver.assertNoValues();
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithOmschrijvingSingle() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithOmschrijving("ms1").subscribe(testQueryObserver);

		testQueryObserver.assertValue(ea1);
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithOmschrijvingMultiple() {
		this.testInsert();

		TestSubscriber<EsselinkArtikel> testQueryObserver = new TestSubscriber<>();
		this.art.getWithOmschrijving("ms").subscribe(testQueryObserver);

		testQueryObserver.assertValues(ea1, ea2, ea3, ea4, ea5);
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}
}
