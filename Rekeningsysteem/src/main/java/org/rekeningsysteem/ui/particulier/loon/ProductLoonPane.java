package org.rekeningsysteem.ui.particulier.loon;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.MoneyField;
import org.rekeningsysteem.ui.textfields.NumberField;

import rx.Observable;

public class ProductLoonPane extends GridPane {

	private final TextField omschrTF = new TextField();
	private final NumberField urenTF;
	private final MoneyField uurloonTF;

	private final Observable<String> omschrijving;
	private final Observable<Double> uren;
	private final Observable<Double> uurloon;

	public ProductLoonPane(Currency currency) {
		this.urenTF = new NumberField();
		this.uurloonTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.uren = Observables.fromProperty(this.urenTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.uurloon = Observables.fromProperty(this.uurloonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);

		this.omschrTF.setPrefColumnCount(20);

		this.initContent();
	}

	private void initContent() {
		this.setPadding(new Insets(8));
		this.setHgap(10);
		this.setVgap(5);
		this.setAlignment(Pos.CENTER);

		Label omschrL = new Label("Omschrijving");
		Label uurloonL = new Label("Uurloon");
		Label urenL = new Label("Uren");

		this.add(omschrL, 0, 0);
		this.add(uurloonL, 0, 1);
		this.add(urenL, 0, 2);

		this.add(this.omschrTF, 1, 0);
		this.add(this.uurloonTF, 1, 1);
		this.add(this.urenTF, 1, 2);
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
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
