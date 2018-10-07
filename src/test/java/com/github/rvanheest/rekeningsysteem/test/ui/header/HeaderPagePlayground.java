package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.DebtorSearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.MutationInvoiceManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.header.HeaderPage;
import io.reactivex.Observable;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.money.Monetary;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.function.Function;

public class HeaderPagePlayground extends Playground implements ConfigurationFixture, DatabaseFixture {

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
        new Debtor("name1", "street1", "number1", "1234AB", "place1"),
        new Debtor("name2", "street2", "number2", "7896CD", "place2", "test2")
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

  @Override
  protected HeaderPage uiElement() {
    Database database = new Database(this.databaseAccess);
    SearchEngine<Debtor> searchEngine = new DebtorSearchEngine(database);
//    return createInvoiceHeaderPage(searchEngine);
//    return createNormalHeaderPage(searchEngine);
    return createOfferHeaderPage(searchEngine);
  }

  private static HeaderPage createInvoiceHeaderPage(SearchEngine<Debtor> searchEngine) {
    MutationInvoice emptyInvoice = new MutationInvoice(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        new ItemList<>(Monetary.getCurrency("EUR"))
    );
    HeaderManager headerManager = new MutationInvoiceManager(emptyInvoice);
    HeaderPage ui = HeaderPage.createInvoiceHeaderPage(searchEngine, headerManager);

    headerManager.getHeader()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );
    headerManager.storeDebtorOnSave()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return ui;
  }

  private static HeaderPage createOfferHeaderPage(SearchEngine<Debtor> searchEngine) {
    Offer emptyOffer = new Offer(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        true
    );
    OfferManager headerManager = new OfferManager(emptyOffer);
    HeaderPage ui = HeaderPage.createOfferHeaderPage(searchEngine, headerManager);

    headerManager.getHeader()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );
    headerManager.storeDebtorOnSave()
        .map(b -> String.format("store debtor on save: %b", b))
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );
    headerManager.getSign()
        .map(b -> String.format("sign offer: %b", b))
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return ui;
  }

  private static HeaderPage createNormalHeaderPage(SearchEngine<Debtor> searchEngine) {
    NormalInvoice emptyInvoice = new NormalInvoice(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        new ItemList<>(Monetary.getCurrency("EUR"))
    );
    NormalInvoiceManager headerWithDescriptionManager = new NormalInvoiceManager(emptyInvoice);
    HeaderPage ui = HeaderPage.createNormalInvoiceHeaderPage(searchEngine, headerWithDescriptionManager);

    headerWithDescriptionManager.getHeader()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );
    headerWithDescriptionManager.storeDebtorOnSave()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );
    headerWithDescriptionManager.getDescription()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return ui;
  }
}
