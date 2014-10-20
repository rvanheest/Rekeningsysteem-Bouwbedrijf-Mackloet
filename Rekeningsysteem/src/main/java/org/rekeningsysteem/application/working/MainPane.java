package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.scene.Node;
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

import rx.Observable;

import com.google.inject.Inject;

public class MainPane extends BorderPane {

	private final RekeningToolbar toolbar;
	private final RekeningTabpane tabpane = new RekeningTabpane();

	private final Button open = new Button();
	private final Button save = new Button();
	private final Button pdf = new Button();
	private final Button settings = new Button();

	@Inject
	public MainPane(Stage stage, List<WorkUnit> workModules) {
		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		List<Node> buttons = workModules.stream()
				.map(WorkUnit::getButton)
				.collect(Collectors.toList());
		buttons.addAll(Arrays.asList(this.open, this.save, this.pdf, spacer, this.settings));

		this.toolbar = new RekeningToolbar(buttons);

		workModules.stream().map(WorkUnit::getNewRekeningTab)
				.map(tab -> tab.doOnNext(this.tabpane::addTab))
				.map(tab -> tab.doOnNext(this.tabpane::selectTab))
				.forEach(Observable::subscribe);

		this.initSaveObservable().doOnNext(tab -> {
			if (!tab.getSaveFile().isPresent()) {
				tab.setSaveFile(this.showSaveFileChooser(stage));
			}
		}).subscribe(RekeningTab::save);

		this.initExportObservable()
				.subscribe(tab -> tab.export(this.showExportFileChooser(stage)));

		this.setTop(this.toolbar);
		this.setCenter(this.tabpane);

		Observable<Boolean> hasNoTabs = Observables.fromObservableList(this.tabpane.getTabs())
				.map(List::isEmpty);
		hasNoTabs.subscribe(this.save::setDisable);
		hasNoTabs.subscribe(this.pdf::setDisable);
	}

	private void initButtons() {
		this.settings.setId("settings-button");

		this.open.setGraphic(new ImageView(new Image(Main
				.getResource("/images/openen.png"))));
		this.save.setGraphic(new ImageView(new Image(Main
				.getResource("/images/opslaan.png"))));
		this.pdf.setGraphic(new ImageView(new Image(Main
				.getResource("/images/pdf.png"))));
		this.settings.setGraphic(new ImageView(new Image(Main
				.getResource("/images/settings.png"))));
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

	private Observable<RekeningTab> initSaveObservable() {
		return Observables.fromNodeEvents(this.save, ActionEvent.ACTION)
				.map(event -> this.tabpane.getSelectedTab());
	}

	private Observable<RekeningTab> initExportObservable() {
		return Observables.fromNodeEvents(this.pdf, ActionEvent.ACTION)
				.map(event -> this.tabpane.getSelectedTab());
	}
}
