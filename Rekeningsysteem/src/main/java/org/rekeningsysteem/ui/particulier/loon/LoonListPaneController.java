package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.List;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

@Deprecated
public class LoonListPaneController extends AbstractListPaneController<AbstractLoon> {
	
	public LoonListPaneController(Currency currency, BtwPercentage defaultBtw) {
		super(new LoonListController(currency, defaultBtw), LoonWorkingPane::new, currency);
	}

	public LoonListPaneController(Currency currency, BtwPercentage defaultBtw,
			List<AbstractLoon> input) {
		super(new LoonListController(currency, defaultBtw, input), LoonWorkingPane::new, currency);
	}
}
