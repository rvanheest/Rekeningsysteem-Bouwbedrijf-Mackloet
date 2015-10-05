package org.rekeningsysteem.ui.particulier2;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.NumberField;
import org.rekeningsysteem.ui.textfields.PercentageField;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;

import rx.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class EsselinkParticulierArtikelPane extends GridPane {

	private final NumberField aantalTF = new NumberField();
	private final PercentageField btwPercentageTF = new PercentageField();

	private Observable<Double> aantal;
	private final Observable<Double> btwPercentage;

	private final AbstractSearchBox<EsselinkArtikel> searchBox;
	private final ToggleGroup searchType = new ToggleGroup();
	private final RadioButton artNr = new RadioButton("Artikelnummer");
	private final RadioButton omschr = new RadioButton("Omschrijving");

	public EsselinkParticulierArtikelPane(Currency currency,
			AbstractSearchBox<EsselinkArtikel> searchField) {
		this.searchBox = searchField;

		this.artNr.setToggleGroup(this.searchType);
		this.artNr.setSelected(false);

		this.omschr.setToggleGroup(this.searchType);
		this.omschr.setSelected(true);

		this.setPadding(new Insets(8));
		this.setHgap(10);
		this.setVgap(5);
		this.setAlignment(Pos.CENTER);

		VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.getChildren().add(this.artNr);
		vbox.getChildren().add(this.omschr);

		Region spacer = new Region();
		spacer.setPadding(new Insets(5, 0, 5, 0));

		Label artNrOmschr = new Label("geen artikel geselecteerd");
		Label extraInfo = new Label();
		artNrOmschr.getStyleClass().add("no-item-found");
		this.getArtikel().subscribe(ea -> {
			artNrOmschr.getStyleClass().remove("no-item-found");
			artNrOmschr.setText(ea.getArtikelNummer() + "\t" + ea.getOmschrijving());
			extraInfo.setText(currency.getSymbol() + " " + ea.getVerkoopPrijs().getBedrag()
					+ " per " + ea.getPrijsPer() + " " + ea.getEenheid());

			searchField.clear();
		});

		this.aantal = Observables.fromProperty(this.aantalTF.valueProperty())
				.filter(Objects::nonNull)
				.map(BigDecimal::doubleValue);
		this.aantalTF.setPrefColumnCount(10);

		this.btwPercentage = Observables.fromProperty(this.btwPercentageTF.valueProperty())
				.map(n -> Objects.isNull(n) ? BigDecimal.ZERO : n)
				.map(BigDecimal::doubleValue);

		GridPane inner = new GridPane();
		inner.setPadding(new Insets(0));
		inner.setHgap(10);
		inner.setVgap(5);
		inner.setAlignment(Pos.CENTER);
		inner.add(new Label("Aantal"), 0, 0);
		inner.add(this.aantalTF, 1, 0);
		inner.add(new Label("BTW"), 0, 1);
		inner.add(this.btwPercentageTF, 1, 1);

		this.add(vbox, 0, 0);
		this.add(searchField, 1, 0);
		this.add(spacer, 0, 1);
		this.add(artNrOmschr, 0, 2, 2, 1);
		this.add(extraInfo, 0, 3, 2, 1);
		this.add(inner, 0, 4, 2, 1);
	}

	public Observable<EsselinkArtikel> getArtikel() {
		return this.searchBox.getSelectedItem();
	}

	public Observable<Double> getAantal() {
		return this.aantal;
	}

	public void setAantal(Double aantal) {
		this.aantalTF.setValue(BigDecimal.valueOf(aantal));
	}

	public Observable<Double> getBtwPercentage() {
		return this.btwPercentage;
	}

	public void setBtwPercentage(Double btwPercentage) {
		this.btwPercentageTF.setValue(BigDecimal.valueOf(btwPercentage));
	}

	public Observable<EsselinkArtikelToggle2> getSelectedToggle() {
		return Observables.fromProperty(this.searchType.selectedToggleProperty())
				.map(toggle -> toggle == this.artNr
						? EsselinkArtikelToggle2.ARTIKELNUMMER
						: EsselinkArtikelToggle2.OMSCHRIJVING);
	}
}
