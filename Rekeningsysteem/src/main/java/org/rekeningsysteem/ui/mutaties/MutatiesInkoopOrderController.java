package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class MutatiesInkoopOrderController extends AbstractListItemController<MutatiesInkoopOrder> {

	public MutatiesInkoopOrderController(Currency currency) {
		this(new MutatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public MutatiesInkoopOrderController(Currency currency, MutatiesInkoopOrder input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setOrdernummer(input.getInkoopOrderNummer());
		this.getUI().setPrijs(input.getMateriaal().getBedrag());
	}

	public MutatiesInkoopOrderController(MutatiesInkoopOrderPane ui) {
		super(ui, Observable.combineLatest(ui.getOmschrijving(),
				ui.getOrdernummer(),
				ui.getPrijs().map(Geld::new), MutatiesInkoopOrder::new)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent()
						.map(event -> Optional.empty()))
				.first());
	}

	public MutatiesInkoopOrderPane getUI() {
		return (MutatiesInkoopOrderPane) super.getUI();
	}
}
