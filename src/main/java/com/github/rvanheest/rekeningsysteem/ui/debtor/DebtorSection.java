package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.ZipcodeTextField;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBox;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class DebtorSection extends AbstractSection implements View, Disposable {

  private final GridPane grid = new GridPane();

  private final SearchBox<Debtor> searchBox;
  private final TextField nameTF = new TextField();
  private final TextField streetTF = new TextField();
  private final TextField numberTF = new TextField();
  private final TextField zipcodeTF = new ZipcodeTextField();
  private final TextField cityTF = new TextField();
  private final TextField vatNumberTF = new TextField();
  private final CheckBox saveDebtorCB = new CheckBox("Sla deze debiteur op");

  private final DebtorSectionPresenter presenter;

  public DebtorSection(SearchEngine<Debtor> searchEngine, HeaderManager headerManager) {
    super("Debiteur");
    this.searchBox = new DebtorSearchBox(searchEngine, headerManager);

    this.initGrid();
    this.initLabels();
    this.initTextFields();
    this.setPromptTexts();
    this.saveDebtorCB.setSelected(false);

    VBox box = new VBox(1, this.searchBox, new VBox(10, this.grid, this.saveDebtorCB));
    this.getChildren().add(box);

    this.presenter = new DebtorSectionPresenter(headerManager);
    this.presenter.attachView(this);
  }

  private void initGrid() {
    this.grid.setHgap(10);
    this.grid.setVgap(1);
    this.grid.setAlignment(Pos.TOP_CENTER);
  }

  private void initLabels() {
    this.grid.add(new Label("Naam"), 0, 0);
    this.grid.add(new Label("Straat"), 0, 1);
    this.grid.add(new Label("Nummer"), 0, 2);
    this.grid.add(new Label("Postcode"), 0, 3);
    this.grid.add(new Label("Plaats"), 0, 4);
    this.grid.add(new Label("BTW nr."), 0, 5);
  }

  private void initTextFields() {
    // this causes every TF to be 20 columns since we're in a GridPane
    this.nameTF.setPrefColumnCount(20);

    this.grid.add(this.nameTF, 1, 0);
    this.grid.add(this.streetTF, 1, 1);
    this.grid.add(this.numberTF, 1, 2);
    this.grid.add(this.zipcodeTF, 1, 3);
    this.grid.add(this.cityTF, 1, 4);
    this.grid.add(this.vatNumberTF, 1, 5);
  }

  private void setPromptTexts() {
    this.nameTF.setPromptText("naam");
    this.streetTF.setPromptText("straat");
    this.numberTF.setPromptText("nummer");
    this.zipcodeTF.setPromptText("postcode");
    this.cityTF.setPromptText("plaats");
    this.vatNumberTF.setPromptText("btw nummer");
  }

  public Observable<String> nameIntent() {
    return JavaFxObservable.valuesOf(this.nameTF.textProperty());
  }

  public Observable<String> streetIntent() {
    return JavaFxObservable.valuesOf(this.streetTF.textProperty());
  }

  public Observable<String> numberIntent() {
    return JavaFxObservable.valuesOf(this.numberTF.textProperty());
  }

  public Observable<String> zipcodeIntent() {
    return JavaFxObservable.valuesOf(this.zipcodeTF.textProperty());
  }

  public Observable<String> cityIntent() {
    return JavaFxObservable.valuesOf(this.cityTF.textProperty());
  }

  public Observable<Optional<String>> vatNumberIntent() {
    return JavaFxObservable.valuesOf(this.vatNumberTF.textProperty())
        .map(s -> "".equals(s) ? Optional.empty() : Optional.ofNullable(s));
  }

  public Observable<Boolean> saveDebtorOnSaveIntent() {
    return JavaFxObservable.valuesOf(this.saveDebtorCB.selectedProperty());
  }

  public void render(Debtor debtor) {
    if (!Objects.equals(debtor.getName(), this.nameTF.getText()))
      this.nameTF.setText(debtor.getName());

    if (!Objects.equals(debtor.getStreet(), this.streetTF.getText()))
      this.streetTF.setText(debtor.getStreet());

    if (!Objects.equals(debtor.getNumber(), this.numberTF.getText()))
      this.numberTF.setText(debtor.getNumber());

    if (!Objects.equals(debtor.getZipcode(), this.zipcodeTF.getText()))
      this.zipcodeTF.setText(debtor.getZipcode());

    if (!Objects.equals(debtor.getCity(), this.cityTF.getText()))
      this.cityTF.setText(debtor.getCity());

    if (!debtor.getVatNumber()
        .map(vatNumber -> Objects.equals(vatNumber, this.vatNumberTF.getText()))
        .orElseGet(() -> this.vatNumberTF.getText().isEmpty()))
      this.vatNumberTF.setText(debtor.getVatNumber().orElse(""));
  }

  @Override
  public void dispose() {
    Stream.of(this.presenter, this.searchBox)
        .filter(Objects::nonNull)
        .forEach(Disposable::dispose);
  }

  @Override
  public boolean isDisposed() {
    return Stream.of(this.presenter, this.searchBox)
        .filter(Objects::nonNull)
        .allMatch(Disposable::isDisposed);
  }
}
