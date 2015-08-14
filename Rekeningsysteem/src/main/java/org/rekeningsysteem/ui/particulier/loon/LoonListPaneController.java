package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.List;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class LoonListPaneController extends AbstractListPaneController<AbstractLoon> {
	
	public LoonListPaneController(Currency currency, BtwPercentage defaultBtw) {
		super(new LoonListController(currency, defaultBtw), currency);
	}

	public LoonListPaneController(Currency currency, BtwPercentage defaultBtw,
			List<AbstractLoon> input) {
		super(new LoonListController(currency, defaultBtw, input), currency);
	}
}
