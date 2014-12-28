package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class ReparatiesBonController extends AbstractListItemController<ReparatiesBon> {

	public ReparatiesBonController(Currency currency) {
		this(new ReparatiesBonPane(currency));
		this.getUI().setOmschrijving("Bonnummer");
	}

	public ReparatiesBonController(Currency currency, ReparatiesBon input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setBonnummer(input.getBonnummer());
		this.getUI().setLoon(input.getLoon().getBedrag());
		this.getUI().setMateriaal(input.getMateriaal().getBedrag());
	}

	public ReparatiesBonController(ReparatiesBonPane ui) {
		super(ui, Observable.combineLatest(ui.getOmschrijving(),
				ui.getBonnummer(),
				ui.getLoon().map(Geld::new),
				ui.getMateriaal().map(Geld::new), ReparatiesBon::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent()
						.map(event -> Optional.empty()))
				.first());
	}

	public ReparatiesBonPane getUI() {
		return (ReparatiesBonPane) super.getUI();
	}
}
