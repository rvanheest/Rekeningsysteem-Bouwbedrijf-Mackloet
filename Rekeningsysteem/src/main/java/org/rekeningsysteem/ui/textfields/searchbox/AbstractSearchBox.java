package org.rekeningsysteem.ui.textfields.searchbox;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.geometry.Side;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;

import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;

public abstract class AbstractSearchBox<T> extends Region {

	private final TextField textBox = new TextField();
	private final Button clearButton = new Button();
	final ContextMenu contextMenu = new ContextMenu();

	private final Observable<String> textProperty = Observables.fromProperty(this.textBox.textProperty());
	final PublishSubject<T> selectedItem = PublishSubject.create();

	// info popup
	final Popup infoPopup = new Popup();
	final VBox infoBox = new VBox();

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

		this.infoPopup.getContent().add(this.infoBox);
		Observables.fromNodeEvents(this.contextMenu, WindowEvent.WINDOW_HIDDEN)
			.subscribe(e -> this.infoPopup.hide());
	}

	@Override
	protected void layoutChildren() {
		this.textBox.resize(this.getWidth(), this.getHeight());
		this.clearButton.resizeRelocate(this.getWidth() - 18, 6, 12, 13);
	}

	public void populateMenu(Observable<T> ts) {
		this.contextMenu.getItems().clear();
		this.contextMenu.hide();

		ts.toFlowable(BackpressureStrategy.BUFFER)
			.observeOn(JavaFxScheduler.getInstance())
			.doOnNext(this::populateMenu)
			.doOnComplete(() -> this.contextMenu.show(this, Side.BOTTOM, 10, -5))
			.subscribe();
	}

	private void populateMenu(T t) {
		Region popRegion = new Region();
		popRegion.getStyleClass().add("search-menu-item-popup-region");
		popRegion.setPrefSize(10, 10);

		HBox hBox = this.getHBox(t);
		hBox.getChildren().add(popRegion);
		hBox.setFillHeight(true);

		Observables.fromProperty(popRegion.opacityProperty())
			.skip(1)
			.map(Number::doubleValue)
			.filter(d -> d == 1)
			.subscribeOn(JavaFxScheduler.getInstance()) // used here as a workaround for RT-14396
			.doOnNext(d -> this.setTextfields(t))
			.map(d -> hBox.localToScene(0, 0))
			.map(hBoxPos -> new Point2D(
				hBoxPos.getX() + this.contextMenu.getScene().getX() + this.contextMenu.getX() - this.infoBox.getPrefWidth() - 10,
				hBoxPos.getY() + this.contextMenu.getScene().getY() + this.contextMenu.getY() - 10
			))
			.subscribe(d -> this.infoPopup.show(this.getScene().getWindow(), d.getX(), d.getY()));

		MenuItem menu = this.createMenuItem(hBox);
		menu.getStyleClass().add("search-menu-item");
		this.contextMenu.getItems().add(menu);
		Observables.fromNodeEvents(menu, ActionEvent.ACTION)
			.map(event -> t)
			.subscribe(this.selectedItem);
	}

	abstract void setTextfields(T t);

	abstract HBox getHBox(T t);

	protected MenuItem createMenuItem(HBox hBox) {
		// can be overriden for extra styling, as is done in EsselinkSearchBox
		return new CustomMenuItem(hBox);
	}

	public Observable<String> textProperty() {
		return this.textProperty;
	}

	public Observable<T> getSelectedItem() {
		return this.selectedItem;
	}

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
