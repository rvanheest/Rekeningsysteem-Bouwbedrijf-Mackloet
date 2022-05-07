package org.rekeningsysteem.io.xml;

import io.reactivex.rxjava3.core.Single;
import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.data.util.Document;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.nio.file.Path;

public class IOWorker implements FactuurSaver, FactuurExporter, FactuurLoader {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader1;
	private final FactuurLoader oldLoader2;
	private final FactuurLoader oldLoader3;

	private final Logger logger;

	public IOWorker(Logger logger) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			this.saver = new XmlWriter(builder, transformerFactory);
			this.exporter = new PdfExporter();
			this.loader = new XmlReader4(builder);
			this.oldLoader1 = new XmlReader1(builder);
			this.oldLoader2 = new XmlReader2(builder);
			this.oldLoader3 = new XmlReader3(builder);

			this.logger = logger;
		}
		catch (ParserConfigurationException e) {
			throw new RuntimeException("can't produce a DocumentBuilder", e);
		}
	}

	public IOWorker(FactuurSaver saver, FactuurExporter exporter, FactuurLoader loader, FactuurLoader oldLoader1, FactuurLoader oldLoader2, FactuurLoader oldLoader3, Logger logger) {
		this.saver = saver;
		this.exporter = exporter;
		this.loader = loader;
		this.oldLoader1 = oldLoader1;
		this.oldLoader2 = oldLoader2;
		this.oldLoader3 = oldLoader3;

		this.logger = logger;
	}

	@Override
	public Single<Document> load(Path file) {
		return this.loader.load(file)
				.onErrorResumeNext(t -> this.oldLoader3.load(file))
				.onErrorResumeNext(t -> this.oldLoader2.load(file))
				.onErrorResumeNext(t -> this.oldLoader1.load(file))
				.doOnError(error -> this.logger.error(error.getMessage()
						+ "\nMislukt om factuur in te laden vanuit de volgende file: \""
						+ file + "\"\n", error));
	}

	@Override
	public void export(Document document, Path saveLocation) throws PdfException {
		this.exporter.export(document, saveLocation);
	}

	@Override
	public void save(Document document, Path saveLocation) {
		this.saver.save(document, saveLocation);
	}
}
