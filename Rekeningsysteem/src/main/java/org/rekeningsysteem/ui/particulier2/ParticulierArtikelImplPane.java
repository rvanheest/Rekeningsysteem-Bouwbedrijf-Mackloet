package org.rekeningsysteem.ui.particulier2;

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
import org.rekeningsysteem.ui.textfields.PercentageField;

import rx.Observable;

// TODO AnderArtikelPane
public class ParticulierArtikelImplPane extends GridPane {

	private final TextField omschrTF = new TextField();
	private final MoneyField prijsTF;
	private final PercentageField btwPercentageTF = new PercentageField();

	private final Observable<String> omschrijving;
	private final Observable<Double> prijs;
	private final Observable<Double> btwPercentage;

	public ParticulierArtikelImplPane(Currency currency) {
		this.prijsTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.prijs = Observables.fromProperty(this.prijsTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.btwPercentage = Observables.fromProperty(this.btwPercentageTF.valueProperty())
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
		Label prijsL = new Label("Prijs");
		Label btwL = new Label("Btw percentage");

		this.add(omschrL, 0, 0);
		this.add(prijsL, 0, 1);
		this.add(btwL, 0, 2);

		this.add(this.omschrTF, 1, 0);
		this.add(this.prijsTF, 1, 1);
		this.add(this.btwPercentageTF, 1, 2);
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<Double> getPrijs() {
		return this.prijs;
	}

	public void setPrijs(Double prijs) {
		this.prijsTF.setValue(BigDecimal.valueOf(prijs));
	}

	public Observable<Double> getBtwPercentage() {
		return this.btwPercentage;
	}

	public void setBtwPercentage(Double btwPercentage) {
		this.btwPercentageTF.setValue(BigDecimal.valueOf(btwPercentage));
	}
}
