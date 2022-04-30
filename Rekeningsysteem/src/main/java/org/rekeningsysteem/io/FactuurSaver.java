package org.rekeningsysteem.io;

import java.nio.file.Path;

import org.rekeningsysteem.data.util.AbstractRekening;

public interface FactuurSaver {

	void save(AbstractRekening rekening, Path saveLocation);
}
