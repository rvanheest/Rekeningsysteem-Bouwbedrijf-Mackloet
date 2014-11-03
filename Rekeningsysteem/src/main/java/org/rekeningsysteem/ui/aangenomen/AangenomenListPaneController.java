package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class AangenomenListPaneController extends AbstractListPaneController<AangenomenListItem> {

	public AangenomenListPaneController(Currency currency, BtwPercentage btw) {
		this(new AangenomenListController(currency), new BtwController(btw), currency);
	}

	public AangenomenListPaneController(Currency currency, ItemList<AangenomenListItem> inputList,
			BtwPercentage inputBtw) {
		this(new AangenomenListController(currency, inputList), new BtwController(inputBtw),
				currency);
	}

	public AangenomenListPaneController(AangenomenListController list, BtwController btw,
			Currency currency) {
		super(list, btw, currency);
	}
}
