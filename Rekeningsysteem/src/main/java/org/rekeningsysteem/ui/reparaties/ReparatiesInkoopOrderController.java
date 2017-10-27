package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class ReparatiesInkoopOrderController extends AbstractListItemController<ReparatiesInkoopOrder> {

	public ReparatiesInkoopOrderController(Currency currency) {
		this(new ReparatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public ReparatiesInkoopOrderController(Currency currency, ReparatiesInkoopOrder input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setOrdernummer(input.getInkoopOrderNummer());
		this.getUI().setLoon(input.getLoon().getBedrag());
		this.getUI().setMateriaal(input.getMateriaal().getBedrag());
	}

	public ReparatiesInkoopOrderController(ReparatiesInkoopOrderPane ui) {
		super(ui, Observable.combineLatest(ui.getOmschrijving(),
				ui.getOrdernummer(),
				ui.getLoon().map(Geld::new),
				ui.getMateriaal().map(Geld::new), ReparatiesInkoopOrder::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent()
						.map(event -> Optional.empty()))
				.first());
	}

	public ReparatiesInkoopOrderPane getUI() {
		return (ReparatiesInkoopOrderPane) super.getUI();
	}
}
