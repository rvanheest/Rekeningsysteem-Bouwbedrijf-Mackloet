package com.github.rvanheest.rekeningsysteem.test.esselinkItems;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.EsselinkItemTable;
import com.github.rvanheest.rekeningsysteem.esselinkItems.EsselinkItemHandler;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.MonetaryException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EsselinkItemHandlerTest implements DatabaseFixture, EsselinkItemFixture {

  private DatabaseConnection databaseAccess;
  private final EsselinkItemTable table = new EsselinkItemTable();
  private final EsselinkItemHandler handler = new EsselinkItemHandler(this.table);

  private final Path csv = Paths.get(getClass()
      .getResource("/esselink/example-data.csv").toURI());
  private final Path headerOnlyCsv = Paths.get(getClass()
      .getResource("/esselink/example-data-header-only.csv").toURI());
  private final Path amountPerFailCsv = Paths.get(getClass()
      .getResource("/esselink/example-data-amountPer-fail.csv").toURI());
  private final Path pricePerUnitFailCsv = Paths.get(getClass()
      .getResource("/esselink/example-data-pricePerUnit-fail.csv").toURI());
  private final Path csvLarge = Paths.get(getClass()
      .getResource("/esselink/example-data-large.csv").toURI());

  // TODO why this constructor
  public EsselinkItemHandlerTest() throws URISyntaxException {
  }

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
  public void testRead() {
    this.handler.read(csv)
        .test()
        .assertValueSequence(this.getEsselinkItems())
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadHeaderOnly() {
    this.handler.read(headerOnlyCsv)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadAmountPerParserFail() {
    this.handler.read(this.amountPerFailCsv)
        .test()
        .assertNoValues()
        .assertError(NumberFormatException.class)
        .assertNotComplete();
  }

  @Test
  public void testReadPricePerUnitParserFail() {
    this.handler.read(this.pricePerUnitFailCsv)
        .test()
        .assertNoValues()
        .assertError(MonetaryException.class)
        .assertNotComplete();
  }

  @Test
  public void testLoad() {
    this.databaseAccess.doTransactionObservable(this.handler.load(this.csv))
        .test()
        .assertValues(0, 8)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertValueSequence(this.getEsselinkItems())
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testLoadLarge() {
    this.databaseAccess.doTransactionObservable(this.handler.load(this.csvLarge))
        .test()
        .assertValues(0, 100, 200, 256)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionObservable(this.table.getAll())
        .test()
        .assertValueCount(256)
        .assertValueSequence(this.handler.read(this.csvLarge).blockingIterable())
        .assertNoErrors()
        .assertComplete();
  }
}
