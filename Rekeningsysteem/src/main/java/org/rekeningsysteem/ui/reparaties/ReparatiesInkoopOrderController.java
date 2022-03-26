package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.ui.list.AbstractListItemController;

public class ReparatiesInkoopOrderController extends AbstractListItemController<ReparatiesInkoopOrder> {

	public ReparatiesInkoopOrderController(Currency currency) {
		this(new ReparatiesInkoopOrderPane(currency));
		this.getUI().setOmschrijving("Inkooporder");
	}

	public ReparatiesInkoopOrderController(ReparatiesInkoopOrderPane ui) {
		super(ui,
			getReparatiesInkoopOrderObservable(ui)
				.sample(ui.getAddButtonEvent())
				.map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.firstElement()
		);
	}

	private static Observable<ReparatiesInkoopOrder> getReparatiesInkoopOrderObservable(ReparatiesInkoopOrderPane ui) {
		return Observable.combineLatest(
			ui.getOmschrijving(),
			ui.getOrdernummer(),
			ui.getLoon().map(Geld::new),
			ui.getMateriaal().map(Geld::new),
			ReparatiesInkoopOrder::new
		);
	}

	public ReparatiesInkoopOrderPane getUI() {
		return (ReparatiesInkoopOrderPane) super.getUI();
	}
}
