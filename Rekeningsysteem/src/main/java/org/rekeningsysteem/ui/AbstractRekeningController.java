package org.rekeningsysteem.ui;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.PropertyFactuurnummerManagerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;

import com.google.inject.Guice;

import rx.Observable;

public abstract class AbstractRekeningController {

	private final RekeningSplitPane ui;

	public AbstractRekeningController(RekeningSplitPane ui) {
		this.ui = ui;
	}

	public RekeningSplitPane getUI() {
		return this.ui;
	}

	public abstract Observable<? extends AbstractRekening> getModel();

	public abstract void initFactuurnummer();

	protected final FactuurnummerManagerFactory getFactuurnummerFactory() {
		return Guice.createInjector(new PropertyFactuurnummerManagerModule(),
				new ConsoleLoggerModule(), new ConfigPropertiesModule())
				.getInstance(FactuurnummerManagerFactory.class);
	}
}
