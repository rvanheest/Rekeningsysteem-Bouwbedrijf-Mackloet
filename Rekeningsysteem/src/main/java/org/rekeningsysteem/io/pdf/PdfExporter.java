package org.rekeningsysteem.io.pdf;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.FactuurExporter;

import java.io.File;

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
	public void export(AbstractRekening rekening, File saveLocation) throws PdfException {
		this.visitor.setSaveLocation(saveLocation);
		this.visitor.visit(rekening);
	}
}
