package org.rekeningsysteem.ui.particulier;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.NumberField;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.EsselinkSearchBox;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GebruiktEsselinkArtikelPane extends GridPane {

	private final NumberField aantalTF = new NumberField();

	private Observable<EsselinkArtikel> selectedItem;
	private Observable<Double> aantal;

	private final ToggleGroup searchType = new ToggleGroup();
	private final RadioButton artNr = new RadioButton("Artikelnummer");
	private final RadioButton omschr = new RadioButton("Omschrijving");

	public GebruiktEsselinkArtikelPane(Currency currency) {
		try {
			Database database = Database.getInstance();
			ArtikellijstDBInteraction interaction = new ArtikellijstDBInteraction(database);
			AbstractSearchBox<EsselinkArtikel> searchField = new EsselinkSearchBox(currency);

			this.artNr.setToggleGroup(this.searchType);
			this.artNr.setSelected(false);

			this.omschr.setToggleGroup(this.searchType);
			this.omschr.setSelected(true);

			Observables.fromProperty(this.searchType.selectedToggleProperty())
					.subscribe(t -> searchField.clear());

			searchField.textProperty().filter(s -> s.length() < 4)
					.subscribe(s -> searchField.hideContextMenu());

			Observable<Toggle> toggles = Observables.fromProperty(this.searchType
					.selectedToggleProperty());
			searchField.textProperty().filter(s -> s.length() >= 4)
					.observeOn(Schedulers.io())
					.withLatestFrom(toggles, (s, toggle) -> {
						if (toggle == this.artNr) {
							return interaction.getWithArtikelnummer(s);
						}
						assert toggle == this.omschr;
						return interaction.getWithOmschrijving(s);
					})
					.observeOn(JavaFxScheduler.getInstance())
					.subscribe(searchField::populateMenu);

			this.selectedItem = searchField.getSelectedItem();

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
			this.selectedItem.subscribe(ea -> {
				artNrOmschr.getStyleClass().remove("no-item-found");
				artNrOmschr.setText(ea.getArtikelNummer() + "\t" + ea.getOmschrijving());
				extraInfo.setText(currency.getSymbol() + " " + ea.getVerkoopPrijs().getBedrag()
						+ " per " + ea.getPrijsPer() + " " + ea.getEenheid());
				
				searchField.clear();
			});

			this.aantal = Observables.fromProperty(this.aantalTF.valueProperty())
					.filter(Objects::nonNull)
					.map(BigDecimal::doubleValue);
			this.aantalTF.setPrefColumnCount(20);

			this.add(vbox, 0, 0);
			this.add(searchField, 1, 0);
			this.add(spacer, 0, 1);
			this.add(artNrOmschr, 0, 2, 2, 1);
			this.add(extraInfo, 0, 3, 2, 1);
			this.add(new Label("Aantal"), 0, 4);
			this.add(this.aantalTF, 1, 4);
		}
		catch (SQLException e) {
			ApplicationLogger.getInstance().fatal(
					"Database exception: probably the database was not found!", e);
		}
	}

	public Observable<EsselinkArtikel> getArtikel() {
		return this.selectedItem;
	}

	public Observable<Double> getAantal() {
		return this.aantal;
	}

	public void setAantal(Double aantal) {
		this.aantalTF.setValue(BigDecimal.valueOf(aantal));
	}
}
