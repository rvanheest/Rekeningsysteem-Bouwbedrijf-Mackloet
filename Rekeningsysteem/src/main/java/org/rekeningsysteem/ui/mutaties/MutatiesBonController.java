package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

public class MutatiesBonController {

	private MutatiesBonPane ui;
	private Observable<Optional<MutatiesBon>> model;

	public MutatiesBonController(Currency currency) {
		this(new MutatiesBonPane(currency));
	}

	public MutatiesBonController(Currency currency, MutatiesBon input) {
		this(currency);
		this.ui.setOmschrijving(input.getOmschrijving());
		this.ui.setBonnummer(input.getBonnummer());
		this.ui.setPrijs(input.getMateriaal().getBedrag());
	}

	public MutatiesBonController(MutatiesBonPane ui) {
		this.ui = ui;
		
		Observable<Optional<MutatiesBon>> item = Observable.combineLatest(
				this.ui.getOmschrijving(), this.ui.getBonnummer(),
				this.ui.getPrijs().map(Geld::new), MutatiesBon::new)
				.sample(this.ui.getAddButtonEvent())
				.map(Optional::of);
		Observable<Optional<MutatiesBon>> cancel = this.ui.getCancelButtonEvent()
				.map(event -> Optional.empty());
		
		this.model = Observable.merge(item, cancel).first();
	}

	public MutatiesBonPane getUI() {
		return this.ui;
	}

	public Observable<Optional<MutatiesBon>> getModel() {
		return this.model;
	}
}
