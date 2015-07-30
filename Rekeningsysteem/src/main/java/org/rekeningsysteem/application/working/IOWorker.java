package org.rekeningsysteem.application.working;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.xml.XmlReader1;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;

import rx.Observable;

public class IOWorker {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader;

	public IOWorker() {
		this(new XmlMaker(), new PdfExporter(), new XmlReader(), new XmlReader1());
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
		return this.loader.load(file).onErrorResumeNext(t -> this.oldLoader.load(file));
	}
}
