package org.rekeningsysteem.io.pdf;

import java.io.File;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;

import com.google.inject.Inject;

public class PdfExporter implements FactuurExporter {

	private PdfExporterVisitor visitor;
	private Logger logger;

	@Inject
	public PdfExporter(PdfExporterVisitor visitor, Logger logger) {
		this.visitor = visitor;
		this.logger = logger;
	}

	@Override
	public void export(AbstractRekening rekening, File saveLocation) {
		try {
			this.visitor.setSaveLocation(saveLocation);
			rekening.accept(this.visitor);
		}
		catch (Exception exception) {
			this.logger.error(exception.getMessage(), exception);
		}
	}
}
