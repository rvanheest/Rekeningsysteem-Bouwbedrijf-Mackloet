package org.rekeningsysteem.ui;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.PropertyFactuurnummerManagerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;

import com.google.inject.Guice;

import rx.Observable;

public abstract class AbstractRekeningController<M extends AbstractRekening> {

	private final RekeningSplitPane ui;
	private final Observable<M> model;

	public AbstractRekeningController(RekeningSplitPane ui, Observable<M> model) {
		this.ui = ui;
		this.model = model;
	}

	public RekeningSplitPane getUI() {
		return this.ui;
	}

	public Observable<M> getModel() {
		return this.model;
	}

	public abstract void initFactuurnummer();

	protected final FactuurnummerManagerFactory getFactuurnummerFactory() {
		return Guice.createInjector(new PropertyFactuurnummerManagerModule(),
				new ConsoleLoggerModule(), new ConfigPropertiesModule())
				.getInstance(FactuurnummerManagerFactory.class);
	}
}
