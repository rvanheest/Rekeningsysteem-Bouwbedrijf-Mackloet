package org.rekeningsysteem.ui.particulier.loon;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.MoneyField;
import org.rekeningsysteem.ui.textfields.NumberField;

import rx.Observable;

public class ProductLoonPane extends GridPane {

	private final NumberField urenTF;
	private final MoneyField uurloonTF;

	private final Observable<Double> uren;
	private final Observable<Double> uurloon;

	public ProductLoonPane(Currency currency) {
		this.urenTF = new NumberField();
		this.uurloonTF = new MoneyField(currency);

		this.uren = Observables.fromProperty(this.urenTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.uurloon = Observables.fromProperty(this.uurloonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);

		this.urenTF.setPrefColumnCount(20);

		this.initContent();
	}

	private void initContent() {
		this.setPadding(new Insets(8));
		this.setHgap(10);
		this.setVgap(5);
		this.setAlignment(Pos.CENTER);

		Label uurloonL = new Label("Uurloon");
		Label urenL = new Label("Uren");

		this.add(uurloonL, 0, 0);
		this.add(urenL, 0, 1);

		this.add(this.uurloonTF, 1, 0);
		this.add(this.urenTF, 1, 1);
	}

	public Observable<Double> getUren() {
		return this.uren;
	}

	public void setUren(Double uren) {
		this.urenTF.setValue(BigDecimal.valueOf(uren));
	}

	public Observable<Double> getUurloon() {
		return this.uurloon;
	}

	public void setUurloon(Double uurloon) {
		this.uurloonTF.setValue(BigDecimal.valueOf(uurloon));
	}
}
