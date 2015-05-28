package org.rekeningsysteem.application.settings;

import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.split;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.io.database.QueryEnumeration;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.schedulers.Schedulers;

public class PrijslijstIO extends Tab {

	private final Label progressLabel = new Label("nog niet gestart");
	private final Label warningLabel = new Label("Let op, de huidige data wordt verwijderd "
			+ "wanneer nieuwe data wordt geimporteerd!");
	private final Button startButton = new Button("Start");

	public PrijslijstIO(Stage stage, Button closeButton) {
		super("Esselink artikel data");

		this.progressLabel.getStyleClass().add("no-item-found");
		this.warningLabel.setTextFill(Color.RED);
		this.warningLabel.setWrapText(true);

		Observables.fromNodeEvents(this.startButton, ActionEvent.ACTION)
				.flatMap(e -> this.showOpenFileChooser(stage))
				.doOnNext(f -> {
					this.startButton.setDisable(true);
					closeButton.setDisable(true);
				})
				.observeOn(Schedulers.io())
				.flatMap(file -> this.clearData()
						.flatMap(i -> this.readFile(file))
						.flatMap(query -> {
							try {
								return Database.getInstance().update(query);
							}
							catch (SQLException e) {
								return Observable.error(e);
							}
						})
						.scan(0, (cum, x) -> cum + 1)
						.observeOn(JavaFxScheduler.getInstance())
						.doOnCompleted(() -> {
							closeButton.setDisable(false);
							this.startButton.setDisable(false);
							this.progressLabel.setText("Artikelen importeren is voltooid");
						}))
				.map(i -> "Voortgang: " + i + " items toegevoegd")
				.subscribe(this.progressLabel::setText, e -> {
					this.progressLabel.setText("Er is een fout opgetreden. Zie de log "
							+ "voor info.");
					ApplicationLogger.getInstance().error("Error occurred in importing "
							+ "new data", e);
				});

		GridPane content = new GridPane();
		content.setPadding(new Insets(8));
		content.setHgap(10);
		content.setVgap(5);

		content.add(this.warningLabel, 0, 0, 2, 1);
		content.add(this.startButton, 0, 1);
		content.add(this.progressLabel, 1, 1);

		this.setContent(content);
	}

	private Observable<File> showOpenFileChooser(Stage stage) {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open data file");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().addAll(new ExtensionFilter("CSV", "*.csv"));

		return Observable.just(chooser.showOpenDialog(stage))
				.filter(Objects::nonNull);
	}

	private Observable<Integer> clearData() {
		try {
			return Database.getInstance().update(PrijslijstQueries.CLEAR_ARTIKELLIJST);
		}
		catch (SQLException e) {
			return Observable.error(e);
		}
	}

	private Observable<QueryEnumeration> readFile(File csv) {
		// TODO replace this implementation with the Apache Commons CSV parser
		// http://commons.apache.org/proper/commons-csv/
		try {
			String regex = ";(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

			FileReader reader = new FileReader(csv);
			return split(split(from(reader), System.lineSeparator()).map(s -> s + ";"), regex)
					.map(s -> s.startsWith("\"") && s.endsWith("\"")
							? s.substring(1, s.length() - 1)
							: s)
					.buffer(5)
					.skip(1)
					.map(list -> () -> "INSERT INTO Artikellijst VALUES ('" + list.get(0) + "', '"
							+ list.get(1).replace("\'", "\'\'") + "', '" + list.get(2) + "', '"
							+ list.get(3) + "', '" + list.get(4).replace(',', '.') + "');");
		}
		catch (FileNotFoundException e) {
			return Observable.error(e);
		}
	}

	private enum PrijslijstQueries implements QueryEnumeration {

		CLEAR_ARTIKELLIJST("DELETE FROM Artikellijst");

		private final String query;

		PrijslijstQueries(String query) {
			this.query = query;
		}

		@Override
		public String getQuery() {
			return this.query;
		}
	}
}
