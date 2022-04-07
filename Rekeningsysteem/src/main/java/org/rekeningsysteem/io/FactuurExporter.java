package org.rekeningsysteem.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;

public interface FactuurExporter {

	void export(AbstractRekening rekening, File saveLocation) throws PdfException;
}
