package org.rekeningsysteem.application.top;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.rekeningsysteem.rxjavafx.Observables;

public class WindowButtons extends VBox {

	private Stage stage;

	public WindowButtons(Stage stage) {
		super(4);
		this.stage = stage;

		Button closeBtn = new Button();
		closeBtn.setId("window-close");
		Observables.fromNodeEvents(closeBtn, ActionEvent.ACTION)
				.subscribe(event -> Platform.exit());

		Button minBtn = new Button();
		minBtn.setId("window-min");
		Observables.fromNodeEvents(minBtn, ActionEvent.ACTION)
				.subscribe(event -> this.stage.setIconified(true));

		Button maxBtn = new Button();
		maxBtn.setId("window-max");
		Observables.fromNodeEvents(maxBtn, ActionEvent.ACTION)
				.subscribe(event -> this.toggleMaximized());

		this.getChildren().addAll(closeBtn, minBtn, maxBtn);
//		this.toggleMaximized(); TODO uncomment this
	}

	public void toggleMaximized() {
		this.stage.setMaximized(!this.isMaximized());
	}

	public boolean isMaximized() {
		return this.stage.isMaximized();
	}
}
