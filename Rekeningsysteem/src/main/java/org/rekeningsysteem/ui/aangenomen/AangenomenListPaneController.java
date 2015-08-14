package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class AangenomenListPaneController extends AbstractListPaneController<AangenomenListItem> {

	public AangenomenListPaneController(Currency currency, BtwPercentage defaultBtw) {
		super(new AangenomenListController(currency, defaultBtw), currency);
	}

	public AangenomenListPaneController(Currency currency, BtwPercentage defaultBtw,
			ItemList<AangenomenListItem> inputList) {
		super(new AangenomenListController(currency, defaultBtw, inputList), currency);
	}
}
