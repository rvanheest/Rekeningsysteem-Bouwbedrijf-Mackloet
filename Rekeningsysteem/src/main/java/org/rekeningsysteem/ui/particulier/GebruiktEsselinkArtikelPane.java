package org.rekeningsysteem.ui.particulier;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.IntegerField;
import org.rekeningsysteem.ui.textfields.MoneyField;
import org.rekeningsysteem.ui.textfields.NumberField;

import rx.Observable;

public class GebruiktEsselinkArtikelPane extends GridPane {

	private final TextField artNrTF = new TextField();
	private final TextField omschrTF = new TextField();
	private final IntegerField prijsPerTF = new IntegerField();
	private final TextField eenheidTF = new TextField();
	private final MoneyField prijsTF;
	private final NumberField aantalTF = new NumberField();

	private final Observable<String> artikelNummer;
	private final Observable<String> omschrijving;
	private final Observable<Integer> prijsPer;
	private final Observable<String> eenheid;
	private final Observable<Double> verkoopPrijs;
	private final Observable<Double> aantal;

	public GebruiktEsselinkArtikelPane(Currency currency) {
		this.prijsTF = new MoneyField(currency);

		this.artikelNummer = Observables.fromProperty(this.artNrTF.textProperty());
		this.omschrijving = Observables.fromProperty(this.omschrTF.textProperty());
		this.prijsPer = Observables.fromProperty(this.prijsPerTF.valueProperty())
				.filter(Objects::nonNull);
		this.eenheid = Observables.fromProperty(this.eenheidTF.textProperty());
		this.verkoopPrijs = Observables.fromProperty(this.prijsTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.aantal = Observables.fromProperty(this.aantalTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);

		this.aantalTF.setPrefColumnCount(20);

		this.initContent();
	}

	private void initContent() {
		this.setPadding(new Insets(8));
		this.setHgap(10);
		this.setVgap(5);
		this.setAlignment(Pos.CENTER);

		this.add(new Label("Artikelnummer"), 0, 0);
		this.add(new Label("Omschrijving"), 0, 1);
		this.add(new Label("Prijs per"), 0, 2);
		this.add(new Label("Eenheid"), 0, 3);
		this.add(new Label("Prijs"), 0, 4);
		this.add(new Label("Aantal"), 0, 5);

		this.add(this.artNrTF, 1, 0);
		this.add(this.omschrTF, 1, 1);
		this.add(this.prijsPerTF, 1, 2);
		this.add(this.eenheidTF, 1, 3);
		this.add(this.prijsTF, 1, 4);
		this.add(this.aantalTF, 1, 5);
	}

	public Observable<String> getArtikelnummer() {
		return this.artikelNummer;
	}

	public void setArtikelnummer(String artikelnummer) {
		this.artNrTF.setText(artikelnummer);
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTF.setText(omschrijving);
	}

	public Observable<Integer> getPrijsPer() {
		return this.prijsPer;
	}

	public void setPrijsPer(Integer prijsPer) {
		this.prijsPerTF.setValue(prijsPer);
	}

	public Observable<String> getEenheid() {
		return this.eenheid;
	}

	public void setEenheid(String eenheid) {
		this.eenheidTF.setText(eenheid);
	}

	public Observable<Double> getVerkoopPrijs() {
		return this.verkoopPrijs;
	}

	public void setVerkoopPrijs(Double prijs) {
		this.prijsTF.setValue(BigDecimal.valueOf(prijs));
	}

	public Observable<Double> getAantal() {
		return this.aantal;
	}

	public void setAantal(Double aantal) {
		this.aantalTF.setValue(BigDecimal.valueOf(aantal));
	}
}
