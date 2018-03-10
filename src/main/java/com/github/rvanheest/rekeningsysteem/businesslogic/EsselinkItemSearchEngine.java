package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.EsselinkItemTable;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.sql.Connection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static com.github.rvanheest.rekeningsysteem.businesslogic.EsselinkItemSearchMode.DESCRIPTION;

public class EsselinkItemSearchEngine implements SearchEngine<EsselinkItem> {

  private final DatabaseConnection dbConnection;
  private final EsselinkItemTable table;

  private final BehaviorSubject<EsselinkItemSearchMode> searchMode = BehaviorSubject.createDefault(DESCRIPTION);

  public EsselinkItemSearchEngine(DatabaseConnection dbConnection, EsselinkItemTable table) {
    this.dbConnection = dbConnection;
    this.table = table;
  }

  public Completable withSearchMode(EsselinkItemSearchMode searchMode) {
    this.searchMode.onNext(searchMode);
    return Completable.complete();
  }

  @Deprecated
  protected Observable<EsselinkItem> searchFromId(String id) {
    return this.dbConnection.doTransactionObservable(this.table.getWithItemId(id));
  }

  @Deprecated
  protected Observable<EsselinkItem> searchFromDescription(String description) {
    return this.dbConnection.doTransactionObservable(this.table.getWithDescription(description));
  }

  @Override
  public Observable<List<EsselinkItem>> suggest(String text) {
    return this.searchMode
        .<Function<Connection, Observable<EsselinkItem>>>map(searchMode -> {
          switch (searchMode) {
            case ID:
              return this.table.getWithItemId(text);
            case DESCRIPTION:
              return this.table.getWithDescription(text);
            default:
              return connection -> Observable.error(
                  new NoSuchElementException(String.format("Unknown searchMode: %s", searchMode)));
          }
        })
        .flatMap(this.dbConnection::doTransactionObservable)
        .toList()
        .toObservable();
  }
}

enum EsselinkItemSearchMode {
  ID, DESCRIPTION
}
