package org.rekeningsysteem.ui.btw;

import org.rekeningsysteem.data.util.BtwPercentage;

import rx.Observable;

public class BtwController {

	private final BtwPane ui;
	private final Observable<BtwPercentage> model;

	public BtwController() {
		this(new BtwPane());
	}

	public BtwController(BtwPercentage input) {
		this();
		this.ui.setLoon(input.getLoonPercentage());
		this.ui.setMateriaal(input.getMateriaalPercentage());
	}

	public BtwController(BtwPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(this.ui.getLoon(),
				this.ui.getMateriaal(), BtwPercentage::new);
	}

	public BtwPane getUI() {
		return this.ui;
	}

	public Observable<BtwPercentage> getModel() {
		return this.model;
	}
}
