package org.rekeningsysteem.application.working;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.guice.PdfExporterModule;
import org.rekeningsysteem.io.xml.OldXmlReader;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.guice.XmlMakerModule;
import org.rekeningsysteem.io.xml.guice.XmlReaderModule;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;

import rx.Observable;

import com.google.inject.Guice;

public class IOWorker {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader;

	public IOWorker() {
		this.saver = Guice.createInjector(new XmlMakerModule(), new ConsoleLoggerModule())
				.getInstance(XmlMaker.class);
		this.exporter = Guice.createInjector(new PdfExporterModule(),
				new ConfigPropertiesModule(), new ConsoleLoggerModule())
				.getInstance(FactuurExporter.class);
		this.loader = Guice.createInjector(new XmlReaderModule(), new ConsoleLoggerModule())
				.getInstance(XmlReader.class);
		this.oldLoader = Guice.createInjector(new XmlReaderModule())
				.getInstance(OldXmlReader.class);
	}

	public IOWorker(FactuurSaver saver, FactuurExporter exporter, FactuurLoader loader,
			FactuurLoader oldLoader) {
		this.saver = saver;
		this.exporter = exporter;
		this.loader = loader;
		this.oldLoader = oldLoader;
	}

	public void save(AbstractRekening rekening, File file) {
		this.saver.save(rekening, file);
	}

	public void export(AbstractRekening rekening, File file) {
		this.exporter.export(rekening, file);
	}

	public Observable<? extends AbstractRekening> load(File file) {
		return this.loader.load(file)
				.onErrorResumeNext(throwable -> this.oldLoader.load(file));
	}
}
