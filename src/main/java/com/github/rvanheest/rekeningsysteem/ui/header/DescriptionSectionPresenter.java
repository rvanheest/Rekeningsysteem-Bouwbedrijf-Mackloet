package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.DescriptionManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

import java.util.concurrent.TimeUnit;

public class DescriptionSectionPresenter extends BasePresenter<DescriptionSection, String> {

  private final DescriptionManager descriptionManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public DescriptionSectionPresenter(DescriptionManager descriptionManager) {
    this.descriptionManager = descriptionManager;
  }

  @Override
  protected void bindIntents() {
    Completable descriptionIntent = intent(DescriptionSection::descriptionIntent)
        .skip(1L)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .flatMapCompletable(this.descriptionManager::withDescription);

    this.disposables.add(descriptionIntent.subscribe());

    subscribeViewState(this.descriptionManager.getDescription(), DescriptionSection::render);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
