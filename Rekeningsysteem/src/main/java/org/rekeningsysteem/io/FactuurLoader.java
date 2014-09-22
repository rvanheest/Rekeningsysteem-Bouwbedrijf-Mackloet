package org.rekeningsysteem.io;

import java.io.File;

import org.rekeningsysteem.data.util.AbstractRekening;

import rx.Observable;

public interface FactuurLoader {

	Observable<? extends AbstractRekening> load(File file);
}
