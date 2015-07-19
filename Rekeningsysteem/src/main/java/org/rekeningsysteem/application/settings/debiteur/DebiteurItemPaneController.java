package org.rekeningsysteem.application.settings.debiteur;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class DebiteurItemPaneController extends AbstractListItemController<Debiteur> {

	public DebiteurItemPaneController() {
		this(new DebiteurItemPane());
	}

	public DebiteurItemPaneController(Debiteur input) {
		this();
		this.getUI().setNaam(input.getNaam());
		this.getUI().setStraat(input.getStraat());
		this.getUI().setNummer(input.getNummer());
		this.getUI().setPostcode(input.getPostcode());
		this.getUI().setPlaats(input.getPlaats());
		this.getUI().setBtwNummer(input.getBtwNummer().orElse(""));
		this.getUI().setAsUpdate();
	}

	public DebiteurItemPaneController(DebiteurItemPane ui) {
		super(ui, Observable.combineLatest(ui.getNaam(), ui.getStraat(), ui.getNummer(),
				ui.getPostcode(), ui.getPlaats(), ui.getBtwnummer(), Debiteur::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());
	}

	public DebiteurItemPane getUI() {
		return (DebiteurItemPane) super.getUI();
	}
}
