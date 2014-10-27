package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.aangenomen.AangenomenController;

import rx.Observable;
import rx.Subscription;

import com.google.inject.Inject;

public class MainPane extends BorderPane {

	private RekeningToolbar toolbar;
	private RekeningTabpane tabpane;

	private Button aangenomen;
	private Button mutaties;
	private Button reparaties;
	private Button particulier;
	private Button offerte;
	private Button open;
	private Button save;
	private Button pdf;
	private Button settings;

	@Inject
	public MainPane(Stage stage) {
		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		this.toolbar = new RekeningToolbar(this.aangenomen, this.mutaties,
				this.reparaties, this.particulier, this.offerte, this.open,
				this.save, this.pdf, spacer, this.settings);
		this.tabpane = new RekeningTabpane();

		this.setTop(this.toolbar);
		this.setCenter(this.tabpane);

		this.initButtonHandlers(stage);

		Observable<Boolean> hasNoTabs = Observables.fromObservableList(this.tabpane.getTabs())
				.map(List::isEmpty);
		hasNoTabs.subscribe(this.save::setDisable);
		hasNoTabs.subscribe(this.pdf::setDisable);
	}

	private void initButtons() {
		this.aangenomen = new Button();
		this.aangenomen.setGraphic(new ImageView(new Image(Main
				.getResource("/images/aangenomen.png"))));

		this.mutaties = new Button();
		this.mutaties.setGraphic(new ImageView(new Image(Main
				.getResource("/images/mutaties.png"))));

		this.reparaties = new Button();
		this.reparaties.setGraphic(new ImageView(new Image(Main
				.getResource("/images/reparaties.png"))));

		this.particulier = new Button();
		this.particulier.setGraphic(new ImageView(new Image(Main
				.getResource("/images/particulier.png"))));

		this.offerte = new Button();
		this.offerte.setGraphic(new ImageView(new Image(Main
				.getResource("/images/offerte.png"))));

		this.open = new Button();
		this.open.setGraphic(new ImageView(new Image(Main
				.getResource("/images/openen.png"))));

		this.save = new Button();
		this.save.setGraphic(new ImageView(new Image(Main
				.getResource("/images/opslaan.png"))));

		this.pdf = new Button();
		this.pdf.setGraphic(new ImageView(new Image(Main
				.getResource("/images/pdf.png"))));

		this.settings = new Button();
		this.settings.setId("settings-button");
		this.settings.setGraphic(new ImageView(new Image(Main
				.getResource("/images/settings.png"))));
	}

	private void initButtonHandlers(Stage stage) {
		Function<Observable<RekeningTab>, Subscription> addSelect = (tab) -> tab
				.doOnNext(this.tabpane::addTab)
				.doOnNext(this.tabpane::selectTab)
				.subscribe();
		addSelect.apply(this.initAangenomenObservable());
		addSelect.apply(this.initMutatiesObservable());
		addSelect.apply(this.initReparatiesObservable());
		addSelect.apply(this.initParticulierObservable());
		addSelect.apply(this.initOfferteObservable());
		addSelect.apply(this.initOpenObservable(stage));

		this.initSaveObservable().doOnNext(tab -> {
			if (!tab.getSaveFile().isPresent()) {
				tab.setSaveFile(this.showSaveFileChooser(stage));
				tab.initFactuurnummer();
			}
		}).subscribe(RekeningTab::save);

		this.initExportObservable()
				.subscribe(tab -> tab.export(this.showExportFileChooser(stage)));
	}

	private File showOpenFileChooser(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open een factuur");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));

		return chooser.showOpenDialog(stage);
	}

	private File showSaveFileChooser(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Sla een factuur op");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));

		return chooser.showSaveDialog(stage);
	}

	private File showExportFileChooser(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Exporteer een factuur");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.setInitialFileName(this.tabpane.getSelectedTab().getSaveFile()
				.map(file -> file.getName())
				.map(s -> s.substring(0, s.length() - 3) + "pdf")
				.orElse(""));
		
		chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF", "*.pdf"));

		return chooser.showSaveDialog(stage);
	}

	private Observable<RekeningTab> initAangenomenObservable() {
		return Observables.fromNodeEvents(this.aangenomen, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Aangenomen factuur", new AangenomenController()));
	}

	private Observable<RekeningTab> initMutatiesObservable() {
		return Observables.fromNodeEvents(this.mutaties, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Mutaties factuur"));
	}

	private Observable<RekeningTab> initReparatiesObservable() {
		return Observables.fromNodeEvents(this.reparaties, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Reparaties factuur"));
	}

	private Observable<RekeningTab> initParticulierObservable() {
		return Observables.fromNodeEvents(this.particulier, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Particulier factuur"));
	}

	private Observable<RekeningTab> initOfferteObservable() {
		return Observables.fromNodeEvents(this.offerte, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Offerte"));
	}

	private Observable<RekeningTab> initOpenObservable(Stage stage) {
		return Observables.fromNodeEvents(this.open, ActionEvent.ACTION)
				.map(event -> this.showOpenFileChooser(stage))
				.map(RekeningTab::openFile);
	}

	private Observable<RekeningTab> initSaveObservable() {
		return Observables.fromNodeEvents(this.save, ActionEvent.ACTION)
				.map(event -> this.tabpane.getSelectedTab());
	}

	private Observable<RekeningTab> initExportObservable() {
		return Observables.fromNodeEvents(this.pdf, ActionEvent.ACTION)
				.map(event -> this.tabpane.getSelectedTab());
	}
}
