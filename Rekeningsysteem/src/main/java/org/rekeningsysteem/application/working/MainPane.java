package org.rekeningsysteem.application.working;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Labeled;
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

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.settings.SettingsPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.mutaties.MutatiesController;
import org.rekeningsysteem.ui.offerte.OfferteController;
import org.rekeningsysteem.ui.particulier.ParticulierController;
import org.rekeningsysteem.ui.reparaties.ReparatiesController;

public class MainPane extends BorderPane implements Disposable {

	private final Database database;

	private final StackPane centerPane;
	private final RekeningTabpane tabpane;
	private SettingsPane settingsPane = null;

	private final Button mutaties = new Button();
	private final Button reparaties = new Button();
	private final Button particulier = new Button();
	private final Button offerte = new Button();
	private final Button open = new Button();
	private final Button save = new Button();
	private final Button pdf = new Button();
	private final ToggleButton settings = new ToggleButton();

	private final CompositeDisposable disposable = new CompositeDisposable();

	private final PropertiesWorker properties = PropertiesWorker.getInstance();
	private final Logger logger;

	public MainPane(Stage stage, Database database, Logger logger) {
		this.database = database;
		this.logger = logger;

		this.tabpane = new RekeningTabpane();
		this.centerPane = new StackPane(this.tabpane);

		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		List<Node> toolbarButtons = new ArrayList<>();
		addToolbarButton(PropertyModelEnum.FEATURE_MUTATIES, toolbarButtons, this.mutaties);
		addToolbarButton(PropertyModelEnum.FEATURE_REPARATIES, toolbarButtons, this.reparaties);
		addToolbarButton(PropertyModelEnum.FEATURE_PARTICULIER, toolbarButtons, this.particulier);
		addToolbarButton(PropertyModelEnum.FEATURE_OFFERTE, toolbarButtons, this.offerte);
		toolbarButtons.add(this.open);
		toolbarButtons.add(this.save);
		toolbarButtons.add(this.pdf);
		toolbarButtons.add(spacer);
		toolbarButtons.add(this.settings);

		this.setTop(new RekeningToolbar(toolbarButtons.toArray(new Node[0])));
		this.setCenter(this.centerPane);

		this.initButtonHandlers(stage);
	}

	private void addToolbarButton(PropertyModelEnum feature, List<Node> toolbarButtons, Button button) {
		this.properties.getBooleanProperty(feature)
			.filter(b -> b)
			.ifPresent(b -> toolbarButtons.add(button));
	}

	private void initButtons() {
		setGraphic(this.mutaties, "/images/mutaties.png");
		setGraphic(this.reparaties, "/images/reparaties.png");
		setGraphic(this.particulier, "/images/particulier.png");
		setGraphic(this.offerte, "/images/offerte.png");
		setGraphic(this.open, "/images/openen.png");
		setGraphic(this.save, "/images/opslaan.png");
		setGraphic(this.pdf, "/images/pdf.png");
		this.settings.setId("settings-button");
		setGraphic(this.settings, "/images/settings.png");
	}

	private static void setGraphic(Labeled node, String url) {
		node.setGraphic(
			new ImageView(
				new Image(Main.getResource(url), 20, 23, false, false)
			)
		);
	}

	private void initButtonHandlers(Stage stage) {
		this.disposable.addAll(
			this.initMutatiesObservable()
				.mergeWith(this.initReparatiesObservable())
				.mergeWith(this.initParticulierObservable())
				.mergeWith(this.initOfferteObservable(this.logger))
				.mergeWith(this.initOpenObservable(stage))
				.retry()
				.subscribe(tab -> {
					this.tabpane.addTab(tab);
					this.tabpane.selectTab(tab);
				}),

			this.initSaveObservable()
				.flatMapMaybe(tab -> saveFromTab(stage, tab, false))
				.filter(tab -> tab.getSavePath().isPresent())
				.subscribe(RekeningTab::save),

			this.initExportObservable()
				.flatMapMaybe(tab -> saveFromTab(stage, tab, true))
				.subscribe(tab -> this.showExportFileChooser(stage).ifPresent(file -> {
					this.saveLastSaveLocationProperty(file);
					try {
						tab.export(file);
					}
					catch (PdfException exception) {
						if (file.toString().contains("  ")) {
							// LaTeX doesn't like double spaces in the file name
							Alert alert = new Alert(
								Alert.AlertType.ERROR,
								"De PDF kon niet worden gegenereerd. De bestandsnaam bevat 2 opeenvolgende spaties. Pas dit aan en probeer opnieuw.",
								new ButtonType("Sluit", ButtonBar.ButtonData.CANCEL_CLOSE)
							);
							alert.setHeaderText("Fout bij PDF genereren");
							alert.show();
						}
						this.logger.error(exception.getMessage(), exception);
					}
				})),

			Observables.fromProperty(this.settings.selectedProperty())
				.subscribe(selected -> {
					if (selected && this.settingsPane == null) {
						this.settingsPane = new SettingsPane(stage, this.settings, this.database, this.logger);
						this.disposable.add(this.settingsPane);
						this.centerPane.getChildren().add(this.settingsPane);
					}
					else if (!selected && this.settingsPane != null) {
						this.centerPane.getChildren().remove(this.settingsPane);
						this.disposable.remove(this.settingsPane);
						this.settingsPane.dispose();
						this.settingsPane = null;
					}

					this.mutaties.setDisable(selected);
					this.reparaties.setDisable(selected);
					this.particulier.setDisable(selected);
					this.offerte.setDisable(selected);
					this.open.setDisable(selected);
				}),

			Observable.combineLatest(
					Observables.fromObservableList(this.tabpane.getTabs()).map(List::isEmpty),
					Observables.fromProperty(this.settings.selectedProperty()),
					(Boolean listEmpty, Boolean settingsSelected) -> listEmpty || settingsSelected
				)
				.subscribe(disable -> {
					this.save.setDisable(disable);
					this.pdf.setDisable(disable);
				})
		);
	}

	private Maybe<RekeningTab> saveFromTab(Stage stage, RekeningTab tab, boolean saveTab) {
		return tab.getModel()
			.firstElement()
			.doOnSuccess(rekening -> {
				if (tab.getSavePath().isEmpty()) {
					save(rekening instanceof Offerte, stage, tab);
					if (saveTab) tab.save();
				}
			})
			.map(rekening -> tab);
	}

	private void save(Boolean isOfferte, Stage stage, RekeningTab tab) {
		if (isOfferte) {
			this.showSaveFileChooserOfferte(stage).ifPresent(file -> {
				this.saveLastSaveLocationOfferteProperty(file);
				tab.setSavePath(file);
				tab.initFactuurnummer();
			});
		}
		else {
			this.showSaveFileChooser(stage).ifPresent(file -> {
				this.saveLastSaveLocationProperty(file);
				tab.setSavePath(file);
				tab.initFactuurnummer();
			});
		}
	}

	private void saveLastSaveLocationProperty(Path path) {
		this.properties.setProperty(PropertyModelEnum.LAST_SAVE_LOCATION, path.getParent());
	}

	private void saveLastSaveLocationOfferteProperty(Path path) {
		this.properties.setProperty(PropertyModelEnum.LAST_SAVE_LOCATION_OFFERTE, path.getParent());
	}

	private Maybe<Path> showOpenFileChooser(Stage stage) {
		Path initDir = this.properties.getPathProperty(PropertyModelEnum.LAST_SAVE_LOCATION)
				.filter(Files::exists)
				.orElseGet(() -> Paths.get(System.getProperty("user.dir")));

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open een factuur");
		chooser.setInitialDirectory(initDir.toFile());
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML, PDF", "*.xml", "*.pdf"));
		
		return Maybe.fromOptional(Optional.ofNullable(chooser.showOpenDialog(stage)).map(File::toPath));
	}

	private Optional<Path> showSaveFileChooser(PropertyKey key, Stage stage) {
		Path initDir = this.properties.getPathProperty(key)
				.filter(Files::exists)
				.orElseGet(() -> Paths.get(System.getProperty("user.dir")));

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Sla een factuur op");
		chooser.setInitialDirectory(initDir.toFile());
		chooser.getExtensionFilters().addAll(new ExtensionFilter("XML", "*.xml"));

		return Optional.ofNullable(chooser.showSaveDialog(stage)).map(File::toPath);
	}

	private Optional<Path> showSaveFileChooser(Stage stage) {
		return this.showSaveFileChooser(PropertyModelEnum.LAST_SAVE_LOCATION, stage);
	}

	private Optional<Path> showSaveFileChooserOfferte(Stage stage) {
		return this.showSaveFileChooser(PropertyModelEnum.LAST_SAVE_LOCATION_OFFERTE, stage);
	}

	private Optional<Path> showExportFileChooser(Stage stage) {
		Path initDir = this.properties.getPathProperty(PropertyModelEnum.LAST_SAVE_LOCATION)
			.filter(Files::exists)
			.orElseGet(() -> Paths.get(System.getProperty("user.dir")));

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Exporteer een factuur");
		chooser.setInitialDirectory(initDir.toFile());
		chooser.setInitialFileName(this.tabpane.getSelectedTab().getSavePath()
			.map(Path::getFileName)
			.map(Path::toString)
			.map(s -> s.substring(0, s.length() - 3) + "pdf")
			.orElse(""));

		chooser.getExtensionFilters().addAll(new ExtensionFilter("PDF", "*.pdf"));

		return Optional.ofNullable(chooser.showSaveDialog(stage)).map(File::toPath);
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
			.flatMapMaybe(event -> this.showOpenFileChooser(stage))
			.doOnNext(this::saveLastSaveLocationProperty)
			.flatMapMaybe(file -> RekeningTab.openFile(file, this.database));
	}

	private Observable<RekeningTab> initSaveObservable() {
		return Observables.fromNodeEvents(this.save, ActionEvent.ACTION)
			.map(event -> this.tabpane.getSelectedTab());
	}

	private Observable<RekeningTab> initExportObservable() {
		return Observables.fromNodeEvents(this.pdf, ActionEvent.ACTION)
			.map(event -> this.tabpane.getSelectedTab());
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
