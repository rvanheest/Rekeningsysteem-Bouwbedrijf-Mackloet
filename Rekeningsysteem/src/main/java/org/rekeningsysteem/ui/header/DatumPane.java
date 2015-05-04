package org.rekeningsysteem.ui.header;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public class DatumPane extends Page {

	private final DatePicker datePicker = new DatePicker(LocalDate.now());
	private final Observable<LocalDate> datum = Observables.fromProperty(this.datePicker.valueProperty());

	public DatumPane() {
		super("Datum");

		this.datePicker.setConverter(new DatumStringConverter());
		this.getChildren().add(this.datePicker);
	}

	public Observable<LocalDate> getDatum() {
		return this.datum;
	}

	public void setDatum(LocalDate datum) {
		this.datePicker.setValue(datum);
	}

	private class DatumStringConverter extends StringConverter<LocalDate> {

		private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

		@Override
		public String toString(LocalDate date) {
			return this.formatter.format(date);
		}

		@Override
		public LocalDate fromString(String string) {
			return LocalDate.parse(string, this.formatter);
		}
	}
}
