package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateSection extends AbstractSection implements View, Disposable {

  private static final StringConverter<LocalDate> dateConverter = new DateStringConverter();

  private final DatePicker datePicker = new DatePicker(LocalDate.now());
  private final DateSectionPresenter presenter;

  public DateSection(HeaderManager headerManager) {
    super("Datum");

    this.datePicker.setConverter(DateSection.dateConverter);
    this.getChildren().add(this.datePicker);

    this.presenter = new DateSectionPresenter(headerManager);
    this.presenter.attachView(this);
  }

  public Observable<LocalDate> dateIntent() {
    return JavaFxObservable.valuesOf(this.datePicker.valueProperty());
  }

  public void render(LocalDate localDate) {
    if (!Objects.equals(localDate, this.datePicker.getValue()))
      this.datePicker.setValue(localDate);
  }

  @Override
  public void dispose() {
    this.presenter.dispose();
  }

  @Override
  public boolean isDisposed() {
    return this.presenter.isDisposed();
  }

  private static class DateStringConverter extends StringConverter<LocalDate> {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    @Override
    public String toString(LocalDate date) {
      return DateStringConverter.formatter.format(date);
    }

    @Override
    public LocalDate fromString(String string) {
      return LocalDate.parse(string, DateStringConverter.formatter);
    }
  }
}
