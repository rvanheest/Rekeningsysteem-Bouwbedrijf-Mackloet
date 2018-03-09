package com.github.rvanheest.rekeningsysteem.test.ui.debtor;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSearchBox;
import io.reactivex.Observable;
import javafx.application.Application;

import java.sql.Connection;
import java.util.function.Function;

public class DebtorSearchboxPlayground extends Playground implements ConfigurationFixture, DatabaseFixture {

  private DatabaseConnection connection;

  @Override
  protected void setUp() {
    try {
      System.out.println("setup test dir");
      this.resetTestDir();

      this.connection = this.initDatabaseConnection();
      this.setUpDatabase(connection);

      System.out.println("setup dependency injection");
      this.initDependencyInjection(connection);
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private void setUpDatabase(DatabaseConnection connection) {
    System.out.println("fill debtor table");
    DebtorTable table = new DebtorTable();
    Function<Connection, Observable<Boolean>> empty = (Connection conn) -> Observable.empty();
    Observable.just(
        new Debtor("name1", "street1", "number1", "zipcode1", "place1"),
        new Debtor("name2", "street2", "number2", "zipcode2", "place2", "test2")
    )
        .map(table::addDebtor)
        .scan(empty, (f, g) -> conn -> f.apply(conn).concatWith(g.apply(conn).toObservable()))
        .flatMap(connection::doTransactionObservable)
        .subscribe();
  }

  @Override
  protected void tearDown() {
    try {
      System.out.println("tear down");
      this.connection.closeConnectionPool();
    }
    catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public DebtorSearchBox uiElement() {
    DebtorSearchBox searchBox = new DebtorSearchBox();

    searchBox.selectedItemIntent()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return searchBox;
  }

  public static void main(String[] args) {
    Application.launch(DebtorSearchboxPlayground.class);
  }
}
