package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import io.strati.functional.Optional;
import io.strati.functional.Try;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public final class InvoiceNumberTable {

  public Try<Optional<InvoiceNumber>> getInvoiceNumber(Connection connection) {
    String query = "SELECT number, year FROM invoice_number;";
    return Try.ofFailable(() -> {
      try (PreparedStatement prepStatement = connection.prepareStatement(query);
          ResultSet resultSet = prepStatement.executeQuery()) {
        if (resultSet.next())
          return Optional.of(new InvoiceNumber(
              resultSet.getInt("number"),
              resultSet.getInt("year")));
        else
          return Optional.empty();
      }
    });
  }

  public Try<InvoiceNumber> initInvoiceNumber(Connection connection) {
    String query = "INSERT INTO invoice_number (number, year) VALUES (?, ?);";
    return Try.ofFailable(() -> {
      try (PreparedStatement prepStatement = connection.prepareStatement(query)) {
        int currentYear = LocalDate.now().getYear();
        prepStatement.setInt(1, 1);
        prepStatement.setInt(2, currentYear);
        prepStatement.executeUpdate();
        return new InvoiceNumber(1, currentYear);
      }
    });
  }

  public Try<InvoiceNumber> setInvoiceNumber(InvoiceNumber invoiceNumber, Connection connection) {
    String query = "UPDATE invoice_number SET number=?, year=?;";
    return Try.ofFailable(() -> {
      try (PreparedStatement prepStatement = connection.prepareStatement(query)) {
        prepStatement.setInt(1, invoiceNumber.getNumber());
        prepStatement.setInt(2, invoiceNumber.getYear());
        prepStatement.executeUpdate();

        return invoiceNumber;
      }
    });
  }
}
