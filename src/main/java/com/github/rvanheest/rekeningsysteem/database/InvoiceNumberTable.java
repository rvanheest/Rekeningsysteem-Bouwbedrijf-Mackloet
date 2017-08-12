package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class InvoiceNumberTable {

  public Function<Connection, Maybe<InvoiceNumber>> getInvoiceNumber() {
    String query = "SELECT number, year FROM invoice_number;";
    return connection -> Maybe.using(() -> connection.prepareStatement(query), this::extract, Statement::close);
  }

  private MaybeSource<? extends InvoiceNumber> extract(PreparedStatement prepStatement) {
    return Maybe.using(prepStatement::executeQuery, this::extract, ResultSet::close);
  }

  private MaybeSource<? extends InvoiceNumber> extract(ResultSet resultSet) throws SQLException {
    if (resultSet.next())
      return Maybe.just(new InvoiceNumber(resultSet.getInt("number"), resultSet.getInt("year")));
    else
      return Maybe.empty();
  }

  public Function<Connection, Single<InvoiceNumber>> initInvoiceNumber() {
    String query = "INSERT INTO invoice_number (number, year) VALUES (?, ?);";
    return connection -> Single.using(() -> connection.prepareStatement(query), prepStatement -> {
      int currentYear = LocalDate.now().getYear();
      prepStatement.setInt(1, 1);
      prepStatement.setInt(2, currentYear);
      prepStatement.executeUpdate();
      return Single.just(new InvoiceNumber(1, currentYear));
    }, Statement::close);
  }

  public Function<Connection, Single<InvoiceNumber>> setInvoiceNumber(InvoiceNumber invoiceNumber) {
    String query = "UPDATE invoice_number SET number=?, year=?;";
    return connection -> Single.using(() -> connection.prepareStatement(query), prepStatement -> {
      prepStatement.setInt(1, invoiceNumber.getNumber());
      prepStatement.setInt(2, invoiceNumber.getYear());
      prepStatement.executeUpdate();
      return Single.just(invoiceNumber);
    }, PreparedStatement::close);
  }
}
