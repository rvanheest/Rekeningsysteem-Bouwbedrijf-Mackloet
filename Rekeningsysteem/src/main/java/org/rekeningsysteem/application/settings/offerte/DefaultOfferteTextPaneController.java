package org.rekeningsysteem.application.settings.offerte;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;

public class DefaultOfferteTextPaneController implements Disposable {

	private final DefaultOfferteTextPane ui;
	private final DefaultOfferteTextHandler handler = new DefaultOfferteTextHandler();

	private final CompositeDisposable disposable = new CompositeDisposable();

	public DefaultOfferteTextPaneController(Logger logger) {
		this.ui = new DefaultOfferteTextPane();

		this.disposable.addAll(
			this.handler.getDefaultText().subscribe(this.ui::setText),

			this.handler.setDefaultText(this.ui.getText().sample(this.ui.getSaveButtonEvent()))
				.observeOn(JavaFxScheduler.getInstance())
				.doOnError(e -> {
					String alertText = "De tekst kon niet worden opgeslagen. Zie de log voor meer info.";
					ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
					Alert alert = new Alert(AlertType.NONE, alertText, close);
					alert.setHeaderText("Fout bij opslaan");
					alert.show();
					logger.error(e.getMessage(), e);
				})
				.subscribe(),

			this.ui.getCancelButtonEvent()
				.flatMapMaybe(e -> this.handler.getDefaultText())
				.subscribe(this.ui::setText)
		);
	}

	public DefaultOfferteTextPane getUI() {
		return this.ui;
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
