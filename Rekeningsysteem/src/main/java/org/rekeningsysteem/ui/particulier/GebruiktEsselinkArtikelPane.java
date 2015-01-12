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
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.textfields.NumberField;
import org.rekeningsysteem.ui.textfields.SearchBox;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GebruiktEsselinkArtikelPane extends GridPane {

	private static final Func1<String, String> artNrQuery = s ->
			"SELECT * FROM Artikellijst WHERE artikelnummer LIKE '" + s + "%';";
	private static final Func1<String, String> omschrQuery = s ->
			"SELECT * FROM Artikellijst WHERE omschrijving LIKE '%" + s + "%';";

	private final NumberField aantalTF = new NumberField();

	private Observable<EsselinkArtikel> selectedItem;
	private Observable<Double> aantal;

	private final ToggleGroup searchType = new ToggleGroup();
	private final RadioButton artNr = new RadioButton("Artikelnummer");
	private final RadioButton omschr = new RadioButton("Omschrijving");

	public GebruiktEsselinkArtikelPane(Currency currency) {
		try {
			Database database = Database.getInstance();
			SearchBox searchField = new SearchBox(currency);

			this.artNr.setToggleGroup(this.searchType);
			this.artNr.setSelected(false);

			this.omschr.setToggleGroup(this.searchType);
			this.omschr.setSelected(true);

			Observables.fromProperty(this.searchType.selectedToggleProperty())
					.subscribe(t -> searchField.clear());

			searchField.textProperty().filter(s -> s.length() < 4)
					.subscribe(s -> searchField.hideContextMenu());

			searchField.textProperty().filter(s -> s.length() >= 4)
					.map(s -> s.replace("\'", "\'\'"))
					.map(this::getQuery)
					.observeOn(Schedulers.io())
					.<Observable<EsselinkArtikel>> map(s -> database.query(s, result -> {
						String artNr = result.getString("artikelnummer");
						String omschr = result.getString("omschrijving");
						int prijsPer = result.getInt("prijsPer");
						String eenheid = result.getString("eenheid");
						Geld verkoopPrijs = new Geld(result.getDouble("verkoopprijs"));

						return new EsselinkArtikel(artNr, omschr, prijsPer, eenheid, verkoopPrijs);
					}))
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
			artNrOmschr.getStyleClass().add("no-item-found");
			this.selectedItem.doOnNext(ea -> artNrOmschr.getStyleClass().remove("no-item-found"))
					.map(ea -> ea.getArtikelNummer() + "\t" + ea.getOmschrijving())
					.subscribe(artNrOmschr::setText);

			Label extraInfo = new Label();
			this.selectedItem.map(ea -> currency.getSymbol() + " "
					+ ea.getVerkoopPrijs().getBedrag() + " per "
					+ ea.getPrijsPer() + " " + ea.getEenheid())
					.subscribe(extraInfo::setText);

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

	private String getQuery(String s) {
		Toggle toggle = this.searchType.getSelectedToggle();
		if (toggle == this.artNr) {
			return artNrQuery.call(s);
		}
		else {
			assert toggle == this.omschr;
			return omschrQuery.call(s);
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
