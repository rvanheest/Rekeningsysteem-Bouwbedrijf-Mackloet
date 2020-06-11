package org.rekeningsysteem.io.pdf;

import java.io.File;

import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.FactuurExporter;

public class PdfExporter implements FactuurExporter {

	private PdfExporterVisitor visitor;
	private Logger logger;
	
	public PdfExporter(Logger logger) {
		this(new PdfExporterVisitor(new PdfListItemVisitor()), logger);
	}
	
	public PdfExporter(boolean autoOpen, Logger logger) {
		this(new PdfExporterVisitor(autoOpen, new PdfListItemVisitor()), logger);
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
		catch (PdfException exception) {
		  System.out.println("found  pdf exception");
			throw exception;
		}
		catch (Exception exception) {
			this.logger.error(exception.getMessage(), exception);
		}
	}
}
