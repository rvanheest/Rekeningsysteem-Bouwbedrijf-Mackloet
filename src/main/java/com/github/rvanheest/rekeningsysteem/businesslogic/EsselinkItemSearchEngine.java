package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.List;
import java.util.NoSuchElementException;

import static com.github.rvanheest.rekeningsysteem.businesslogic.EsselinkItemSearchMode.DESCRIPTION;

public class EsselinkItemSearchEngine implements SearchEngine<EsselinkItem> {

  private final Database database;

  private final BehaviorSubject<EsselinkItemSearchMode> searchMode = BehaviorSubject.createDefault(DESCRIPTION);

  public EsselinkItemSearchEngine(Database database) {
    this.database = database;
  }

  public Completable withSearchMode(EsselinkItemSearchMode searchMode) {
    this.searchMode.onNext(searchMode);
    return Completable.complete();
  }

  @Override
  public Observable<List<EsselinkItem>> suggest(String text) {
    return this.searchMode
        .flatMap(searchMode -> {
          switch (searchMode) {
            case ID:
              return this.database.getEsselinkItemWithId(text);
            case DESCRIPTION:
              return this.database.getEsselinkItemWithDescription(text);
            default:
              return Observable.error(
                  new NoSuchElementException(String.format("Unknown searchMode: %s", searchMode)));
          }
        })
        .toList()
        .toObservable();
  }
}

enum EsselinkItemSearchMode {
  ID, DESCRIPTION
}
