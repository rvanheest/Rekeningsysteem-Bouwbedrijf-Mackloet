package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.ProductLoon;

import rx.Observable;

public class ProductLoonController {

	private final ProductLoonPane ui;
	private final Observable<ProductLoon> model;

	public ProductLoonController(Currency currency) {
		this(new ProductLoonPane(currency));
	}

	public ProductLoonController(Currency currency, ProductLoon input) {
		this(currency);
		this.getUI().setUren(input.getUren());
		this.getUI().setUurloon(input.getUurloon().getBedrag());
	}

	public ProductLoonController(ProductLoonPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getUren(), ui.getUurloon().map(Geld::new),
				(u, uurloon) -> new ProductLoon("Uurloon à " + uurloon.getBedrag(), u, uurloon));
	}

	public ProductLoonPane getUI() {
		return this.ui;
	}

	public Observable<ProductLoon> getModel() {
		return this.model;
	}
}
