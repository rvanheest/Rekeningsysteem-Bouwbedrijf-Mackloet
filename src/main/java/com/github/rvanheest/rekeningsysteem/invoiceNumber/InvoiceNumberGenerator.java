package com.github.rvanheest.rekeningsysteem.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import io.strati.functional.Try;

import java.sql.Connection;

public class InvoiceNumberGenerator {

  private final InvoiceNumberTable table;

  public InvoiceNumberGenerator(InvoiceNumberTable table) {
    this.table = table;
  }

  public Try<String> calculateAndPersist(Connection connection) {
    return this.table.getInvoiceNumber(connection)
        .flatMap(invoiceNumberOptional -> invoiceNumberOptional
            .map(invoiceNumber -> this.table.setInvoiceNumber(invoiceNumber.next(), connection))
            .orElseGet(() -> this.table.initInvoiceNumber(connection)))
        .map(InvoiceNumber::formatted);
  }
}
