package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class MutatiesBonController extends AbstractListItemController<MutatiesBon> {

	public MutatiesBonController(Currency currency) {
		this(new MutatiesBonPane(currency));
		this.getUI().setOmschrijving("Bonnummer");
	}

	public MutatiesBonController(Currency currency, MutatiesBon input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setBonnummer(input.getBonnummer());
		this.getUI().setPrijs(input.getMateriaal().getBedrag());
	}

	public MutatiesBonController(MutatiesBonPane ui) {
		super(ui, Observable.combineLatest(ui.getOmschrijving(),
				ui.getBonnummer(),
				ui.getPrijs().map(Geld::new), MutatiesBon::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent()
						.map(event -> Optional.empty()))
				.first());
	}

	public MutatiesBonPane getUI() {
		return (MutatiesBonPane) super.getUI();
	}
}
