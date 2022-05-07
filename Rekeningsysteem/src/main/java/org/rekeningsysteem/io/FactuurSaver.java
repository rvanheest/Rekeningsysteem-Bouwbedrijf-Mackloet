package org.rekeningsysteem.io;

import org.rekeningsysteem.data.util.Document;

import java.nio.file.Path;

public interface FactuurSaver {

	void save(Document document, Path saveLocation);
}
