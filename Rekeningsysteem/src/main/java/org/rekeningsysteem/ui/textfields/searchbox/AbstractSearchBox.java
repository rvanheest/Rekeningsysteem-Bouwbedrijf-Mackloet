package org.rekeningsysteem.ui.textfields.searchbox;

import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

public abstract class AbstractSearchBox<T> extends Region {

	private final TextField textBox = new TextField();
	private final Button clearButton = new Button();
	final ContextMenu contextMenu = new ContextMenu();

	private final Observable<String> textProperty = Observables.fromProperty(this.textBox.textProperty());

	public AbstractSearchBox(String defaultSearchBoxValue) {
		this.setId("searchBox");
		this.setMinHeight(24);
		this.setPrefSize(250, 24);
		this.setMaxHeight(24);

		this.textBox.setPromptText(defaultSearchBoxValue);
		this.textProperty()
				.map(String::isEmpty)
				.map(b -> !b)
				.subscribe(this.clearButton::setVisible);

		Observables.fromNodeEvents(this.clearButton, ActionEvent.ACTION)
				.doOnNext(e -> this.textBox.setText(""))
				.subscribe(e -> this.textBox.requestFocus());

		this.getChildren().addAll(this.textBox, this.clearButton);
	}

	@Override
	protected void layoutChildren() {
		this.textBox.resize(this.getWidth(), this.getHeight());
		this.clearButton.resizeRelocate(this.getWidth() - 18, 6, 12, 13);
	}

	public void populateMenu(Observable<T> ts) {
		this.contextMenu.getItems().clear();
		this.contextMenu.hide();

		ts.observeOn(JavaFxScheduler.getInstance())
				.doOnNext(this::populateMenu)
				.doOnCompleted(() -> this.contextMenu.show(this, Side.BOTTOM, 10, -5))
				.subscribe();
	}

	abstract void populateMenu(T t);

	public Observable<String> textProperty() {
		return this.textProperty;
	}

	public abstract Observable<T> getSelectedItem();

	public void setText(String value) {
		this.textBox.setText(value);
	}

	public void hideContextMenu() {
		this.contextMenu.hide();
	}

	public void clear() {
		this.textBox.clear();
	}
}
