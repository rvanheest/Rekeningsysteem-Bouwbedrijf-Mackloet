package org.rekeningsysteem.ui.reparaties;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import java.util.Currency;

public class ReparatiesInkoopOrderController extends AbstractListItemController<ReparatiesInkoopOrder, ReparatiesInkoopOrderPane> {

	public ReparatiesInkoopOrderController(Currency currency) {
		this(new ReparatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public ReparatiesInkoopOrderController(ReparatiesInkoopOrderPane ui) {
		super(ui, getReparatiesInkoopOrder(ui));
	}

	private static Observable<ReparatiesInkoopOrder> getReparatiesInkoopOrder(ReparatiesInkoopOrderPane ui) {
		return Observable.combineLatest(
			ui.getOmschrijving(),
			ui.getOrdernummer(),
			ui.getLoon().map(Geld::new),
			ui.getMateriaal().map(Geld::new),
			ReparatiesInkoopOrder::new
		);
	}
}
