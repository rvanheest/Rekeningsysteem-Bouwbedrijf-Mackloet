package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import rx.Observable;

// TODO ProductLoonController
public class ProductLoonController2 {

	private final ProductLoonPane2 ui;
	private final Observable<ProductLoon2> model;

	public ProductLoonController2(Currency currency) {
		this(currency, PropertiesWorker.getInstance());
	}

	public ProductLoonController2(Currency currency, PropertiesWorker properties) {
		this(new ProductLoonPane2(currency));
		properties.getProperty(PropertyModelEnum.UURLOON)
				.map(Double::parseDouble)
				.ifPresent(this.getUI()::setUurloon);
	}

	public ProductLoonController2(Currency currency, ProductLoon2 input) {
		this(currency);
		this.getUI().setUren(input.getUren());
		this.getUI().setUurloon(input.getUurloon().getBedrag());
	}

	public ProductLoonController2(ProductLoonPane2 ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getUren(), ui.getUurloon().map(Geld::new),
				ui.getLoonBtwPercentage(),
				(uren, uurloon, percentage) -> new ProductLoon2("Uurloon à " + uurloon.getBedrag(),
						uren, uurloon, percentage));
	}

	public ProductLoonPane2 getUI() {
		return this.ui;
	}

	public Observable<ProductLoon2> getModel() {
		return this.model;
	}
}
