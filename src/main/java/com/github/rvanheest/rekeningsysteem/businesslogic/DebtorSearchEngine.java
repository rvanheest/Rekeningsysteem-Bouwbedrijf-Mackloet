package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Observable;

import java.util.List;

public class DebtorSearchEngine implements SearchEngine<Debtor> {

  private final DatabaseConnection dbConnection;
  private final DebtorTable table;

  public DebtorSearchEngine(DatabaseConnection dbConnection, DebtorTable table) {
    this.dbConnection = dbConnection;
    this.table = table;
  }

  @Override
  public Observable<List<Debtor>> suggest(String text) {
    return this.dbConnection.doTransactionObservable(this.table.getWithName(text))
        .toList()
        .toObservable();
  }
}
