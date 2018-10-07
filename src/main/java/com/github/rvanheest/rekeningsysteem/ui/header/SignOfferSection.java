package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.control.CheckBox;

public class SignOfferSection extends AbstractSection implements View, Disposable {

  private final CheckBox signCB = new CheckBox("ondertekenen");

  private final SignOfferSectionPresenter presenter;

  public SignOfferSection(OfferManager offerManager) {
    super("Ondertekenen");

    this.presenter = new SignOfferSectionPresenter(offerManager);
    this.presenter.attachView(this);

    this.getChildren().add(this.signCB);
  }

  public Observable<Boolean> signOfferIntent() {
    return JavaFxObservable.valuesOf(this.signCB.selectedProperty());
  }

  public void render(Boolean sign) {
    if (sign != this.signCB.isSelected())
      this.signCB.setSelected(sign);
  }

  @Override
  public void dispose() {
    this.presenter.dispose();
  }

  @Override
  public boolean isDisposed() {
    return this.presenter.isDisposed();
  }
}
