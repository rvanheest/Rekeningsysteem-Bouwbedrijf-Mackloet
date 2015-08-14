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
import org.rekeningsysteem.io.xml.XmlReader2;

import rx.Observable;

public class IOWorker {

	private final FactuurSaver saver;
	private final FactuurExporter exporter;
	private final FactuurLoader loader;
	private final FactuurLoader oldLoader1;
	private final FactuurLoader oldLoader2;

	public IOWorker() {
		this(new XmlMaker(), new PdfExporter(), new XmlReader(), new XmlReader1(), new XmlReader2());
	}

	public IOWorker(FactuurSaver saver, FactuurExporter exporter, FactuurLoader loader,
			FactuurLoader oldLoader1, FactuurLoader oldLoader2) {
		this.saver = saver;
		this.exporter = exporter;
		this.loader = loader;
		this.oldLoader1 = oldLoader1;
		this.oldLoader2 = oldLoader2;
	}

	public void save(AbstractRekening rekening, File file) {
		this.saver.save(rekening, file);
	}

	public void export(AbstractRekening rekening, File file) {
		this.exporter.export(rekening, file);
	}

	public Observable<? extends AbstractRekening> load(File file) {
		return this.loader.load(file)
				.onErrorResumeNext(t -> this.oldLoader2.load(file))
				.onErrorResumeNext(t -> this.oldLoader1.load(file));
	}
}
