package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class DebtorTable {

  private int expectedChangedRows(Debtor debtor) {
    return debtor.getVatNumber().map(s -> 2).orElse(1);
  }

  public Function<Connection, Single<Boolean>> addDebtor(Debtor debtor) {
    return connection -> Single.defer(() -> {
      String format = debtor.getVatNumber()
          .map(s -> "INSERT INTO TotaalDebiteur (naam, straat, nummer, postcode, plaats, btwNummer) VALUES ('%s', '%s', '%s', '%s', '%s', '%s');")
          .orElse("INSERT INTO TotaalDebiteur (naam, straat, nummer, postcode, plaats) VALUES ('%s', '%s', '%s', '%s', '%s');");

      String query = debtor.getVatNumber()
          .map(vatNumber -> String.format(format, debtor.getName(), debtor.getStreet(), debtor.getNumber(), debtor.getZipcode(), debtor.getCity(), vatNumber))
          .orElseGet(() -> String.format(format, debtor.getName(), debtor.getStreet(), debtor.getNumber(), debtor.getZipcode(), debtor.getCity()));

      return this.update(query).apply(connection).map(i -> i == expectedChangedRows(debtor));
    });
  }

  public Function<Connection, Single<Debtor>> addAndGetDebtor(Debtor debtor) {
    return connection -> Single.defer(() -> this.addDebtor(debtor).apply(connection)
        .flatMap(b -> getDebtorId(debtor).apply(connection)
            .map(id -> new Debtor(Optional.of(id), debtor.getName(), debtor.getStreet(), debtor.getNumber(),
                debtor.getZipcode(), debtor.getCity(), debtor.getVatNumber()))));
  }

  private Function<Connection, Single<Integer>> getDebtorId(Debtor debtor) {
    return connection -> Single.defer(() -> {
      String format = debtor.getVatNumber()
          .map(s -> "SELECT debiteurID FROM TotaalDebiteur WHERE naam = '%s' AND straat = '%s' AND nummer = '%s' AND postcode = '%s' AND plaats = '%s' AND btwNummer = '%s';")
          .orElse("SELECT debiteurID FROM TotaalDebiteur WHERE naam = '%s' AND straat = '%s' AND nummer = '%s' AND postcode = '%s' AND plaats = '%s';");

      String query = debtor.getVatNumber()
          .map(vatNumber -> String.format(format, debtor.getName(), debtor.getStreet(), debtor.getNumber(), debtor.getZipcode(), debtor.getCity(), vatNumber))
          .orElseGet(() -> String.format(format, debtor.getName(), debtor.getStreet(), debtor.getNumber(), debtor.getZipcode(), debtor.getCity()));

      return Single.<Integer, PreparedStatement>using(() -> connection.prepareStatement(query),
          prepStatement -> Single.using(prepStatement::executeQuery, resultSet -> {
            if (resultSet.next())
              return Single.just(resultSet.getInt(1));
            else
              return Single.<Integer>error(new Exception("no last inserted rowid"));
          }, ResultSet::close), Statement::close);
    });
  }

  public Function<Connection, Single<Boolean>> deleteDebtor(Debtor debtor) {
    return connection -> this.update(debtor.getDebtorID()
        .map(id -> String.format("DELETE FROM TotaalDebiteur WHERE debiteurID = '%s';", id))
        .orElseThrow(() -> new IllegalArgumentException("This debtor doesn't contain an id: " + debtor.toString())))
        .apply(connection)
        .map(i -> i == expectedChangedRows(debtor));
  }

  public Function<Connection, Single<Boolean>> updateDebtor(Debtor oldDebtor, Debtor newDebtor) {
    return connection -> Single.defer(() -> {
      String format = "UPDATE TotaalDebiteur SET naam = '%s', straat = '%s', nummer = '%s', postcode = '%s', plaats = '%s', btwNummer = %s WHERE debiteurID = %s;";
      String query = oldDebtor.getDebtorID()
          .map(oldId -> String.format(format,
              newDebtor.getName(), newDebtor.getStreet(), newDebtor.getNumber(), newDebtor.getZipcode(),
              newDebtor.getCity(), newDebtor.getVatNumber().map(s -> String.format("'%s'", s)).orElse("NULL"),
              oldId))
          .orElseThrow(() -> new IllegalArgumentException("This debtor doesn't contain an id: " + oldDebtor.toString()));

      int res = oldDebtor.getVatNumber()
          .map(s -> newDebtor.getVatNumber().map(s2 -> 2).orElse(2))
          .orElseGet(() -> newDebtor.getVatNumber().map(s2 -> 4).orElse(1));

      return this.update(query).apply(connection).map(i -> i == res);
    });
  }

  private Function<Connection, Single<Integer>> update(String query) {
    return connection -> Single.using(
        connection::createStatement,
        statement -> Single.fromCallable(() -> statement.executeUpdate(query)),
        Statement::close);
  }

  public Function<Connection, Observable<Debtor>> getAll() {
    return this.getWith("SELECT * FROM TotaalDebiteur;");
  }

  public Function<Connection, Observable<Debtor>> getWithName(String text) {
    return this.getWith(
        String.format("SELECT * FROM TotaalDebiteur WHERE naam LIKE '%%%s%%';", text.replace("\'", "\'\'")));
  }

  private Function<Connection, Observable<Debtor>> getWith(String query) {
    return connection -> Observable.using(
        connection::createStatement,
        statement -> Observable.using(() -> statement.executeQuery(query), this::fromResultSet, ResultSet::close),
        Statement::close);
  }

  private Observable<Debtor> fromResultSet(ResultSet resultSet) {
    return Observable.generate(() -> resultSet, (rs, emitter) -> {
      if (rs.next()) {
        int debtorId = rs.getInt("debiteurID");
        String name = rs.getString("naam");
        String street = rs.getString("straat");
        String number = rs.getString("nummer");
        String zipcode = rs.getString("postcode");
        String city = rs.getString("plaats");
        String vatNumber = rs.getString("btwNummer");

        emitter.onNext(new Debtor(debtorId, name, street, number, zipcode, city, vatNumber));
      }
      else
        emitter.onComplete();
    });
  }
}
