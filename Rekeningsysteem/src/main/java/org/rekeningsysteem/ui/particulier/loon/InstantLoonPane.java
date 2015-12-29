package org.rekeningsysteem.ui.particulier.loon;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.MoneyField;
import org.rekeningsysteem.ui.textfields.PercentageField;

import rx.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class InstantLoonPane extends GridPane {

	private final TextField omschrTF = new TextField();
	private final MoneyField loonTF;
	private final PercentageField loonBtwTF = new PercentageField();

	private final Observable<String> omschrijving;
	private final Observable<Double> loon;
	private final Observable<Double> loonBtwPercentage;

	public InstantLoonPane(Currency currency) {
		this.loonTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.loon = Observables.fromProperty(this.loonTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.loonBtwPercentage = Observables.fromProperty(this.loonBtwTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
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
		Label loonL = new Label("Loon");
		Label loonBtwL = new Label("Btw percentage");

		this.add(omschrL, 0, 0);
		this.add(loonL, 0, 1);
		this.add(loonBtwL, 0, 2);

		this.add(this.omschrTF, 1, 0);
		this.add(this.loonTF, 1, 1);
		this.add(this.loonBtwTF, 1, 2);
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<Double> getLoon() {
		return this.loon;
	}

	public void setLoon(Double loon) {
		this.loonTF.setValue(BigDecimal.valueOf(loon));
	}

	public Observable<Double> getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public void setBtwPercentage(Double percentage) {
		this.loonBtwTF.setValue(BigDecimal.valueOf(percentage));
	}
}
