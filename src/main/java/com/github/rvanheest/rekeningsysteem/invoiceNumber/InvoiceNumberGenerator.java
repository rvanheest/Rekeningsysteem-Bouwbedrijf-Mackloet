package com.github.rvanheest.rekeningsysteem.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.strati.functional.Try;

import java.sql.Connection;

public class InvoiceNumberGenerator {

  private final InvoiceNumberTable table;

  public InvoiceNumberGenerator() {
    this(new InvoiceNumberTable());
  }

  public InvoiceNumberGenerator(InvoiceNumberTable table) {
    this.table = table;
  }

  // TODO not sure if this works with Maybe and Single
  public Function<Connection, Single<String>> calculateAndPersist() {
    return connection -> this.table.getInvoiceNumber().apply(connection)
        .flatMap(invoiceNumber -> this.table.setInvoiceNumber(invoiceNumber.next()).apply(connection).toMaybe())
        .switchIfEmpty(this.table.initInvoiceNumber().apply(connection).toMaybe())
        .map(InvoiceNumber::formatted)
        .toSingle();
  }
}
