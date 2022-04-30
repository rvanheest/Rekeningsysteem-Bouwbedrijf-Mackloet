package org.rekeningsysteem.ui;

import java.util.Currency;
import java.util.function.BiFunction;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

public abstract class AbstractRekeningController<M extends AbstractRekening> implements Disposable {

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

	public abstract Maybe<Boolean> getSaveSelected();

	public abstract void initFactuurnummer();

	protected final BiFunction<PropertyKey, PropertyKey, FactuurnummerManager> getFactuurnummerFactory() {
		return PropertyFactuurnummerManager::new;
	}

	protected static Currency getDefaultCurrency(PropertiesWorker properties) {
		return properties.getCurrencyProperty(PropertyModelEnum.VALUTAISO4217, "EUR");
	}

	protected static BtwPercentages getDefaultBtwPercentage(PropertiesWorker properties) {
		return properties.getDoubleProperty(PropertyModelEnum.LOONBTWPERCENTAGE)
			.flatMap(l -> properties.getDoubleProperty(PropertyModelEnum.MATERIAALBTWPERCENTAGE)
				.map(m -> new BtwPercentages(l, m)))
			.orElse(new BtwPercentages(9, 21));
	}

	@Override
	public boolean isDisposed() {
		return this.ui.isDisposed();
	}

	@Override
	public void dispose() {
		this.ui.dispose();
	}
}
