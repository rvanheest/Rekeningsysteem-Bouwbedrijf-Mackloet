package org.rekeningsysteem.ui.offerte;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.WorkingPane;

public class TextPaneController implements Disposable {

	private final TextPane textPane;
	private final WorkingPane ui;
	private final Observable<String> model;
	private final CompositeDisposable disposables = new CompositeDisposable();

	private TextPaneController(TextPane textPane) {

		this.textPane = textPane;
		this.model = this.textPane.getText();
		this.ui = new WorkingPane("Offerte tekst", textPane);
	}

	public TextPaneController(Logger logger) {
		this(new TextPane());
		this.initDefaultText(logger);
	}

	public TextPaneController(String input) {
		this(new TextPane());
		this.textPane.setText(input);
	}

	private void initDefaultText(Logger logger) {
		this.disposables.addAll(
			new DefaultOfferteTextHandler()
				.getDefaultText()
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(
					this.textPane::setText,
					e -> {
						String alertText = "De standaard tekst voor de offerte kon niet worden geladen. Zie de error log voor meer info.";
						ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
						Alert alert = new Alert(AlertType.ERROR, alertText, close);
						alert.setHeaderText("Fout bij lezen");
						alert.show();
						logger.error("error in reading default offerte text file", e);
					}
				)
		);
	}

	public Observable<String> getModel() {
		return this.model;
	}

	public WorkingPane getUI() {
		return this.ui;
	}

	@Override
	public boolean isDisposed() {
		return this.disposables.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposables.dispose();
	}
}
