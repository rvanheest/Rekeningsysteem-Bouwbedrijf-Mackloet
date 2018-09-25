package com.github.rvanheest.rekeningsysteem.database;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Observable;

public class Database implements AutoCloseable {

  private final DatabaseConnection dbConnection;
  private final DebtorTable debtorTable;
  private final EsselinkItemTable esselinkItemTable;

  public Database(DatabaseConnection dbConnection) {
    this.dbConnection = dbConnection;
    this.debtorTable = new DebtorTable();
    this.esselinkItemTable = new EsselinkItemTable();
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

  public Observable<EsselinkItem> getEsselinkItemWithId(String id) {
    return this.dbConnection.doTransactionObservable(this.esselinkItemTable.getWithItemId(id));
  }

  public Observable<EsselinkItem> getEsselinkItemWithDescription(String description) {
    return this.dbConnection.doTransactionObservable(this.esselinkItemTable.getWithDescription(description));
  }
}
