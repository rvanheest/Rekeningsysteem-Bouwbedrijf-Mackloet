package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class LoonController extends AbstractListItemController<AbstractLoon> {

	public LoonController(Currency currency) {
		this(new LoonPane(currency));
	}

	public LoonController(Currency currency, InstantLoon input) {
		this(currency);
		this.getUI().setInstantLoon(input);
	}

	public LoonController(Currency currency, ProductLoon input) {
		this(currency);
		this.getUI().setProductLoon(input);
	}

	public LoonController(LoonPane ui) {
		super(ui, Observable.merge(
				ui.getType().flatMap(type -> {
					switch (type) {
						case INSTANT:
							return ui.getInstantLoon();
						case PRODUCT:
							return ui.getProductLoon();
						default:
							return null;
							// Does never happen!!!
					}
				}).sample(ui.getAddButtonEvent()).map(Optional::of),
				ui.getCancelButtonEvent().map(event -> Optional.<AbstractLoon> empty()))
				.first());
	}

	@Override
	public LoonPane getUI() {
		return (LoonPane) super.getUI();
	}
}
