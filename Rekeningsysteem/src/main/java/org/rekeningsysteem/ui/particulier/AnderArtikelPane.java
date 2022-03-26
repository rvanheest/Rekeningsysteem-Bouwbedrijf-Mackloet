package org.rekeningsysteem.ui.particulier;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.MoneyField;
import org.rekeningsysteem.ui.textfields.PercentageField;

public class AnderArtikelPane extends GridPane {

	private final TextField omschrTF = new TextField();
	private final MoneyField prijsTF;
	private final PercentageField btwPercentageTF = new PercentageField();
	private final CheckBox verlegdCB = new CheckBox();

	private final Observable<String> omschrijving;
	private final Observable<Double> prijs;
	private final Observable<Double> btwPercentage;
	private final Observable<Boolean> verlegd;

	public AnderArtikelPane(Currency currency) {
		this.prijsTF = new MoneyField(currency);

		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.prijs = Observables.fromProperty(this.prijsTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.btwPercentage = Observables.fromProperty(this.btwPercentageTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
				.map(BigDecimal::doubleValue);
		this.verlegd = Observables.fromProperty(this.verlegdCB.selectedProperty())
				.map(Boolean::booleanValue);

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
		Label verlegdL = new Label("Verlegd");

		this.add(omschrL, 0, 0);
		this.add(prijsL, 0, 1);
		this.add(btwL, 0, 2);
		this.add(verlegdL, 0, 3);

		this.add(this.omschrTF, 1, 0);
		this.add(this.prijsTF, 1, 1);
		this.add(this.btwPercentageTF, 1, 2);
		this.add(this.verlegdCB, 1, 3);
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

	public Observable<BtwPercentage> getBtwPercentage() {
		return Observable.combineLatest(this.btwPercentage, this.verlegd, BtwPercentage::new);
	}

	public void setBtwPercentage(BtwPercentage btwPercentage) {
		this.btwPercentageTF.setValue(BigDecimal.valueOf(btwPercentage.getPercentage()));
		this.verlegdCB.setSelected(btwPercentage.isVerlegd());
	}
}
