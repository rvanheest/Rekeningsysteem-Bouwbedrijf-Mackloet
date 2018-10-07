package com.github.rvanheest.rekeningsysteem.ui.body;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

import java.util.concurrent.TimeUnit;

public class OfferTextSectionPresenter extends BasePresenter<OfferTextSection, String> {

  private final OfferManager offerManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public OfferTextSectionPresenter(OfferManager offerManager) {
    this.offerManager = offerManager;
  }

  @Override
  protected void bindIntents() {
    this.disposables.add(
        intent(OfferTextSection::offerTextIntent)
            .skip(1L)
            .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .flatMapCompletable(this.offerManager::withText)
            .subscribe()
    );

    subscribeViewState(this.offerManager.getText(), OfferTextSection::render);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
