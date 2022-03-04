package org.rekeningsysteem.application.settings.prijslijst;

import javafx.scene.control.ButtonBase;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.io.FileIO;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.io.File;
import java.util.Objects;

public class PrijsLijstController {

	private final PrijsLijstPane ui;

	public PrijsLijstController(Stage stage, ButtonBase closeButton, ArtikellijstDBInteraction db,
			Logger logger) {
		this(stage, closeButton, db, new FileIO(), logger, new PrijsLijstPane());
	}

	public PrijsLijstController(Stage stage, ButtonBase closeButton, ArtikellijstDBInteraction db, FileIO fileIO,
			Logger logger, PrijsLijstPane ui) {
		this.ui = ui;

		this.ui.getStartButtonEvents()
				.flatMap(e -> this.showOpenFileChooser(stage))
				.doOnNext(f -> {
					this.ui.setStartButtonDisable(true);
					closeButton.setDisable(true);
				})
				.observeOn(Schedulers.io())
				.flatMap(file -> db.clearData()
						.flatMap(i -> fileIO.readCSV(file))
						.window(100)
						.flatMap(db::insertAll)
						.scan(0, Math::addExact)
						.observeOn(JavaFxScheduler.getInstance())
						.doOnCompleted(() -> {
							closeButton.setDisable(false);
							this.ui.setStartButtonDisable(false);
							this.ui.setProgressText(this.ui.getProgressText()
									+ "\nArtikelen importeren is voltooid");
						}))
				.map(i -> "Voortgang: " + i + " items toegevoegd")
				.subscribe(this.ui::setProgressText, e -> {
					this.ui.setProgressText("Er is een fout opgetreden. Zie de log voor info.");
					logger.error("Error occurred in importing new data", e);
				});
	}

	public PrijsLijstPane getUI() {
		return this.ui;
	}

	private Observable<File> showOpenFileChooser(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open data file");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));

		return Observable.just(chooser.showOpenDialog(stage))
				.filter(Objects::nonNull);
	}
}
