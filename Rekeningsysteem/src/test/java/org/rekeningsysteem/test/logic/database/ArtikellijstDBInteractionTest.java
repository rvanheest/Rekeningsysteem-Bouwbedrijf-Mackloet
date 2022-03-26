package org.rekeningsysteem.test.logic.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.core.Observable;
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

public class ArtikellijstDBInteractionTest {

	private final static EsselinkArtikel ea1 = new EsselinkArtikel("art1", "oms1", 1, "e1", new Geld(1));
	private final static EsselinkArtikel ea2 = new EsselinkArtikel("art2", "oms2", 2, "e2", new Geld(2));
	private final static EsselinkArtikel ea3 = new EsselinkArtikel("art3", "oms3", 3, "e3", new Geld(3));
	private final static EsselinkArtikel ea4 = new EsselinkArtikel("art4", "oms4", 4, "e4", new Geld(4));
	private final static EsselinkArtikel ea5 = new EsselinkArtikel("art5", "oms5", 5, "e5", new Geld(5));

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
				.doOnComplete(latch::countDown)
				.subscribe();
		latch.await();
	}

	@After
	public void tearDown() throws Exception {
		this.database.close();
	}

	private void assertEsselinkArtikels(EsselinkArtikel... expected) {
		this.art.getAll().test()
			.assertValues(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testInsert() {
		this.art.insert(ea1)
				.flatMap((Integer i) -> this.art.insert(ea2), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea3), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea4), Math::addExact)
				.flatMap((Integer i) -> this.art.insert(ea5), Math::addExact)
				.test()
			.assertValue(5)
			.assertNoErrors()
			.assertComplete();

		this.assertEsselinkArtikels(ea1, ea2, ea3, ea4, ea5);
	}

	@Test
	public void testInsertAllEmpty() throws InterruptedException {
		this.art.insertAll(Observable.empty())
			.test()
			.await()
			.assertValue(0)
			.assertNoErrors()
			.assertComplete();

		this.assertEsselinkArtikels();
	}

	@Test
	public void testInsertAll() {
		this.art.insertAll(Observable.just(ea1, ea2, ea3, ea4, ea5))
			.test()
			.assertValue(5)
			.assertNoErrors()
			.assertComplete();

		this.assertEsselinkArtikels(ea1, ea2, ea3, ea4, ea5);
	}

	@Test
	public void testClearData() {
		this.testInsert();

		this.art.clearData()
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertEsselinkArtikels();
	}

	@Test
	public void testGetWithArtikelnummerNone() {
		this.testInsert();

		this.art.getWithArtikelnummer("foo")
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithArtikelnummerSingle() {
		this.testInsert();

		this.art.getWithArtikelnummer("art1")
			.test()
			.assertValue(ea1)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithArtikelnummerMultiple() {
		this.testInsert();

		this.art.getWithArtikelnummer("art")
			.test()
			.assertValues(ea1, ea2, ea3, ea4, ea5)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithOmschrijvingNone() {
		this.testInsert();

		this.art.getWithOmschrijving("foo")
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithOmschrijvingSingle() {
		this.testInsert();

		this.art.getWithOmschrijving("ms1")
			.test()
			.assertValue(ea1)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithOmschrijvingMultiple() {
		this.testInsert();

		this.art.getWithOmschrijving("ms")
			.test()
			.assertValues(ea1, ea2, ea3, ea4, ea5)
			.assertNoErrors()
			.assertComplete();
	}
}
