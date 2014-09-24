package org.rekeningsysteem.application.working;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public abstract class AbstractWorkModule {

	private final Button button = new Button();

	protected AbstractWorkModule(ImageView buttonImage) {
		this.button.setGraphic(buttonImage);
	}

	public Button getButton() {
		return this.button;
	}
}
