package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

public class MutatiesInkoopOrderController extends AbstractListItemController<MutatiesInkoopOrder> {

	public MutatiesInkoopOrderController(Currency currency) {
		this(new MutatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public MutatiesInkoopOrderController(MutatiesInkoopOrderPane ui) {
		super(ui,
			getMutatiesInkoopOrderObservable(ui)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.firstElement()
		);
	}

	private static Observable<MutatiesInkoopOrder> getMutatiesInkoopOrderObservable(MutatiesInkoopOrderPane ui) {
		return Observable.combineLatest(
			ui.getOmschrijving(),
			ui.getOrdernummer(),
			ui.getPrijs().map(Geld::new),
			MutatiesInkoopOrder::new
		);
	}

	public MutatiesInkoopOrderPane getUI() {
		return (MutatiesInkoopOrderPane) super.getUI();
	}
}
