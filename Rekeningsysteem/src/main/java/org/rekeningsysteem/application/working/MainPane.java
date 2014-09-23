package org.rekeningsysteem.application.working;

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

		this.setTop(this.toolbar);
		this.setCenter(new StackPane());
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
}
