package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class MutatiesListPaneController extends AbstractListPaneController<MutatiesInkoopOrder> {

	public MutatiesListPaneController(Currency currency) {
		super(new MutatiesListController(currency), currency);
	}

	public MutatiesListPaneController(Currency currency, ItemList<MutatiesInkoopOrder> inputList) {
		super(new MutatiesListController(currency, inputList), currency);
	}
}
