package org.rekeningsysteem.application.working;

import java.io.File;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.XmlReader1;
import org.rekeningsysteem.io.xml.XmlReader2;
import org.rekeningsysteem.io.xml.XmlReader3;

import rx.Observable;

public class IOWorker {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader1;
	private final FactuurLoader oldLoader2;
	private final FactuurLoader oldLoader3;

	private final Logger logger;

	public IOWorker(Logger logger) {
		this(new XmlMaker(logger), new PdfExporter(logger), new XmlReader(logger),
				new XmlReader1(logger), new XmlReader2(logger), new XmlReader3(), logger);
	}

	public IOWorker(FactuurSaver saver, FactuurExporter exporter, FactuurLoader loader,
			FactuurLoader oldLoader1, FactuurLoader oldLoader2, FactuurLoader oldLoader3,
			Logger logger) {
		this.saver = saver;
		this.exporter = exporter;
		this.loader = loader;
		this.oldLoader1 = oldLoader1;
		this.oldLoader2 = oldLoader2;
		this.oldLoader3 = oldLoader3;
		this.logger = logger;
	}

	public void save(AbstractRekening rekening, File file) {
		this.saver.save(rekening, file);
	}

	public void export(AbstractRekening rekening, File file) {
		this.exporter.export(rekening, file);
	}

	public Observable<? extends AbstractRekening> load(File file) {
		return this.loader.load(file)
				.onErrorResumeNext(t -> this.oldLoader3.load(file))
				.onErrorResumeNext(t -> this.oldLoader2.load(file))
				.onErrorResumeNext(t -> this.oldLoader1.load(file))
				.doOnError(error -> {
					String alertText = "Deze factuur kon niet worden geopend. De file is "
							+ "waarschijnlijk corrupt. Raadpleeg de programmeur om dit probleem "
							+ "op te lossen.";
					ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
					Alert alert = new Alert(AlertType.NONE, alertText, close);
					alert.setHeaderText("Fout bij inladen factuur");
					alert.show();
					this.logger.error(error.getMessage() + "\nMislukt om factuur in te laden "
							+ "vanuit de volgende file: \"" + file + "\"\n", error);
				});
	}
}
