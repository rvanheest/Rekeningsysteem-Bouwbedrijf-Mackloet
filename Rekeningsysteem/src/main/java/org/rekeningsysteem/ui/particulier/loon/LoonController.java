package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.ui.list.AbstractListItemController;

public class LoonController extends AbstractListItemController<AbstractLoon> {

	public LoonController(Currency currency, BtwPercentage defaultBtw) {
		this(new LoonPane(currency), defaultBtw);
	}

	public LoonController(Currency currency, BtwPercentage defaultBtw, InstantLoon input) {
		this(currency, defaultBtw);
		this.getUI().setInstantLoon(input);
	}

	public LoonController(Currency currency, BtwPercentage defaultBtw, ProductLoon input) {
		this(currency, defaultBtw);
		this.getUI().setProductLoon(input);
	}

	public LoonController(LoonPane ui, BtwPercentage defaultBtw) {
		super(ui, ui.getType().flatMap(type -> {
			switch (type) {
				case INSTANT:
					return ui.getInstantLoon();
				case PRODUCT:
					return ui.getProductLoon();
				default:
					return null;
					// Does never happen!!!
			}
		}).sample(ui.getAddButtonEvent()).map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());
		ui.setBtwPercentage(defaultBtw.getLoonPercentage());
	}

	@Override
	public LoonPane getUI() {
		return (LoonPane) super.getUI();
	}
}
