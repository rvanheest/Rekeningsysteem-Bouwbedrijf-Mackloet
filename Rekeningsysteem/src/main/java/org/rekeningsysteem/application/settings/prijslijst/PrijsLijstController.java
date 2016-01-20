package org.rekeningsysteem.application.settings.prijslijst;

import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

import javafx.scene.control.ButtonBase;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.exception.GeldParseException;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;

import rx.Observable;
import rx.schedulers.Schedulers;

public class PrijsLijstController {

	private final PrijsLijstPane ui;

	public PrijsLijstController(Stage stage, ButtonBase closeButton, ArtikellijstDBInteraction db,
			Logger logger) {
		this(stage, closeButton, db, logger, new PrijsLijstPane());
	}

	public PrijsLijstController(Stage stage, ButtonBase closeButton, ArtikellijstDBInteraction db,
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
						.flatMap(i -> this.readFile(file))
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

	private Observable<EsselinkArtikel> readFile(File csv) {
		// TODO replace this implementation with the Apache Commons CSV parser
		// http://commons.apache.org/proper/commons-csv/
		// TODO move to separate class as this is an IO thing!
		try {
			String regex = ";(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

			FileReader reader = new FileReader(csv);
			return split(split(from(reader), System.lineSeparator()).map(s -> s + ";"), regex)
					.map(s -> s.startsWith("\"") && s.endsWith("\"")
							? s.substring(1, s.length() - 1)
							: s)
					.buffer(5)
					.skip(1)
					.flatMap(list -> {
						try {
							return Observable.just(new EsselinkArtikel(list.get(0), list.get(1),
									Integer.parseInt(list.get(2)), list.get(3),
									new Geld(list.get(4))));
						}
						catch (GeldParseException e) {
							return Observable.error(e);
						}
					});
		}
		catch (FileNotFoundException e) {
			return Observable.error(e);
		}
	}
}
