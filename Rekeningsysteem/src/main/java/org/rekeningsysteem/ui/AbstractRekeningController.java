package org.rekeningsysteem.ui;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertyKey;

import rx.Observable;
import rx.functions.Func1;

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

	public abstract Observable<Boolean> getSaveSelected();

	public abstract void initFactuurnummer();

	protected final Func1<PropertyKey, FactuurnummerManager> getFactuurnummerFactory() {
		return PropertyFactuurnummerManager::new;
	}
}
