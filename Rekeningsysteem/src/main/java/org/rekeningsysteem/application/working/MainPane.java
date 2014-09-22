package org.rekeningsysteem.application.working;

import java.util.Collection;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

import com.google.inject.Inject;

public class MainPane extends BorderPane {

	private final RekeningToolbar toolbar;
	private final RekeningTabPane tabpane;

	private final Button aangenomen = new Button();
	private final Button mutaties = new Button();
	private final Button reparaties = new Button();
	private final Button particulier = new Button();
	private final Button offerte = new Button();
	private final Button open = new Button();
	private final Button save = new Button();
	private final Button pdf = new Button();
	private final Button settings = new Button();
	
	private final Observable<ActionEvent> aangenomenEvents = Observables.fromNodeEvents(this.aangenomen, ActionEvent.ACTION);
	private final Observable<ActionEvent> mutatiesEvents = Observables.fromNodeEvents(this.mutaties, ActionEvent.ACTION);
	private final Observable<ActionEvent> reparatiesEvents = Observables.fromNodeEvents(this.reparaties, ActionEvent.ACTION);
	private final Observable<ActionEvent> particulierEvents = Observables.fromNodeEvents(this.particulier, ActionEvent.ACTION);
	private final Observable<ActionEvent> offerteEvents = Observables.fromNodeEvents(this.offerte, ActionEvent.ACTION);
	private final Observable<ActionEvent> openEvents = Observables.fromNodeEvents(this.open, ActionEvent.ACTION);
	private final Observable<ActionEvent> saveEvents = Observables.fromNodeEvents(this.save, ActionEvent.ACTION);
	private final Observable<ActionEvent> pdfEvents = Observables.fromNodeEvents(this.pdf, ActionEvent.ACTION);
	private final Observable<ActionEvent> settingsEvents = Observables.fromNodeEvents(this.settings, ActionEvent.ACTION);

	@Inject
	public MainPane(Stage stage, RekeningTabPane tabPane) {
		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		this.toolbar = new RekeningToolbar(this.aangenomen, this.mutaties,
				this.reparaties, this.particulier, this.offerte, this.open,
				this.save, this.pdf, spacer, this.settings);
		this.tabpane = tabPane;

		this.setTop(this.toolbar);
		this.setCenter(this.tabpane);
		
		Observable<Boolean> hasNoTabs = Observables.fromObservableList(this.tabpane.getTabs())
				.map(Collection::isEmpty);
		hasNoTabs.subscribe(this.save::setDisable);
		hasNoTabs.subscribe(this.pdf::setDisable);
	}

	private void initButtons() {
		this.aangenomen.setGraphic(new ImageView(new Image(Main
				.getResource("/images/aangenomen.png"))));

		this.mutaties.setGraphic(new ImageView(new Image(Main
				.getResource("/images/mutaties.png"))));

		this.reparaties.setGraphic(new ImageView(new Image(Main
				.getResource("/images/reparaties.png"))));

		this.particulier.setGraphic(new ImageView(new Image(Main
				.getResource("/images/particulier.png"))));

		this.offerte.setGraphic(new ImageView(new Image(Main
				.getResource("/images/offerte.png"))));

		this.open.setGraphic(new ImageView(new Image(Main
				.getResource("/images/openen.png"))));

		this.save.setGraphic(new ImageView(new Image(Main
				.getResource("/images/opslaan.png"))));

		this.pdf.setGraphic(new ImageView(new Image(Main
				.getResource("/images/pdf.png"))));

		this.settings.setId("settings-button");
		this.settings.setGraphic(new ImageView(new Image(Main
				.getResource("/images/settings.png"))));
	}
	
	public RekeningTabPane getTabPane() {
		return this.tabpane;
	}

	public Observable<ActionEvent> getAangenomenEvents() {
		return this.aangenomenEvents;
	}
	
	public Observable<ActionEvent> getMutatiesEvents() {
		return this.mutatiesEvents;
	}
	
	public Observable<ActionEvent> getReparatiesEvents() {
		return this.reparatiesEvents;
	}
	
	public Observable<ActionEvent> getParticulierEvents() {
		return this.particulierEvents;
	}
	
	public Observable<ActionEvent> getOfferteEvents() {
		return this.offerteEvents;
	}
	
	public Observable<ActionEvent> getOpenEvents() {
		return this.openEvents;
	}
	
	public Observable<ActionEvent> getSaveEvents() {
		return this.saveEvents;
	}
	
	public Observable<ActionEvent> getPdfEvents() {
		return this.pdfEvents;
	}
	
	public Observable<ActionEvent> getSettingsEvents() {
		return this.settingsEvents;
	}
}
