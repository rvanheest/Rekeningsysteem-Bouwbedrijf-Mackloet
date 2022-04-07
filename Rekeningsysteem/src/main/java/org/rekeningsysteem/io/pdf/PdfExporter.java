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
		switch (rekening) {
			case MutatiesFactuur factuur -> this.visitor.visit(factuur);
			case Offerte offerte -> this.visitor.visit(offerte);
			case ParticulierFactuur factuur -> this.visitor.visit(factuur);
			case ReparatiesFactuur factuur -> this.visitor.visit(factuur);
			default -> throw new IllegalStateException("Unexpected value: " + rekening);
		}
	}
}
