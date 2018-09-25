package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Observable;

import java.util.Collections;
import java.util.List;

public class DebtorSearchEngine implements SearchEngine<Debtor> {

  private final Database database;

  public DebtorSearchEngine(Database database) {
    this.database = database;
  }

  @Override
  public Observable<List<Debtor>> suggest(String text) {
    if (text.length() <= 2)
      return Observable.just(Collections.emptyList());
    else
      return this.database.getDebtorWithName(text)
          .toList()
          .toObservable();
  }
}
