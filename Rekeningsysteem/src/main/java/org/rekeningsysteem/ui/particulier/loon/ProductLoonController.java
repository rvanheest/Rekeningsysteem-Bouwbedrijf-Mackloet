package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

public class ProductLoonController {

	private final ProductLoonPane ui;
	private final Observable<ProductLoon> model;

	public ProductLoonController(Currency currency) {
		this(currency, PropertiesWorker.getInstance());
	}

	public ProductLoonController(Currency currency, PropertiesWorker properties) {
		this(new ProductLoonPane(currency));
		properties.getProperty(PropertyModelEnum.UURLOON)
				.map(Double::parseDouble)
				.ifPresent(this.getUI()::setUurloon);
	}

	public ProductLoonController(ProductLoonPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getUren(), ui.getUurloon().map(Geld::new),
				ui.getLoonBtwPercentage(),
				(uren, uurloon, percentage) -> new ProductLoon("Uurloon à " + uurloon.getBedrag(),
						uren, uurloon, percentage));
	}

	public ProductLoonPane getUI() {
		return this.ui;
	}

	public Observable<ProductLoon> getModel() {
		return this.model;
	}
}
