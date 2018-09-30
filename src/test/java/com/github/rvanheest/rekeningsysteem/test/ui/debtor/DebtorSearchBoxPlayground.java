package com.github.rvanheest.rekeningsysteem.test.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.DebtorSearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSearchBox;
import io.reactivex.Observable;
import javafx.application.Application;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.function.Function;

public class DebtorSearchBoxPlayground extends Playground implements ConfigurationFixture, DatabaseFixture {

  private DatabaseConnection databaseAccess;

  @Override
  protected void setUp() {
    try {
      System.out.println("setup test dir");
      this.resetTestDir();

      System.out.println("configure app");
      this.initDatabaseConnection().closeConnectionPool();
      PropertiesConfiguration configuration = this.getConfiguration();
      configuration.setProperty("database.url", String.format("jdbc:sqlite:%s", databaseFile()));

      this.databaseAccess = new DatabaseConnection(configuration);
      this.databaseAccess.initConnectionPool();
      this.setUpDatabase();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void setUpDatabase() {
    System.out.println("fill debtor table");
    DebtorTable table = new DebtorTable();
    Function<Connection, Observable<Boolean>> empty = (Connection conn) -> Observable.empty();
    Observable.just(
        new Debtor("name1", "street1", "number1", "zipcode1", "place1"),
        new Debtor("name2", "street2", "number2", "zipcode2", "place2", "test2")
    )
        .map(table::addDebtor)
        .reduce(empty, (f, g) -> conn -> f.apply(conn).concatWith(g.apply(conn).toObservable()))
        .flatMapObservable(this.databaseAccess::doTransactionObservable)
        .subscribe();
  }

  @Override
  protected void tearDown() {
    try {
      System.out.println("tear down");
      this.databaseAccess.closeConnectionPool();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public DebtorSearchBox uiElement() {
    Database database = new Database(this.databaseAccess);
    SearchEngine<Debtor> searchEngine = new DebtorSearchEngine(database);
    Offer emptyOffer = new Offer(
        new Header(
            new Debtor("", "", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        false
    );
    HeaderManager headerManager = new OfferManager(emptyOffer);
    DebtorSearchBox searchBox = new DebtorSearchBox(searchEngine, headerManager);

    searchBox.selectedItemIntent()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return searchBox;
  }

  public static void main(String[] args) {
    Application.launch(DebtorSearchBoxPlayground.class);
  }
}
