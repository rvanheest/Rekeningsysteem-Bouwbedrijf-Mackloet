package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

import java.util.concurrent.TimeUnit;

public class DescriptionSectionPresenter extends BasePresenter<DescriptionSection, String> {

  private final NormalInvoiceManager normalInvoiceManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public DescriptionSectionPresenter(NormalInvoiceManager NormalInvoiceManager) {
    this.normalInvoiceManager = NormalInvoiceManager;
  }

  @Override
  protected void bindIntents() {
    Completable descriptionIntent = intent(DescriptionSection::descriptionIntent)
        .skip(1L)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .flatMapCompletable(this.normalInvoiceManager::withDescription);

    this.disposables.add(descriptionIntent.subscribe());

    subscribeViewState(this.normalInvoiceManager.getDescription(), DescriptionSection::render);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
