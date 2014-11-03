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
	}

	public ReparatiesBonController(Currency currency, ReparatiesBon input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setBonnummer(input.getBonnummer());
		this.getUI().setLoon(input.getLoon().getBedrag());
		this.getUI().setMateriaal(input.getMateriaal().getBedrag());
	}

	public ReparatiesBonController(ReparatiesBonPane ui) {
		super(ui, Observable.merge(
				Observable.combineLatest(ui.getOmschrijving(),
						ui.getBonnummer(),
						ui.getLoon().map(Geld::new),
						ui.getMateriaal().map(Geld::new), ReparatiesBon::new)
						.sample(ui.getAddButtonEvent())
						.map(Optional::of),
				ui.getCancelButtonEvent()
						.<Optional<ReparatiesBon>> map(event -> Optional.empty()))
				.first());
	}

	public ReparatiesBonPane getUI() {
		return (ReparatiesBonPane) super.getUI();
	}
}
