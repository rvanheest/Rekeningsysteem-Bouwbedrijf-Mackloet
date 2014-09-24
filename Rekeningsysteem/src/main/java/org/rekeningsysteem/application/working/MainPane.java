package org.rekeningsysteem.application.working;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
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
	private final RekeningTabpane tabpane = new RekeningTabpane();

	private final Button open = new Button();
	private final Button save = new Button();
	private final Button pdf = new Button();
	private final Button settings = new Button();

	@Inject
	public MainPane(Stage stage, List<AbstractWorkModule> workModules) {
		this.setId("main-pane");
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		this.initButtons();

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);

		List<Node> buttons = workModules.stream()
				.map(AbstractWorkModule::getButton)
				.collect(Collectors.toList());
		buttons.addAll(Arrays.asList(this.open, this.save, this.pdf, spacer, this.settings));

		this.toolbar = new RekeningToolbar(buttons);
		
		workModules.stream().map(AbstractWorkModule::getButtonEvent)
				.map(tab -> tab.doOnNext(this.tabpane::addTab))
				.map(tab -> tab.doOnNext(this.tabpane::selectTab))
				.forEach(Observable::subscribe);

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
}
