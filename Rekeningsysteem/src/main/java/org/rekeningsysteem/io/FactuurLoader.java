package org.rekeningsysteem.io;

import java.nio.file.Path;

import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.data.util.Document;

public interface FactuurLoader {

	Single<Document> load(Path file);
}
