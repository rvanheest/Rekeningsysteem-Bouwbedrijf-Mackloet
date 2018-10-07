package com.github.rvanheest.rekeningsysteem.ui.body;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.control.TextArea;

import java.util.Objects;

public class OfferTextSection extends AbstractSection implements View, Disposable {

  private final TextArea offerTextTA = new TextArea();

  private final OfferTextSectionPresenter presenter;

  public OfferTextSection(OfferManager offerManager) {
    super("Omschrijving");

    this.offerTextTA.setPrefColumnCount(50);
    this.offerTextTA.setPrefRowCount(20);

    this.presenter = new OfferTextSectionPresenter(offerManager);
    this.presenter.attachView(this);

    this.getChildren().add(this.offerTextTA);
  }

  public Observable<String> offerTextIntent() {
    return JavaFxObservable.valuesOf(this.offerTextTA.textProperty());
  }

  public void render(String offerText) {
    if (!Objects.equals(offerText, this.offerTextTA.getText()))
      this.offerTextTA.setText(offerText);
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
