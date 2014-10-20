package org.rekeningsysteem.application.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.xml.OldXmlReader;
import org.rekeningsysteem.io.xml.XmlReader;

import rx.Observable;

import com.google.inject.Inject;

public class IOWorker {

	private final FactuurSaver maker;
	private final XmlReader reader;
	private final OldXmlReader oldReader;
	private final FactuurExporter pdf;

	@Inject
	public IOWorker(FactuurSaver maker, XmlReader reader, OldXmlReader oldReader, FactuurExporter pdf) {
		this.maker = maker;
		this.reader = reader;
		this.oldReader = oldReader;
		this.pdf = pdf;
	}

	public void save(AbstractRekening model, File file) {
		this.maker.save(model, file);
	}

	public Observable<AbstractRekening> load(File file) {
		return this.reader.load(file)
				.onExceptionResumeNext(this.oldReader.load(file));
	}

	public void export(AbstractRekening model, File file) {
		this.pdf.export(model, file);
	}
}
