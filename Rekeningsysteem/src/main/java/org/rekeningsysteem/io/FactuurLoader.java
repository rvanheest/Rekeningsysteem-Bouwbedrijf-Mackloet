package org.rekeningsysteem.io;

import java.io.File;

import io.reactivex.rxjava3.core.Single;
import org.rekeningsysteem.data.util.AbstractRekening;

public interface FactuurLoader {

	Single<AbstractRekening> load(File file);
}
