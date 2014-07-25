package org.rekeningsysteem.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;

public interface FactuurSaver {

	void save(AbstractRekening rekening, File saveLocation);
}
