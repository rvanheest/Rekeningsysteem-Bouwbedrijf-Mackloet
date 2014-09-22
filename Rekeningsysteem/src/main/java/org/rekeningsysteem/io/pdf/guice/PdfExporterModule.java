package org.rekeningsysteem.io.pdf.guice;

import java.util.List;

import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.PdfListItemVisitor;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class PdfExporterModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurSaver.class).to(PdfExporter.class);
		this.bind(new TypeLiteral<ListItemVisitor<List<String>>>() {})
				.to(PdfListItemVisitor.class);
	}
}
