package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class EsselinkItemTable {

  public Function<Connection, Completable> clearData() {
    return connection -> Completable.using(
        connection::createStatement,
        statement -> Completable.fromAction(() -> statement.executeUpdate("DELETE FROM Artikellijst;")),
        Statement::close
    );
  }

  public Function<Connection, Single<Integer>> insertAll(Observable<EsselinkItem> items) {
    return connection -> items
        .map(item -> String.format("('%s', '%s', '%s', '%s', '%s')",
            item.getItemId(),
            item.getDescription().replace("\'", "\'\'"),
            String.valueOf(item.getAmountPer()),
            item.getUnit(),
            String.valueOf(item.getPricePerUnit().getNumber().doubleValueExact())))
        .reduce((s1, s2) -> String.format("%s, %s", s1, s2))
        .map(values -> String.format("INSERT INTO Artikellijst VALUES %s;", values))
        .map(query -> Single.using(
            connection::createStatement,
            statement -> Single.just(statement.executeUpdate(query)),
            Statement::close))
        .defaultIfEmpty(Single.just(0))
        .flatMapSingle(i -> i);
  }

  public Function<Connection, Observable<EsselinkItem>> getAll() {
    return this.getWith("SELECT * FROM Artikellijst;");
  }

  public Function<Connection, Observable<EsselinkItem>> getWithItemId(String text) {
    return this.getWith(String.format(
        "SELECT * FROM Artikellijst WHERE artikelnummer LIKE '%s%%';", text.replace("\'", "\'\'")));
  }

  public Function<Connection, Observable<EsselinkItem>> getWithDescription(String text) {
    return this.getWith(String.format(
        "SELECT * FROM Artikellijst WHERE omschrijving LIKE '%%%s%%';", text.replace("\'", "\'\'")));
  }

  private Function<Connection, Observable<EsselinkItem>> getWith(String query) {
    return connection -> Observable.using(
        connection::createStatement,
        statement -> Observable.using(() -> statement.executeQuery(query), this::fromResultSet, ResultSet::close),
        Statement::close);
  }

  private ObservableSource<? extends EsselinkItem> fromResultSet(ResultSet resultSet) {
    return Observable.generate(() -> resultSet, (rs, emitter) -> {
      if (rs.next()) {
        String itemId = resultSet.getString("artikelnummer");
        String description = resultSet.getString("omschrijving");
        int amountPer = resultSet.getInt("prijsPer");
        String unit = resultSet.getString("eenheid");
        MonetaryAmount pricePerUnit = Money.of(resultSet.getDouble("verkoopprijs"), "EUR");

        emitter.onNext(new EsselinkItem(itemId, description, amountPer, unit, pricePerUnit));
      }
      else
        emitter.onComplete();
    });
  }
}
