package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxView;
import io.reactivex.disposables.CompositeDisposable;

public class DebtorSearchBoxPresenter extends SearchBoxPresenter<Debtor> {

  private final HeaderManager headerManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public DebtorSearchBoxPresenter(SearchEngine<Debtor> searchEngine, HeaderManager headerManager) {
    super(searchEngine);
    this.headerManager = headerManager;
  }

  @Override
  protected void bindIntents() {
    super.bindIntents();

    this.disposables.add(
        intent(SearchBoxView::selectedItemIntent)
            .flatMapCompletable(this.headerManager::withDebtor)
            .subscribe()
    );
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
