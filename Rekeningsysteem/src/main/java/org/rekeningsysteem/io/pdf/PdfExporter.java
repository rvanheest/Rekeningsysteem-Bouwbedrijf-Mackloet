package org.rekeningsysteem.io.pdf;

import org.rekeningsysteem.data.util.Document;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.FactuurExporter;

import java.nio.file.Path;

public class PdfExporter implements FactuurExporter {

	private final PdfExporterVisitor visitor;

	public PdfExporter() {
		this(new PdfExporterVisitor(new PdfListItemVisitor()));
	}

	public PdfExporter(boolean autoOpen) {
		this(new PdfExporterVisitor(autoOpen, new PdfListItemVisitor()));
	}

	public PdfExporter(PdfExporterVisitor visitor) {
		this.visitor = visitor;
	}

	@Override
	public void export(Document rekening, Path saveLocation) throws PdfException {
		this.visitor.setSaveLocation(saveLocation);
		this.visitor.visit(rekening);
	}
}
