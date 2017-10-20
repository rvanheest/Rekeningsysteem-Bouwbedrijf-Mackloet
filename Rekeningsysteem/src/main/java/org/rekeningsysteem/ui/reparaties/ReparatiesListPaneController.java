package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;

import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class ReparatiesListPaneController extends AbstractListPaneController<ReparatiesInkoopOrder> {

	public ReparatiesListPaneController(Currency currency) {
		super(new ReparatiesListController(currency), currency);
	}

	public ReparatiesListPaneController(Currency currency, ItemList<ReparatiesInkoopOrder> inputList) {
		super(new ReparatiesListController(currency, inputList), currency);
	}
}
