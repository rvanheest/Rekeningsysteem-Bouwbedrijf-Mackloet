package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.businesslogic.DebtorSearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.function.Function;

public class OfferIntegrationTest2 implements DatabaseFixture, ConfigurationFixture {

  private DatabaseConnection connection;
  private Database database;
  private PropertiesConfiguration configuration;
  private OfferManager offerManager;

  private static Debtor emptyDebtor = new Debtor("", "", "", "", "");
  private static Debtor debtor1 = new Debtor(1, "name1", "street1", "number1", "zipcode1", "place1");
  private static Debtor debtor2 = new Debtor(2, "name2", "street2", "number2", "zipcode2", "place2", "test2");

  @Before
  public void setUp() throws Exception {
    this.connection = this.initDatabaseConnection();
    this.database = new Database(this.connection);
    this.configuration = this.getConfiguration();

    this.offerManager = new OfferManager(new Offer(
        new Header(emptyDebtor, LocalDate.now()),
        "",
        false));
  }

  @After
  public void tearDown() throws Exception {
    this.connection.closeConnectionPool();
  }

  private void setUpDatabase(DatabaseConnection connection) {
    DebtorTable table = new DebtorTable();
    Function<Connection, Observable<Boolean>> empty = (Connection conn) -> Observable.empty();
    Observable.just(debtor1, debtor2)
        .map(table::addDebtor)
        .reduce(empty, (f, g) -> conn -> f.apply(conn).concatWith(g.apply(conn).toObservable()))
        .flatMapObservable(connection::doTransactionObservable)
        .subscribe();
  }

  @Test
  public void setDebtorFromSearch() {
    this.setUpDatabase(this.connection);

    SearchEngine<Debtor> searchEngine = new DebtorSearchEngine(this.database);

    TestObserver<Debtor> debtor = this.offerManager.getDocument()
        .map(offer -> offer.getHeader().getDebtor())
        .test();

    searchEngine.suggest("nam")
        .map(list -> list.get(0))
        .flatMapCompletable(this.offerManager::withDebtor)
        .subscribe();

    debtor.assertValues(emptyDebtor, debtor1)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void setTextFromDefaultOfferTextHandler() {
    DefaultOfferTextHandler handler = new DefaultOfferTextHandler(this.configuration);

    TestObserver<String> text = this.offerManager.getDocument().map(Offer::getText).map(String::trim).test();

    handler.getDefaultText()
        .flatMapCompletable(this.offerManager::withText)
        .subscribe();

    text.assertValues("", "This is a testing text! Here you can put your name, adres, etc.")
        .assertNoErrors()
        .assertNotComplete();
  }
}
