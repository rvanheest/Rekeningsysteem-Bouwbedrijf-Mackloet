package org.rekeningsysteem.io.pdf.guice;

import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;

import com.google.inject.AbstractModule;

public class PdfExporterModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurSaver.class).to(PdfExporter.class);
	}
}
