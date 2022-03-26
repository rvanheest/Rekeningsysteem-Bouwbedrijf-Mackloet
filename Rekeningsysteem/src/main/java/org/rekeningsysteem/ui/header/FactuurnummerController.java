package org.rekeningsysteem.ui.header;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

public class FactuurnummerController {

	private final FactuurnummerPane ui;
	private final Observable<Optional<String>> model;

	public FactuurnummerController(FactuurnummerType type) {
		this(new FactuurnummerPane(type));
	}

	public FactuurnummerController(FactuurnummerType type, Optional<String> input) {
		this(type);
		this.ui.setFactuurnummer(input);
	}

	public FactuurnummerController(FactuurnummerPane ui) {
		this.ui = ui;
		this.model = this.ui.getFactuurnummer();
	}

	public FactuurnummerPane getUI() {
		return this.ui;
	}

	public Observable<Optional<String>> getModel() {
		return this.model;
	}
}
