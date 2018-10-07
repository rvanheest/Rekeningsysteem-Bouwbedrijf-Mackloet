package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.disposables.CompositeDisposable;

public class SignOfferSectionPresenter extends BasePresenter<SignOfferSection, Boolean> {

  private final OfferManager offerManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public SignOfferSectionPresenter(OfferManager offerManager) {
    this.offerManager = offerManager;
  }

  @Override
  protected void bindIntents() {
    this.disposables.add(
        intent(SignOfferSection::signOfferIntent)
            .skip(1L)
            .flatMapCompletable(this.offerManager::withSign)
            .subscribe()
    );

    subscribeViewState(this.offerManager.getSign(), SignOfferSection::render);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
