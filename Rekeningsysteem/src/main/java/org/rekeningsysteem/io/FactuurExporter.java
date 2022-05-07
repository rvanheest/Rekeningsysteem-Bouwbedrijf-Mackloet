package org.rekeningsysteem.io;

import java.nio.file.Path;

import org.rekeningsysteem.data.util.Document;
import org.rekeningsysteem.exception.PdfException;

public interface FactuurExporter {

	void export(Document document, Path saveLocation) throws PdfException;
}
