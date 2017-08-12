package com.github.rvanheest.rekeningsysteem.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.strati.functional.Try;

import java.sql.Connection;
import java.util.Optional;

public class InvoiceNumberGenerator {

  private final InvoiceNumberTable table;

  public InvoiceNumberGenerator() {
    this(new InvoiceNumberTable());
  }

  public InvoiceNumberGenerator(InvoiceNumberTable table) {
    this.table = table;
  }

  public Function<Connection, Single<String>> calculateAndPersist() {
    return connection -> this.table.getInvoiceNumber().apply(connection)
        // only call initInvoiceNumber when the stream is really empty.
        // because defaultIfEmpty or switchIfEmpty is not lazily evaluated,
        // we require to do a trick with wrapping the value in a java.util.Optional.
        .map(Optional::of)
        .defaultIfEmpty(Optional.empty())
        .flatMapSingle(optInvoiceNumber -> optInvoiceNumber
            .map(invoiceNumber -> this.table.setInvoiceNumber(invoiceNumber.next()))
            .orElseGet(this.table::initInvoiceNumber)
            .apply(connection))
        .map(InvoiceNumber::formatted);
  }
}
