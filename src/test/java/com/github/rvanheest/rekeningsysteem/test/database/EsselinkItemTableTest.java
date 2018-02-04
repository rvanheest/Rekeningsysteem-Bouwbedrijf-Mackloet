package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.EsselinkItemTable;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.test.esselinkItems.EsselinkItemFixture;
import io.reactivex.Observable;
import org.javamoney.moneta.Money;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EsselinkItemTableTest implements DatabaseFixture, EsselinkItemFixture {

  private DatabaseConnection databaseAccess;
  private final EsselinkItemTable table = new EsselinkItemTable();

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() throws Exception {
    this.databaseAccess = this.initDatabaseConnection();
  }

  @After
  public void tearDown() throws Exception {
    this.databaseAccess.closeConnectionPool();
  }

  @Test
  public void testInsertAll() {
    this.databaseAccess.doTransactionSingle(this.table.insertAll(Observable.fromIterable(this.getEsselinkItems())))
        .test()
        .assertValue(8)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertValueSequence(this.getEsselinkItems())
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testInsertAllEmpty() {
    this.databaseAccess.doTransactionSingle(this.table.insertAll(Observable.empty()))
        .test()
        .assertValue(0)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testClearData() {
    this.testInsertAll();

    this.databaseAccess.doTransactionCompletable(this.table.clearData())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithItemIdNotFound() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithItemId("34"))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithItemIdOne() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithItemId("456"))
        .test()
        .assertValue(new EsselinkItem("456789", "test456", 10, "bar", Money.of(15.93, "EUR")))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithItemIdMultiple() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithItemId("1"))
        .test()
        .assertValues(
            new EsselinkItem("123456", "test123", 13, "foo", Money.of(15.93, "EUR")),
            new EsselinkItem("147258", "test147", 17, "qux", Money.of(15.93, "EUR")),
            new EsselinkItem("159357", "test159", 19, "grault", Money.of(15.93, "EUR"))
        )
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithDescriptionNotFound() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithDescription("esx"))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithDescriptionOne() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithDescription("est2"))
        .test()
        .assertValue(new EsselinkItem("258369", "test258", 11, "quux", Money.of(15.93, "EUR")))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetWithDescriptionMultiple() {
    this.testInsertAll();

    this.databaseAccess.doTransactionObservable(this.table.getWithDescription("est"))
        .test()
        .assertValueSequence(this.getEsselinkItems())
        .assertNoErrors()
        .assertComplete();
  }
}
