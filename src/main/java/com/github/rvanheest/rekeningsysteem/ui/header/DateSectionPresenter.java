package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class DateSectionPresenter extends BasePresenter<DateSection, LocalDate> {

  private final HeaderManager headerManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public DateSectionPresenter(HeaderManager headerManager) {
    this.headerManager = headerManager;
  }

  @Override
  protected void bindIntents() {
    Completable dateIntent = intent(DateSection::dateIntent)
        .skip(1L)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .flatMapCompletable(this.headerManager::withDate);

    this.disposables.add(dateIntent.subscribe());

    subscribeViewState(this.headerManager.getHeader().map(Header::getDate), DateSection::render);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
