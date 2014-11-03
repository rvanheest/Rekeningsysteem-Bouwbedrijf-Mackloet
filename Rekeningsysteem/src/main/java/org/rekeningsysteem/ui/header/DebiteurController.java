package org.rekeningsysteem.ui.header;

import org.rekeningsysteem.data.util.header.Debiteur;

import rx.Observable;

public class DebiteurController {

	private final DebiteurPane ui;
	private final Observable<Debiteur> model;

	public DebiteurController() {
		this(new DebiteurPane());
	}

	public DebiteurController(Debiteur input) {
		this();
		this.ui.setNaam(input.getNaam());
		this.ui.setStraat(input.getStraat());
		this.ui.setNummer(input.getNummer());
		this.ui.setPostcode(input.getPostcode());
		this.ui.setPlaats(input.getPlaats());
		this.ui.setBtwNummer(input.getBtwNummer().orElse(""));
	}

	public DebiteurController(DebiteurPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(this.ui.getNaam(), this.ui.getStraat(),
				this.ui.getNummer(), this.ui.getPostcode(), this.ui.getPlaats(),
				this.ui.getBtwnummer(), Debiteur::new);
	}

	public DebiteurPane getUI() {
		return this.ui;
	}

	public Observable<Debiteur> getModel() {
		return this.model;
	}
}
