package org.rekeningsysteem.ui.btw;

import org.rekeningsysteem.data.util.BtwPercentage;

import rx.Observable;
import rx.Observer;

public class BtwController implements Observer<BtwPercentage> {

	private final BtwPane ui;
	private final Observable<BtwPercentage> model;

	public BtwController() {
		this(new BtwPane());
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

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(BtwPercentage btw) {
		this.ui.setLoon(btw.getLoonPercentage());
		this.ui.setMateriaal(btw.getMateriaalPercentage());
	}
}
