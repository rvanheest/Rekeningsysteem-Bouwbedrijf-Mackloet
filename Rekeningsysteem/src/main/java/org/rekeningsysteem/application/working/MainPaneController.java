package org.rekeningsysteem.application.working;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import com.google.inject.Inject;

public class MainPaneController {

	private final MainPane ui;

	@Inject
	public MainPaneController(MainPane ui) {
		this.ui = ui;
		this.initButtonHandlers();
	}
	
	public MainPane getUI() {
		return this.ui;
	}

	private void initButtonHandlers() {
		Func1<Observable<RekeningTab>, Subscription> addSelect = (tab) -> tab
				.doOnNext(this.ui.getTabPane()::addTab)
				.doOnNext(this.ui.getTabPane()::selectTab)
				.subscribe();
		addSelect.call(this.initAangenomenObservable());
		addSelect.call(this.initMutatiesObservable());
		addSelect.call(this.initReparatiesObservable());
		addSelect.call(this.initParticulierObservable());
		addSelect.call(this.initOfferteObservable());
	}

	private Observable<RekeningTab> initAangenomenObservable() {
		return this.ui.getAangenomenEvents()
				.map(event -> new RekeningTab("Aangenomen factuur"));
	}

	private Observable<RekeningTab> initMutatiesObservable() {
		return this.ui.getMutatiesEvents()
				.map(event -> new RekeningTab("Mutaties factuur"));
	}

	private Observable<RekeningTab> initReparatiesObservable() {
		return this.ui.getReparatiesEvents()
				.map(event -> new RekeningTab("Reparaties factuur"));
	}

	private Observable<RekeningTab> initParticulierObservable() {
		return this.ui.getParticulierEvents()
				.map(event -> new RekeningTab("Particulier factuur"));
	}

	private Observable<RekeningTab> initOfferteObservable() {
		return this.ui.getOfferteEvents()
				.map(event -> new RekeningTab("Offerte"));
	}
}
