package org.rekeningsysteem.ui.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class MutatiesListPaneController extends AbstractListPaneController<MutatiesBon> {

	public MutatiesListPaneController(Currency currency, BtwPercentage btw) {
		this(new MutatiesListController(currency), new BtwController(btw), currency);
	}

	public MutatiesListPaneController(Currency currency, ItemList<MutatiesBon> inputList,
			BtwPercentage inputBtw) {
		this(new MutatiesListController(currency, inputList), new BtwController(inputBtw),
				currency);
	}

	public MutatiesListPaneController(MutatiesListController list, BtwController btw,
			Currency currency) {
		super(list, btw, currency);
	}
}
