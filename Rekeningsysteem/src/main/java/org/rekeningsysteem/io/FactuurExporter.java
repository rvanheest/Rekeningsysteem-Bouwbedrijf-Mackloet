package org.rekeningsysteem.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;

public interface FactuurExporter {

	void export(AbstractRekening rekening, File saveLocation);
}
