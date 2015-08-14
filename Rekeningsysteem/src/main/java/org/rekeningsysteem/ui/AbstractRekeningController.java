package org.rekeningsysteem.ui;

import java.util.Currency;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

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

	protected static Currency getDefaultCurrency(PropertiesWorker properties) {
		return properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR"));
	}

	protected static BtwPercentage getDefaultBtwPercentage(PropertiesWorker properties) {
		return properties.getProperty(PropertyModelEnum.LOONBTWPERCENTAGE)
						.map(Double::parseDouble)
						.<BtwPercentage> flatMap(l -> properties
								.getProperty(PropertyModelEnum.MATERIAALBTWPERCENTAGE)
								.map(Double::parseDouble)
								.map(m -> new BtwPercentage(l, m)))
						.orElse(new BtwPercentage(6, 21));
	}
}
