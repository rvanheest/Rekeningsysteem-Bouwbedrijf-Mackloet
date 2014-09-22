package org.rekeningsysteem.application;

import javafx.geometry.Bounds;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.rekeningsysteem.application.top.UpperBar;
import org.rekeningsysteem.rxjavafx.Observables;

public class Root extends BorderPane {

	private WindowResizeButton resizeButton;

	public Root(Stage stage) {
		this.setId("root");

		this.setTop(new UpperBar(stage));
		this.setCenter(new SplitPane());

		this.resizeButton = new WindowResizeButton(stage, 1061, 728);
		this.resizeButton.setManaged(false);

		Observables.fromProperty(stage.maximizedProperty())
				.map(b -> b ? this.getChildren().remove(this.resizeButton)
						: this.getChildren().add(this.resizeButton))
				.subscribe();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		this.resizeButton.autosize();

		Bounds layoutBounds = this.resizeButton.getLayoutBounds();
		this.resizeButton.setLayoutX(this.getWidth() - layoutBounds.getWidth());
		this.resizeButton.setLayoutY(this.getHeight() - layoutBounds.getHeight());
	}
}
