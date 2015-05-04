package org.rekeningsysteem.ui.textfields;

import java.util.Currency;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.subjects.PublishSubject;

public class SearchBox extends Region {

	private final TextField textBox = new TextField();
	private final Button clearButton = new Button();
	private final ContextMenu contextMenu = new ContextMenu();

	private final PublishSubject<EsselinkArtikel> selectedItem = PublishSubject.create();
	
	// info popup
	private final Popup infoPopup = new Popup();
	private final VBox infoBox = new VBox();
	private final Label infoOmschrijving = new Label();
	private final Label infoArtikelNummer = new Label();
	private final Label infoPrijsEenheid = new Label();
	
	private final Currency currency;

	public SearchBox(Currency currency) {
		this.currency = currency;
		
		this.setId("searchBox");
		this.setMinHeight(24);
		this.setPrefSize(250, 24);
		this.setMaxHeight(24);
		
		this.textBox.setPromptText("Zoek...");
		this.textProperty().subscribe(s -> this.clearButton.setVisible(!s.isEmpty()));
		
		this.clearButton.setVisible(false);
		Observables.fromNodeEvents(this.clearButton, ActionEvent.ACTION)
				.doOnNext(e -> this.textBox.setText(""))
				.subscribe(e -> this.textBox.requestFocus());
		
		this.getChildren().addAll(this.textBox, this.clearButton);
		
		this.infoBox.setId("search-info-box");
		this.infoBox.setFillWidth(true);
		this.infoBox.setMinWidth(Region.USE_PREF_SIZE);
		this.infoBox.setPrefWidth(200);
		this.infoBox.getChildren().addAll(this.infoOmschrijving,
				this.infoArtikelNummer, this.infoPrijsEenheid);
		
		this.infoOmschrijving.setId("search-info-name");
		this.infoOmschrijving.setMinHeight(Region.USE_PREF_SIZE);
		this.infoOmschrijving.setPrefHeight(28);
		
		this.infoArtikelNummer.setId("search-info-description");
		this.infoArtikelNummer.setWrapText(true);
		this.infoArtikelNummer.setPrefWidth(this.infoBox.getPrefWidth() - 24);

		this.infoPrijsEenheid.setId("search-info-description");
		this.infoPrijsEenheid.setWrapText(true);
		this.infoPrijsEenheid.setPrefWidth(this.infoBox.getPrefWidth() - 24);
		
		this.infoPopup.getContent().add(this.infoBox);
		Observables.fromNodeEvents(this.contextMenu, WindowEvent.WINDOW_HIDDEN)
				.subscribe(e -> this.infoPopup.hide());
	}
	
	@Override
	protected void layoutChildren() {
        this.textBox.resize(getWidth(),getHeight());
        this.clearButton.resizeRelocate(getWidth()-18,6,12,13);
    }
	
	public void populateMenu(Observable<EsselinkArtikel> eas) {
		this.contextMenu.getItems().clear();
		this.contextMenu.hide();
		
		eas.observeOn(JavaFxScheduler.getInstance())
				.doOnNext(this::populateMenu)
				.doOnCompleted(() -> this.contextMenu.show(this, Side.BOTTOM, 10, -5))
				.subscribe();
	}

	private void populateMenu(EsselinkArtikel ea) {
		Label artNrLabel = new Label(ea.getArtikelNummer());
		artNrLabel.getStyleClass().add("artikelnummer-label");
        artNrLabel.setAlignment(Pos.CENTER_RIGHT);
        artNrLabel.setMinWidth(USE_PREF_SIZE);
        artNrLabel.setPrefWidth(70);
        
		Label omschrLabel = new Label(ea.getOmschrijving());
		
		Region popRegion = new Region();
		popRegion.getStyleClass().add("search-menu-item-popup-region");
		popRegion.setPrefSize(10, 10);
		
		HBox hBox = new HBox(artNrLabel, omschrLabel, popRegion);
		hBox.setFillHeight(true);
		
		String omschr = ea.getOmschrijving();
		String artNr = "Artikelnummer: " + ea.getArtikelNummer();
		String prijsEenheid = this.currency.getSymbol() + " "
				+ ea.getVerkoopPrijs().getBedrag() + " per "
				+ ea.getPrijsPer() + " " + ea.getEenheid();
		
		Observables.fromProperty(popRegion.opacityProperty())
				.map(Number::doubleValue)
				.filter(d -> d == 1)
				.subscribeOn(JavaFxScheduler.getInstance()) // used here as a workaround for RT-14396
				.doOnNext(d -> this.infoOmschrijving.setText(omschr))
				.doOnNext(d -> this.infoArtikelNummer.setText(artNr))
				.doOnNext(d -> this.infoPrijsEenheid.setText(prijsEenheid))
				.map(d -> hBox.localToScene(0, 0))
				.map(hBoxPos -> new Point2D(
						hBoxPos.getX() + this.contextMenu.getScene().getX() + this.contextMenu.getX() - this.infoBox.getPrefWidth() - 10,
                        hBoxPos.getY() + this.contextMenu.getScene().getY() + this.contextMenu.getY() - 10))
				.forEach(d -> this.infoPopup.show(this.getScene().getWindow(), d.getX(), d.getY()));
		
		CustomMenuItem menu = new CustomMenuItem(hBox);
		menu.getStyleClass().add("search-menu-item");
		this.contextMenu.getItems().add(menu);
		Observables.fromNodeEvents(menu, ActionEvent.ACTION)
				.map(event -> ea)
				.subscribe(this.selectedItem);
	}

	public Observable<EsselinkArtikel> getSelectedItem() {
		return this.selectedItem.asObservable();
	}

	public Observable<String> textProperty() {
		return Observables.fromProperty(this.textBox.textProperty());
	}

	public void hideContextMenu() {
		this.contextMenu.hide();
	}

	public void clear() {
		this.textBox.clear();
	}
}
