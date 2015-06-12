package org.rekeningsysteem.ui.textfields.searchbox;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.subjects.PublishSubject;

public class DebiteurSearchBox extends AbstractSearchBox<Debiteur> {
	
	private final PublishSubject<Debiteur> selectedItem = PublishSubject.create();
	
	// info popup
	private final Popup infoPopup = new Popup();
	private final VBox infoBox = new VBox();
	private final Label infoNaam = new Label();
	private final Label infoAdres = new Label();
	private final Label infoPostcodePlaats = new Label();
	private final Label infoBtwNummer = new Label();

	public DebiteurSearchBox() {
		super("Zoek debiteur...");
		
		this.infoBox.setId("search-info-box");
		this.infoBox.setFillWidth(true);
		this.infoBox.setMinWidth(Region.USE_PREF_SIZE);
		this.infoBox.setPrefWidth(200);
		this.infoBox.getChildren().addAll(this.infoNaam, this.infoAdres, this.infoPostcodePlaats);
		
		this.infoNaam.setId("search-info-name");
		this.infoNaam.setMinHeight(Region.USE_PREF_SIZE);
		this.infoNaam.setPrefHeight(28);
		
		this.infoAdres.setId("search-info-description");
		this.infoAdres.setWrapText(true);
		this.infoAdres.setPrefWidth(this.infoBox.getPrefWidth() - 24);
		
		this.infoPostcodePlaats.setId("search-info-description");
		this.infoPostcodePlaats.setWrapText(true);
		this.infoPostcodePlaats.setPrefWidth(this.infoBox.getPrefWidth() - 24);
		
		this.infoBtwNummer.setId("search-info-description");
		this.infoBtwNummer.setWrapText(true);
		this.infoBtwNummer.setPrefWidth(this.infoBox.getPrefWidth() - 24);
		
		this.infoPopup.getContent().add(this.infoBox);
		Observables.fromNodeEvents(this.contextMenu, WindowEvent.WINDOW_HIDDEN)
				.subscribe(e -> this.infoPopup.hide());
	}
	
	@Override
	void populateMenu(Debiteur debiteur) {
		Label naamLabel = new Label(debiteur.getNaam());
		
		Region popRegion = new Region();
		popRegion.getStyleClass().add("search-menu-item-popup-region");
		popRegion.setPrefSize(10, 10);
		
		HBox hBox = new HBox(naamLabel, popRegion);
		hBox.setFillHeight(true);
		
		String naam = debiteur.getNaam();
		String adres = debiteur.getStraat() + " " + debiteur.getNummer();
		String postcodePlaats = debiteur.getPostcode() + "  " + debiteur.getPlaats().toUpperCase();
		Optional<String> btwNummer = debiteur.getBtwNummer().map(s -> "BTW nummer: " + s);
		
		Observables.fromProperty(popRegion.opacityProperty())
				.map(Number::doubleValue)
				.filter(d -> d == 1)
				.subscribeOn(JavaFxScheduler.getInstance()) // used here as a workaround for RT-14396
				.doOnNext(d -> {
					this.infoNaam.setText(naam);
					this.infoAdres.setText(adres);
					this.infoPostcodePlaats.setText(postcodePlaats);
					this.infoBtwNummer.setText(btwNummer.orElse(""));
					
					if (btwNummer.isPresent()) {
						this.infoBox.getChildren().add(this.infoBtwNummer);
					}
					else {
						this.infoBox.getChildren().remove(this.infoBtwNummer);
					}
				})
				.map(d -> hBox.localToScene(0, 0))
				.map(hBoxPos -> new Point2D(
						hBoxPos.getX() + this.contextMenu.getScene().getX() + this.contextMenu.getX() - this.infoBox.getPrefWidth() - 10,
                        hBoxPos.getY() + this.contextMenu.getScene().getY() + this.contextMenu.getY() - 10))
				.forEach(d -> this.infoPopup.show(this.getScene().getWindow(), d.getX(), d.getY()));
		
		CustomMenuItem menu = new CustomMenuItem(hBox);
		menu.getStyleClass().add("search-menu-item");
		this.contextMenu.getItems().add(menu);
		Observables.fromNodeEvents(menu, ActionEvent.ACTION)
				.map(event -> debiteur)
				.subscribe(this.selectedItem);
	}

	@Override
	public Observable<Debiteur> getSelectedItem() {
		return this.selectedItem.asObservable();
	}
}
