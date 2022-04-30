package org.rekeningsysteem.io;

import java.nio.file.Path;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;

public interface FactuurExporter {

	void export(AbstractRekening rekening, Path saveLocation) throws PdfException;
}
