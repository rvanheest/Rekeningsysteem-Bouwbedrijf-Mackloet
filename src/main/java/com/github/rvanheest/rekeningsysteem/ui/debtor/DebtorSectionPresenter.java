package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.DebtorManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.ViewIntentBinder;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

import java.util.concurrent.TimeUnit;

public class DebtorSectionPresenter extends BasePresenter<DebtorSection, Debtor> {

  private final DebtorManager debtorManager;
  private final CompositeDisposable disposables = new CompositeDisposable();

  public DebtorSectionPresenter(DebtorManager debtorManager) {
    this.debtorManager = debtorManager;
  }

  @Override
  protected void bindIntents() {
    Completable nameIntent = createIntent(DebtorSection::nameIntent, this.debtorManager::withDebtorName);
    Completable streetIntent = createIntent(DebtorSection::streetIntent, this.debtorManager::withDebtorStreet);
    Completable numberIntent = createIntent(DebtorSection::numberIntent, this.debtorManager::withDebtorNumber);
    Completable zipcodeIntent = createIntent(DebtorSection::zipcodeIntent, this.debtorManager::withDebtorZipcode);
    Completable cityIntent = createIntent(DebtorSection::cityIntent, this.debtorManager::withDebtorCity);
    Completable vatNumberIntent = createIntent(DebtorSection::vatNumberIntent, this.debtorManager::withDebtorVatNumber);

    Completable debtorIntent = Completable.mergeArray(
        nameIntent,
        streetIntent,
        numberIntent,
        zipcodeIntent,
        cityIntent,
        vatNumberIntent
    );
    this.disposables.add(debtorIntent.subscribe());

    subscribeViewState(this.debtorManager.getDebtor(), DebtorSection::render);
  }

  private <T> Completable createIntent(ViewIntentBinder<DebtorSection, T> intentFunc, Function<T, Completable> setter) {
    return intent(intentFunc)
        .skip(1L)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .flatMapCompletable(setter);
  }

  @Override
  protected void unbindIntents() {
    if (!this.disposables.isDisposed())
      this.disposables.dispose();
  }
}
