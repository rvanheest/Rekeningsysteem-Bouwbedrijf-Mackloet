package com.github.rvanheest.rekeningsysteem.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.Database;
import io.reactivex.Single;

public class InvoiceNumberGenerator {

  private final Database database;

  public InvoiceNumberGenerator(Database database) {
    this.database = database;
  }

  public Single<String> calculateAndPersist() {
    return this.database.newInvoiceNumber();
  }
}
