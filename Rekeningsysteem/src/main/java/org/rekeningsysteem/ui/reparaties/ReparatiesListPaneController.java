package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class ReparatiesListPaneController extends AbstractListPaneController<ReparatiesBon> {

	public ReparatiesListPaneController(Currency currency, BtwPercentage btw) {
		this(new ReparatiesListController(currency), new BtwController(btw), currency);
	}

	public ReparatiesListPaneController(Currency currency, ItemList<ReparatiesBon> inputList,
			BtwPercentage inputBtw) {
		this(new ReparatiesListController(currency, inputList), new BtwController(inputBtw),
				currency);
	}

	public ReparatiesListPaneController(ReparatiesListController list, BtwController btw,
			Currency currency) {
		super(list, btw, currency);
	}
}
