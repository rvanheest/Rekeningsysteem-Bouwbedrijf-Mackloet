package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class MutatiesListPaneController extends AbstractListPaneController<MutatiesBon> {

	public MutatiesListPaneController(Currency currency) {
		super(new MutatiesListController(currency), currency);
	}

	public MutatiesListPaneController(Currency currency, ItemList<MutatiesBon> inputList) {
		super(new MutatiesListController(currency, inputList), currency);
	}
}
