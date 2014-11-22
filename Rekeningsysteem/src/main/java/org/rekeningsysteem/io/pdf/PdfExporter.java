package org.rekeningsysteem.io.pdf;

import java.io.File;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.logging.ApplicationLogger;

public class PdfExporter implements FactuurExporter {

	private PdfExporterVisitor visitor;
	private Logger logger;
	
	public PdfExporter() {
		this(new PdfExporterVisitor(new PdfListItemVisitor()), ApplicationLogger.getInstance());
	}

	public PdfExporter(PdfExporterVisitor visitor) {
		this(visitor, ApplicationLogger.getInstance());
	}

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
