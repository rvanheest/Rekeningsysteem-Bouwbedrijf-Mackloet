package org.rekeningsysteem.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.utils.Try;

public interface FactuurLoader {

	Try<? extends AbstractRekening> load(File file);
}
