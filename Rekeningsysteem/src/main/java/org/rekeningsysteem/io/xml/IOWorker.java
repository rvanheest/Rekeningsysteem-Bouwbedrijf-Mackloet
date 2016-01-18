package org.rekeningsysteem.io.xml;

import java.io.File;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;

import rx.Observable;

public class IOWorker implements FactuurSaver, FactuurExporter, FactuurLoader {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader1;
	private final FactuurLoader oldLoader2;
	private final FactuurLoader oldLoader3;

	private final Logger logger;

	public IOWorker(Logger logger) {
		this(new XmlMaker(logger), new PdfExporter(logger), new XmlReader(logger),
				new XmlReader1(logger), new XmlReader2(logger), new XmlReader3(logger), logger);
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

	@Override
	public Observable<AbstractRekening> load(File file) {
		return this.loader.load(file)
				.onErrorResumeNext(t -> this.oldLoader3.load(file))
				.onErrorResumeNext(t -> this.oldLoader2.load(file))
				.onErrorResumeNext(t -> this.oldLoader1.load(file))
				.doOnError(error -> this.logger.error(error.getMessage()
						+ "\nMislukt om factuur in te laden vanuit de volgende file: \""
						+ file + "\"\n", error));
	}

	@Override
	public void export(AbstractRekening rekening, File saveLocation) {
		this.exporter.export(rekening, saveLocation);
	}

	@Override
	public void save(AbstractRekening rekening, File saveLocation) {
		this.saver.save(rekening, saveLocation);
	}
}
