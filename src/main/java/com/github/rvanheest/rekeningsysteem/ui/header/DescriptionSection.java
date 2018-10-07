package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.control.TextArea;

import java.util.Objects;

public class DescriptionSection extends AbstractSection implements View, Disposable {

  private final TextArea descriptionTA = new TextArea();

  private final DescriptionSectionPresenter presenter;

  public DescriptionSection(NormalInvoiceManager normalInvoiceManager) {
    super("Omschrijving");

    this.descriptionTA.setPrefColumnCount(50);
    this.descriptionTA.setPrefRowCount(5);

    this.presenter = new DescriptionSectionPresenter(normalInvoiceManager);
    this.presenter.attachView(this);

    this.getChildren().add(this.descriptionTA);
  }

  public Observable<String> descriptionIntent() {
    return JavaFxObservable.valuesOf(this.descriptionTA.textProperty());
  }

  public void render(String description) {
    if (!Objects.equals(description, this.descriptionTA.getText()))
      this.descriptionTA.setText(description);
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
