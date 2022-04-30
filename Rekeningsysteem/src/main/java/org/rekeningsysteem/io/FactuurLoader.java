package org.rekeningsysteem.io;

import java.nio.file.Path;

import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.data.util.AbstractRekening;

public interface FactuurLoader {

	Single<AbstractRekening> load(Path file);
}
