package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Optional;

public class Database implements AutoCloseable {

  private final DatabaseConnection dbConnection;
  private final DebtorTable debtorTable;
  private final EsselinkItemTable esselinkItemTable;
  private final InvoiceNumberTable invoiceNumberTable;

  public Database(DatabaseConnection dbConnection) {
    this.dbConnection = dbConnection;
    this.debtorTable = new DebtorTable();
    this.esselinkItemTable = new EsselinkItemTable();
    this.invoiceNumberTable = new InvoiceNumberTable();
  }

  @Override
  public void close() throws Exception {
    this.dbConnection.closeConnectionPool();
  }

  // debtor queries

  public Observable<Debtor> getDebtorWithName(String text) {
    return this.dbConnection.doTransactionObservable(this.debtorTable.getWithName(text));
  }

  // EsselinkItem queries

  public Observable<Integer> loadEsselinkItems(Observable<EsselinkItem> items) {
    return this.dbConnection.doTransactionObservable(connection ->
        this.esselinkItemTable.clearData().apply(connection)
            .andThen(items)
            .window(100L)
            .flatMapSingle(itemStream -> this.esselinkItemTable.insertAll(itemStream).apply(connection))
            .scan(0, Math::addExact)
    );
  }

  public Observable<EsselinkItem> getAllEsselinkItems() {
    return this.dbConnection.doTransactionObservable(this.esselinkItemTable.getAll());
  }

  public Observable<EsselinkItem> getEsselinkItemWithId(String id) {
    return this.dbConnection.doTransactionObservable(this.esselinkItemTable.getWithItemId(id));
  }

  public Observable<EsselinkItem> getEsselinkItemWithDescription(String description) {
    return this.dbConnection.doTransactionObservable(this.esselinkItemTable.getWithDescription(description));
  }

  // InvoiceNumber queries

  public Single<String> newInvoiceNumber() {
    return this.dbConnection.doTransactionSingle(connection ->
        this.invoiceNumberTable.getInvoiceNumber().apply(connection)
            // only call initInvoiceNumber when the stream is really empty.
            // because defaultIfEmpty or switchIfEmpty is not lazily evaluated,
            // we require to do a trick with wrapping the value in a java.util.Optional.
            .map(Optional::of)
            .defaultIfEmpty(Optional.empty())
            .flatMapSingle(optInvoiceNumber -> optInvoiceNumber
                .map(invoiceNumber -> this.invoiceNumberTable.setInvoiceNumber(invoiceNumber.next()))
                .orElseGet(this.invoiceNumberTable::initInvoiceNumber)
                .apply(connection))
            .map(InvoiceNumber::formatted));
  }
}
