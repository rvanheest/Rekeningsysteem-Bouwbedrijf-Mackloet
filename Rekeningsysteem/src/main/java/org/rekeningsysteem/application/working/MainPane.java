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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.rekeningsysteem.application.Main;

import com.google.inject.Inject;

public class MainPane extends BorderPane {

	private RekeningToolbar toolbar;

	private Button open;
	private Button save;
	private Button pdf;
	private Button settings;

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

		this.setTop(this.toolbar);
		this.setCenter(new StackPane());
	}

	private void initButtons() {
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
}
