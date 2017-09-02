package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.function.Function;

public class DebtorTableTest extends DatabaseFixture {

  private final DebtorTable table = new DebtorTable();

  private static Debtor simplePreDebtor = new Debtor("name1", "street1", "number1", "zipcode1", "place1");
  private static Debtor simplePostDebtor = new Debtor(1, "name1", "street1", "number1", "zipcode1", "place1");
  private static Debiteur simplePostDebiteur = new Debiteur(1, "name1", "street1", "number1", "zipcode1", "place1");

  private static Debtor complexPreDebtor = new Debtor("name2", "street2", "number2", "zipcode2", "place2", "test2");
  private static Debtor complexPostDebtor1 = new Debtor(1, "name2", "street2", "number2", "zipcode2", "place2", "test2");
  private static Debiteur complexPostDebiteur = new Debiteur(1, "name2", "street2", "number2", "zipcode2", "place2");
  private static BTWDebiteur complexPostBTWDebiteur1 = new BTWDebiteur(1, "test2");

  private static Debtor complexPostDebtor2 = new Debtor(2, "name2", "street2", "number2", "zipcode2", "place2", "test2");

  private Function<Connection, Observable<Debiteur>> getDebiteursFromDatabase() {
    String query = "SELECT * FROM Debiteur;";
    return connection -> Observable.using(connection::createStatement,
        statement -> Observable.generate(() -> statement.executeQuery(query), (rs, emitter) -> {
          if (rs.next()) {
            int id = rs.getInt("debiteurID");
            String name = rs.getString("naam");
            String street = rs.getString("straat");
            String number = rs.getString("nummer");
            String zipcode = rs.getString("postcode");
            String city = rs.getString("plaats");

            emitter.onNext(new Debiteur(id, name, street, number, zipcode, city));
          }
          else
            emitter.onComplete();
        }, ResultSet::close), Statement::close);
  }

  private Function<Connection, Observable<BTWDebiteur>> getBTWDebiteursFromDatabase() {
    String query = "SELECT * FROM BTWDebiteur;";
    return connection -> Observable.using(connection::createStatement,
        statement -> Observable.generate(() -> statement.executeQuery(query), (rs, emitter) -> {
          if (rs.next()) {
            int id = rs.getInt("debiteurID");
            String btwNummer = rs.getString("btwNummer");

            emitter.onNext(new BTWDebiteur(id, btwNummer));
          }
          else
            emitter.onComplete();
        }, ResultSet::close), Statement::close);
  }

  @Test
  public void testAddSimpleDebtor() {
    this.databaseAccess.doTransactionSingle(this.table.addDebtor(simplePreDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValue(simplePostDebiteur)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddComplexDebtor() {
    this.databaseAccess.doTransactionSingle(this.table.addDebtor(complexPreDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValue(complexPostDebiteur)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddDebtorThatAlreadyIsInTheDatabase() {
    this.testAddSimpleDebtor();

    this.databaseAccess.doTransactionSingle(this.table.addDebtor(simplePreDebtor))
        .test()
        .assertValue(false)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValue(simplePostDebiteur) // only one value
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddMultipleDebtors() {
    this.testAddSimpleDebtor();

    this.databaseAccess.doTransactionSingle(this.table.addDebtor(complexPreDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur, complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddAndGetSimpleDebtor() {
    this.databaseAccess.doTransactionSingle(this.table.addAndGetDebtor(simplePreDebtor))
        .test()
        .assertValue(simplePostDebtor)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddAndGetComplexDebtor() {
    this.databaseAccess.doTransactionSingle(this.table.addAndGetDebtor(complexPreDebtor))
        .test()
        .assertValue(complexPostDebtor1)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testAddAndGetMultipleDebtors() {
    this.databaseAccess.doTransactionSingle(this.table.addAndGetDebtor(simplePreDebtor))
        .test()
        .assertValue(simplePostDebtor)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionSingle(this.table.addAndGetDebtor(complexPreDebtor))
        .test()
        .assertValue(complexPostDebtor2)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDeleteSimpleDebtor() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionSingle(this.table.deleteDebtor(simplePostDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValue(complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDeleteComplexDebtor() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionSingle(this.table.deleteDebtor(complexPostDebtor2))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValue(simplePostDebiteur.withId(1))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDeleteNonExistingDebtor() {
    this.testDeleteSimpleDebtor();

    this.databaseAccess.doTransactionSingle(this.table.deleteDebtor(simplePostDebtor))
        .test()
        .assertValue(false)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDeleteDebtorWithoutId() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionSingle(this.table.deleteDebtor(simplePreDebtor))
        .test()
        .assertNoValues()
        .assertError(IllegalArgumentException.class)
        .assertError(t -> t.getMessage().startsWith("This debtor doesn't contain an id:"))
        .assertNotComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur.withId(1), complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateSimpleDebtorWithSimpleDebtor() {
    this.testAddAndGetMultipleDebtors();

    Debtor newDebtor = new Debtor(1, "abc", "def", "ghi", "jkl", "mno");
    Debiteur newPostDebiteur = new Debiteur(1, "abc", "def", "ghi", "jkl", "mno");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(simplePostDebtor, newDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(newPostDebiteur, complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateComplexDebtorWithComplexDebtor() {
    this.testAddAndGetMultipleDebtors();

    Debtor newDebtor = new Debtor(2, "abc", "def", "ghi", "jkl", "mno", "pqr");
    Debiteur newPostDebiteur = new Debiteur(2, "abc", "def", "ghi", "jkl", "mno");
    BTWDebiteur newPostBtwDebiteur = new BTWDebiteur(2, "pqr");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(complexPostDebtor2, newDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur.withId(1), newPostDebiteur)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(newPostBtwDebiteur)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateSimpleDebtorWithComplexDebtor() {
    this.testAddAndGetMultipleDebtors();

    Debtor newDebtor = new Debtor(1, "abc", "def", "ghi", "jkl", "mno", "pqr");
    Debiteur newPostDebiteur = new Debiteur(1, "abc", "def", "ghi", "jkl", "mno");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(simplePostDebtor, newDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(newPostDebiteur, complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValues(new BTWDebiteur(1, "pqr").withId(1), complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateComplexDebtorWithSimpleDebtor() {
    this.testAddAndGetMultipleDebtors();

    Debtor newDebtor = new Debtor(2, "abc", "def", "ghi", "jkl", "mno");
    Debiteur newPostDebiteur = new Debiteur(2, "abc", "def", "ghi", "jkl", "mno");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(complexPostDebtor2, newDebtor))
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur.withId(1), newPostDebiteur)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateNonExistingDebtor() {
    this.testAddAndGetMultipleDebtors();

    Debtor nonExisiting = new Debtor(3, "zyx", "wvu", "tsr", "qpo", "nml");
    Debtor newDebtor = new Debtor(1, "abc", "def", "ghi", "jkl", "mno");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(nonExisiting, newDebtor))
        .test()
        .assertValue(false)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur.withId(1), complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testUpdateDebtorWithoutId() {
    this.testAddAndGetMultipleDebtors();

    Debtor newDebtor = new Debtor(1, "abc", "def", "ghi", "jkl", "mno");

    this.databaseAccess.doTransactionSingle(this.table.updateDebtor(simplePreDebtor, newDebtor))
        .test()
        .assertNoValues()
        .assertError(IllegalArgumentException.class)
        .assertError(e -> e.getMessage().startsWith("This debtor doesn't contain an id:"))
        .assertNotComplete();

    this.databaseAccess.doTransactionObservable(getDebiteursFromDatabase())
        .test()
        .assertValues(simplePostDebiteur.withId(1), complexPostDebiteur.withId(2))
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(getBTWDebiteursFromDatabase())
        .test()
        .assertValue(complexPostBTWDebiteur1.withId(2))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetAll() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertValues(simplePostDebtor, complexPostDebtor2)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetAllEmpty() {
    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithNameOneFound() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionObservable(this.table.getWithName("ame1"))
        .test()
        .assertValue(simplePostDebtor)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithNameMultipleFound() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionObservable(this.table.getWithName("name"))
        .test()
        .assertValues(simplePostDebtor, complexPostDebtor2)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithNameNoFound() {
    this.testAddAndGetMultipleDebtors();

    this.databaseAccess.doTransactionObservable(this.table.getWithName("unknown"))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }
}

class Debiteur {

  private int id;
  private final String naam;
  private final String straat;
  private final String nummer;
  private final String postcode;
  private final String plaats;

  Debiteur(int id, String naam, String straat, String nummer, String postcode, String plaats) {
    this.id = id;
    this.naam = naam;
    this.straat = straat;
    this.nummer = nummer;
    this.postcode = postcode;
    this.plaats = plaats;
  }

  Debiteur withId(int newId) {
    return new Debiteur(newId, this.naam, this.straat, this.nummer, this.postcode, this.plaats);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Debiteur) {
      Debiteur that = (Debiteur) other;
      return Objects.equals(this.id, that.id)
          && Objects.equals(this.naam, that.naam)
          && Objects.equals(this.straat, that.straat)
          && Objects.equals(this.nummer, that.nummer)
          && Objects.equals(this.postcode, that.postcode)
          && Objects.equals(this.plaats, that.plaats);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.naam, this.straat, this.nummer, this.postcode, this.plaats);
  }

  @Override
  public String toString() {
    return String.format("<Debiteur[%d, %s, %s, %s, %s, %s]>", this.id, this.naam, this.straat, this.nummer, this.postcode, this.plaats);
  }
}

class BTWDebiteur {
  private final int id;
  private final String btwNummer;

  BTWDebiteur(int id, String btwNummer) {
    this.id = id;
    this.btwNummer = btwNummer;
  }

  BTWDebiteur withId(int newId) {
    return new BTWDebiteur(newId, this.btwNummer);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof BTWDebiteur) {
      BTWDebiteur that = (BTWDebiteur) other;
      return Objects.equals(this.id, that.id)
          && Objects.equals(this.btwNummer, that.btwNummer);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.btwNummer);
  }

  @Override
  public String toString() {
    return String.format("<BTWDebiteur[%d, %s]>", this.id, this.btwNummer);
  }
}
