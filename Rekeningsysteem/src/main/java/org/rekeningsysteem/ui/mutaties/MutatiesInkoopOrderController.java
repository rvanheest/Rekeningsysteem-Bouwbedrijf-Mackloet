package org.rekeningsysteem.ui.mutaties;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import java.util.Currency;

public class MutatiesInkoopOrderController extends AbstractListItemController<MutatiesInkoopOrder, MutatiesInkoopOrderPane> {

	public MutatiesInkoopOrderController(Currency currency) {
		this(new MutatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public MutatiesInkoopOrderController(MutatiesInkoopOrderPane ui) {
		super(ui, getMutatiesInkoopOrder(ui));
	}

	private static Observable<MutatiesInkoopOrder> getMutatiesInkoopOrder(MutatiesInkoopOrderPane ui) {
		return Observable.combineLatest(
			ui.getOmschrijving(),
			ui.getOrdernummer(),
			ui.getPrijs().map(Geld::new),
			MutatiesInkoopOrder::new
		);
	}
}
