package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.settings.SettingsPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.mutaties.MutatiesController;
import org.rekeningsysteem.ui.offerte.OfferteController;
import org.rekeningsysteem.ui.particulier.ParticulierController;
import org.rekeningsysteem.ui.reparaties.ReparatiesController;

import rx.Observable;
import rx.functions.Func0;

public class MainPane extends BorderPane {

	private final Database database;

	private final RekeningToolbar toolbar;
	private final StackPane centerPane;
	private final RekeningTabpane tabpane;
	private SettingsPane settingsPane = null;
	private final Func0<SettingsPane> settingsPaneFactory;

	private final Button mutaties = new Button();
	private final Button reparaties = new Button();
	private final Button particulier = new Button();
	private final Button offerte = new Button();
	private final Button open = new Button();
	private final Button save = new Button();
	private final Button pdf = new Button();
	private final ToggleButton settings = new ToggleButton();

	private final PropertiesWorker properties = PropertiesWorker.getInstance();

	public MainPane(Stage stage, Database database, Logger logger) {
		this.database = database;

		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		this.toolbar = new RekeningToolbar(this.mutaties, this.reparaties, this.particulier,
				this.offerte, this.open, this.save, this.pdf, spacer, this.settings);
		this.tabpane = new RekeningTabpane();
		this.settingsPaneFactory = () -> new SettingsPane(stage, this.settings, this.database,
				logger);
		this.centerPane = new StackPane(this.tabpane);

		this.setTop(this.toolbar);
		this.setCenter(this.centerPane);

		this.initButtonHandlers(stage, logger);
	}

	private void initButtons() {
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

	private void initButtonHandlers(Stage stage, Logger logger) {
		this.initMutatiesObservable()
				.mergeWith(this.initReparatiesObservable())
				.mergeWith(this.initParticulierObservable())
				.mergeWith(this.initOfferteObservable(logger))
				.mergeWith(this.initOpenObservable(stage))
				.retry()
				.subscribe(tab -> {
					this.tabpane.addTab(tab);
					this.tabpane.selectTab(tab);
				});

		this.initSaveObservable()
				.flatMap(tab -> tab.getModel()
							.first()
							.map(rekening -> rekening instanceof Offerte)
							.doOnNext(isOfferte -> {
								if (!tab.getSaveFile().isPresent()) {
									if (isOfferte) {
										this.showSaveFileChooserOfferte(stage).ifPresent(file -> {
											this.saveLastSaveLocationOfferteProperty(file);
											tab.setSaveFile(file);
											tab.initFactuurnummer();
										});
									}
									else {
										this.showSaveFileChooser(stage).ifPresent(file -> {
											this.saveLastSaveLocationProperty(file);
											tab.setSaveFile(file);
											tab.initFactuurnummer();
										});
									}
								}
							}), (tab, isOfferte) -> tab)
				.filter(tab -> tab.getSaveFile().isPresent())
				.subscribe(RekeningTab::save);

		this.initExportObservable()
				.flatMap(tab -> tab.getModel()
						.first()
						.map(rekening -> rekening instanceof Offerte)
						.doOnNext(isOfferte -> {
							if (!tab.getSaveFile().isPresent()) {
								if (isOfferte) {
									this.showSaveFileChooserOfferte(stage).ifPresent(file -> {
										this.saveLastSaveLocationOfferteProperty(file);
										tab.setSaveFile(file);
										tab.initFactuurnummer();
									});
								}
								else {
									this.showSaveFileChooser(stage).ifPresent(file -> {
										this.saveLastSaveLocationProperty(file);
										tab.setSaveFile(file);
										tab.initFactuurnummer();
									});
								}
								tab.save();
							}
						}), (tab, isOfferte) -> tab)
				.subscribe(tab -> this.showExportFileChooser(stage).ifPresent(file -> {
					this.saveLastSaveLocationProperty(file);
					tab.export(file);
				}));

		Observables.fromProperty(this.settings.selectedProperty())
				.subscribe(selected -> {
					if (selected) {
						assert this.settingsPane == null;
						this.settingsPane = this.settingsPaneFactory.call();
						this.centerPane.getChildren().add(this.settingsPane);
					}
					else {
						assert this.settingsPane != null;
						this.centerPane.getChildren().remove(this.settingsPane);
						this.settingsPane = null;
					}
					
					this.mutaties.setDisable(selected);
					this.reparaties.setDisable(selected);
					this.particulier.setDisable(selected);
					this.offerte.setDisable(selected);
					this.open.setDisable(selected);
				});

		Observable.combineLatest(Observables.fromObservableList(this.tabpane.getTabs())
				.map(List::isEmpty),
				Observables.fromProperty(this.settings.selectedProperty()),
				(Boolean listEmpty, Boolean settingsSelected) -> listEmpty || settingsSelected)
				.forEach(disable -> {
					this.save.setDisable(disable);
					this.pdf.setDisable(disable);
				});
	}

	private void saveLastSaveLocationProperty(File file) {
		this.properties.setProperty(PropertyModelEnum.LAST_SAVE_LOCATION,
				file.getParentFile().getPath());
	}

	private void saveLastSaveLocationOfferteProperty(File file) {
		this.properties.setProperty(PropertyModelEnum.LAST_SAVE_LOCATION_OFFERTE,
				file.getParentFile().getPath());
	}

	private Observable<File> showOpenFileChooser(Stage stage) {
		File initDir = new File(this.properties.getProperty(PropertyModelEnum.LAST_SAVE_LOCATION)
				.orElse(System.getProperty("user.dir")));

		if (!initDir.exists()) {
			initDir = new File(System.getProperty("user.dir"));
		}

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open een factuur");
		chooser.setInitialDirectory(initDir);
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML, PDF", "*.xml", "*.pdf"));

		return Observable.just(chooser.showOpenDialog(stage))
				.filter(Objects::nonNull);
	}

	private Optional<File> showSaveFileChooser(PropertyKey key, Stage stage) {
		File initDir = new File(this.properties.getProperty(key)
				.orElse(System.getProperty("user.dir")));

		if (!initDir.exists()) {
			initDir = new File(System.getProperty("user.dir"));
		}

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Sla een factuur op");
		chooser.setInitialDirectory(initDir);
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));

		return Optional.ofNullable(chooser.showSaveDialog(stage));
	}
	
	private Optional<File> showSaveFileChooser(Stage stage) {
		return this.showSaveFileChooser(PropertyModelEnum.LAST_SAVE_LOCATION, stage);
	}

	private Optional<File> showSaveFileChooserOfferte(Stage stage) {
		return this.showSaveFileChooser(PropertyModelEnum.LAST_SAVE_LOCATION_OFFERTE, stage);
	}

	private Optional<File> showExportFileChooser(Stage stage) {
		File initDir = new File(this.properties.getProperty(PropertyModelEnum.LAST_SAVE_LOCATION)
				.orElse(System.getProperty("user.dir")));

		if (!initDir.exists()) {
			initDir = new File(System.getProperty("user.dir"));
		}

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Exporteer een factuur");
		chooser.setInitialDirectory(initDir);
		chooser.setInitialFileName(this.tabpane.getSelectedTab().getSaveFile()
				.map(file -> file.getName())
				.map(s -> s.substring(0, s.length() - 3) + "pdf")
				.orElse(""));

		chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF", "*.pdf"));

		return Optional.ofNullable(chooser.showSaveDialog(stage));
	}

	private Observable<RekeningTab> initMutatiesObservable() {
		return Observables.fromNodeEvents(this.mutaties, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Mutaties factuur", new MutatiesController(this.database), this.database));
	}

	private Observable<RekeningTab> initReparatiesObservable() {
		return Observables.fromNodeEvents(this.reparaties, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Reparaties factuur", new ReparatiesController(this.database), this.database));
	}

	private Observable<RekeningTab> initParticulierObservable() {
		return Observables.fromNodeEvents(this.particulier, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Particulier factuur", new ParticulierController(this.database), this.database));
	}

	private Observable<RekeningTab> initOfferteObservable(Logger logger) {
		return Observables.fromNodeEvents(this.offerte, ActionEvent.ACTION)
				.map(event -> new RekeningTab("Offerte", new OfferteController(this.database, logger), this.database));
	}

	private Observable<RekeningTab> initOpenObservable(Stage stage) {
		return Observables.fromNodeEvents(this.open, ActionEvent.ACTION)
				.flatMap(event -> this.showOpenFileChooser(stage))
				.doOnNext(this::saveLastSaveLocationProperty)
				.flatMap(file -> RekeningTab.openFile(file, this.database));
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
