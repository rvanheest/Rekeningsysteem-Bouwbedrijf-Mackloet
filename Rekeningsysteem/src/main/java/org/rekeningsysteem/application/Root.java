package org.rekeningsysteem.application;

import javafx.geometry.Bounds;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import org.rekeningsysteem.application.top.UpperBar;
import org.rekeningsysteem.application.working.MainPaneController;
import org.rekeningsysteem.rxjavafx.Observables;

import com.google.inject.Inject;

public class Root extends BorderPane {

	private WindowResizeButton resizeButton;

	@Inject
	public Root(Stage stage, UpperBar upperBar, MainPaneController mainPaneController, WindowResizeButton resize) {
		this.setId("root");

		this.setTop(upperBar);
		this.setCenter(mainPaneController.getUI());

		this.resizeButton = resize;
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
