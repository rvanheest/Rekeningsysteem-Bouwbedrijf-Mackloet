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
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;

import rx.observers.TestSubscriber;

public class DebiteurDBInteractionTest {

	private static Debiteur simplePreDebiteur = new Debiteur("name1", "street1", "number1",
			"zipcode1", "place1");
	private static Debiteur complexPreDebiteur = new Debiteur("name2", "street2", "number2",
			"zipcode2", "place2", "test2");
	private static Debiteur simplePostDebiteur = new Debiteur(1, "name1", "street1", "number1",
			"zipcode1", "place1");
	private static Debiteur complexPostDebiteur = new Debiteur(1, "name2", "street2", "number2",
			"zipcode2", "place2", "test2");

	@Rule public TemporaryFolder folder = new TemporaryFolder();
	private Database database;
	private DebiteurDBInteraction deb;

	@Before
	public void setUp() throws IOException, SQLException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		this.database = new Database(this.folder.newFile("database.db"));
		this.deb = new DebiteurDBInteraction(this.database);
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

	private void assertDebiteurs(Debiteur... expected) {
		TestSubscriber<Debiteur> testDebiteurObserver = new TestSubscriber<>();
		this.deb.getAll().subscribe(testDebiteurObserver);

		testDebiteurObserver.assertValues(expected);
		testDebiteurObserver.assertNoErrors();
		testDebiteurObserver.assertCompleted();
	}

	@Test
	public void testAddDebiteurSimpleDebiteur() {
		TestSubscriber<Integer> testAddObserver = new TestSubscriber<>();
		this.deb.addDebiteur(simplePreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(1);
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddDebiteurComplexDebiteur() {
		TestSubscriber<Integer> testAddObserver = new TestSubscriber<>();
		this.deb.addDebiteur(complexPreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(2); // updated 2 tables
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(complexPostDebiteur);
	}

	@Test
	public void testAddDebiteurThatAlreadyIsInTheDatabase() {
		this.testAddDebiteurSimpleDebiteur();

		TestSubscriber<Integer> testAddObserver = new TestSubscriber<>();
		this.deb.addDebiteur(simplePreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(0);
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddMultipleDebiteurs() {
		this.testAddDebiteurSimpleDebiteur();

		TestSubscriber<Integer> testAddObserver = new TestSubscriber<>();
		this.deb.addDebiteur(complexPreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(2); // updated 2 tables
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(
				new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1"),
				new Debiteur(2, "name2", "street2", "number2", "zipcode2", "place2", "test2"));
	}

	@Test
	public void testAddAndGetDebiteurSimpleDebiteur() {
		TestSubscriber<Debiteur> testAddObserver = new TestSubscriber<>();
		this.deb.addAndGetDebiteur(simplePreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(simplePostDebiteur);
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddAndGetDebiteurComplexDebiteur() {
		TestSubscriber<Debiteur> testAddObserver = new TestSubscriber<>();
		this.deb.addAndGetDebiteur(complexPreDebiteur).subscribe(testAddObserver);

		testAddObserver.assertValue(complexPostDebiteur);
		testAddObserver.assertNoErrors();
		testAddObserver.assertCompleted();

		this.assertDebiteurs(complexPostDebiteur);
	}

	@Test
	public void testDeleteDebiteurSimpleDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		TestSubscriber<Integer> testRemoveObserver = new TestSubscriber<>();
		this.deb.deleteDebiteur(simplePostDebiteur).subscribe(testRemoveObserver);

		testRemoveObserver.assertValue(1);
		testRemoveObserver.assertNoErrors();
		testRemoveObserver.assertCompleted();

		this.assertDebiteurs(); // table is now empty
	}

	@Test
	public void testDeleteDebiteurComplexDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		TestSubscriber<Integer> testRemoveObserver = new TestSubscriber<>();
		this.deb.deleteDebiteur(complexPostDebiteur).subscribe(testRemoveObserver);

		testRemoveObserver.assertValue(2);
		testRemoveObserver.assertNoErrors();
		testRemoveObserver.assertCompleted();

		this.assertDebiteurs(); // table is now empty
	}

	@Test
	public void testDeleteDebiteurThatIsNotInTheDatabase() {
		this.assertDebiteurs(); // table is now empty

		TestSubscriber<Integer> testRemoveObserver = new TestSubscriber<>();
		this.deb.deleteDebiteur(simplePostDebiteur).subscribe(testRemoveObserver);

		testRemoveObserver.assertValue(0);
		testRemoveObserver.assertNoErrors();
		testRemoveObserver.assertCompleted();

		this.assertDebiteurs(); // table is now empty
	}

	@Test (expected = IllegalArgumentException.class)
	public void testDeleteDebiteurWithoutID() {
		this.deb.deleteDebiteur(new Debiteur("naam", "straat", "nummer", "postcode", "plaats"));
	}

	@Test
	public void testUpdateSimpleDebiteurWithSimpleDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		TestSubscriber<Integer> testUpdateObserver = new TestSubscriber<>();
		this.deb.updateDebiteur(simplePostDebiteur, new Debiteur("1", "2", "3", "4", "5"))
				.subscribe(testUpdateObserver);

		testUpdateObserver.assertValue(1);
		testUpdateObserver.assertNoErrors();
		testUpdateObserver.assertCompleted();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5"));
	}

	@Test
	public void testUpdateComplexDebiteurWithComplexDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		TestSubscriber<Integer> testUpdateObserver = new TestSubscriber<>();
		this.deb.updateDebiteur(complexPostDebiteur, new Debiteur("1", "2", "3", "4", "5", "6"))
				.subscribe(testUpdateObserver);

		testUpdateObserver.assertValue(2);
		testUpdateObserver.assertNoErrors();
		testUpdateObserver.assertCompleted();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5", "6"));
	}

	@Test
	public void testUpdateSimpleDebiteurWithComplexDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		TestSubscriber<Integer> testUpdateObserver = new TestSubscriber<>();
		this.deb.updateDebiteur(simplePostDebiteur, new Debiteur("1", "2", "3", "4", "5", "6"))
				.subscribe(testUpdateObserver);

		testUpdateObserver.assertValue(4); // not sure why this is 4...
		testUpdateObserver.assertNoErrors();
		testUpdateObserver.assertCompleted();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5", "6"));
	}

	@Test
	public void testUpdateComplexDebiteurWithSimpleDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		TestSubscriber<Integer> testUpdateObserver = new TestSubscriber<>();
		this.deb.updateDebiteur(complexPostDebiteur, new Debiteur("1", "2", "3", "4", "5"))
				.subscribe(testUpdateObserver);

		testUpdateObserver.assertValue(2);
		testUpdateObserver.assertNoErrors();
		testUpdateObserver.assertCompleted();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5"));
	}

	@Test
	public void testUpdateNonExistingDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		Debiteur newDebiteur = new Debiteur("1", "2", "3", "4", "5");

		TestSubscriber<Integer> testUpdateObserver = new TestSubscriber<>();
		this.deb.updateDebiteur(new Debiteur(2, "1", "2", "3", "4", "5"), newDebiteur)
				.subscribe(testUpdateObserver);

		testUpdateObserver.assertValue(0);
		testUpdateObserver.assertNoErrors();
		testUpdateObserver.assertCompleted();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testUpdateDebiteurWithoutID() {
		this.deb.updateDebiteur(new Debiteur("naam", "straat", "nummer", "postcode", "plaats"), simplePreDebiteur);
	}

	@Test
	public void testGetWithNameOneFound() {
		this.testAddMultipleDebiteurs();

		TestSubscriber<Debiteur> testQueryObserver = new TestSubscriber<>();
		this.deb.getWithNaam("name1").subscribe(testQueryObserver);

		testQueryObserver.assertValue(simplePostDebiteur);
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithNameMultipleFound() {
		this.testAddMultipleDebiteurs();

		TestSubscriber<Debiteur> testQueryObserver = new TestSubscriber<>();
		this.deb.getWithNaam("ame").subscribe(testQueryObserver);

		testQueryObserver.assertValues(
				new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1"),
				new Debiteur(2, "name2", "street2", "number2", "zipcode2", "place2", "test2"));
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}

	@Test
	public void testGetWithNameNoFound() {
		this.testAddMultipleDebiteurs();

		TestSubscriber<Debiteur> testQueryObserver = new TestSubscriber<>();
		this.deb.getWithNaam("foo").subscribe(testQueryObserver);

		testQueryObserver.assertNoValues();
		testQueryObserver.assertNoErrors();
		testQueryObserver.assertCompleted();
	}
}
