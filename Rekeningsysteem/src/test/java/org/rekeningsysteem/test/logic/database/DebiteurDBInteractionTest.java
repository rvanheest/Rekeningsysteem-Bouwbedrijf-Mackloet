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

public class DebiteurDBInteractionTest {

	private final static Debiteur simplePreDebiteur = new Debiteur("name1", "street1", "number1", "zipcode1", "place1");
	private final static Debiteur complexPreDebiteur = new Debiteur("name2", "street2", "number2", "zipcode2", "place2", "test2");
	private final static Debiteur simplePostDebiteur = new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1");
	private final static Debiteur complexPostDebiteur = new Debiteur(1, "name2", "street2", "number2", "zipcode2", "place2", "test2");

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
			.doOnComplete(latch::countDown)
			.subscribe();
		latch.await();
	}

	@After
	public void tearDown() throws Exception {
		this.database.close();
	}

	private void assertDebiteurs(Debiteur... expected) {
		this.deb.getAll()
			.test()
			.assertValues(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testAddDebiteurSimpleDebiteur() {
		this.deb.addDebiteur(simplePreDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddDebiteurComplexDebiteur() {
		this.deb.addDebiteur(complexPreDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(complexPostDebiteur);
	}

	@Test
	public void testAddDebiteurThatAlreadyIsInTheDatabase() {
		this.testAddDebiteurSimpleDebiteur();

		this.deb.addDebiteur(simplePreDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddMultipleDebiteurs() {
		this.testAddDebiteurSimpleDebiteur();

		this.deb.addDebiteur(complexPreDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(
			new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1"),
			new Debiteur(2, "name2", "street2", "number2", "zipcode2", "place2", "test2")
		);
	}

	@Test
	public void testAddAndGetDebiteurSimpleDebiteur() {
		this.deb.addAndGetDebiteur(simplePreDebiteur)
			.test()
			.assertValue(simplePostDebiteur)
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test
	public void testAddAndGetDebiteurComplexDebiteur() {
		this.deb.addAndGetDebiteur(complexPreDebiteur)
			.test()
			.assertValue(complexPostDebiteur)
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(complexPostDebiteur);
	}

	@Test
	public void testDeleteDebiteurSimpleDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		this.deb.deleteDebiteur(simplePostDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(); // table is now empty
	}

	@Test
	public void testDeleteDebiteurComplexDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		this.deb.deleteDebiteur(complexPostDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(); // table is now empty
	}

	@Test
	public void testDeleteDebiteurThatIsNotInTheDatabase() {
		this.assertDebiteurs(); // table is now empty

		this.deb.deleteDebiteur(simplePostDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(); // table is now empty
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteDebiteurWithoutID() {
		this.deb.deleteDebiteur(new Debiteur("naam", "straat", "nummer", "postcode", "plaats"));
	}

	@Test
	public void testUpdateSimpleDebiteurWithSimpleDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		this.deb.updateDebiteur(simplePostDebiteur, new Debiteur("1", "2", "3", "4", "5"))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5"));
	}

	@Test
	public void testUpdateComplexDebiteurWithComplexDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		this.deb.updateDebiteur(complexPostDebiteur, new Debiteur("1", "2", "3", "4", "5", "6"))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5", "6"));
	}

	@Test
	public void testUpdateSimpleDebiteurWithComplexDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		this.deb.updateDebiteur(simplePostDebiteur, new Debiteur("1", "2", "3", "4", "5", "6"))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5", "6"));
	}

	@Test
	public void testUpdateComplexDebiteurWithSimpleDebiteur() {
		this.testAddDebiteurComplexDebiteur();

		this.deb.updateDebiteur(complexPostDebiteur, new Debiteur("1", "2", "3", "4", "5"))
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(new Debiteur(1, "1", "2", "3", "4", "5"));
	}

	@Test
	public void testUpdateNonExistingDebiteur() {
		this.testAddDebiteurSimpleDebiteur();

		Debiteur newDebiteur = new Debiteur("1", "2", "3", "4", "5");

		this.deb.updateDebiteur(new Debiteur(2, "1", "2", "3", "4", "5"), newDebiteur)
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();

		this.assertDebiteurs(simplePostDebiteur);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateDebiteurWithoutID() {
		this.deb.updateDebiteur(new Debiteur("naam", "straat", "nummer", "postcode", "plaats"), simplePreDebiteur);
	}

	@Test
	public void testGetWithNameOneFound() {
		this.testAddMultipleDebiteurs();

		this.deb.getWithNaam("name1")
			.test()
			.assertValue(simplePostDebiteur)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithNameMultipleFound() {
		this.testAddMultipleDebiteurs();

		this.deb.getWithNaam("ame")
			.test()
			.assertValues(
				new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1"),
				new Debiteur(2, "name2", "street2", "number2", "zipcode2", "place2", "test2")
			)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testGetWithNameNoFound() {
		this.testAddMultipleDebiteurs();

		this.deb.getWithNaam("foo")
			.test()
			.assertNoValues()
			.assertNoErrors()
			.assertComplete();
	}
}
