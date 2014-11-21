package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class ParticulierListPaneController extends AbstractListPaneController<ParticulierArtikel> {

	public ParticulierListPaneController(Currency currency, BtwPercentage btw) {
		this(new ParticulierListController(currency), new BtwController(btw), currency);
	}

	public ParticulierListPaneController(Currency currency, ItemList<ParticulierArtikel> inputList,
			BtwPercentage inputBtw) {
		this(new ParticulierListController(currency, inputList), new BtwController(inputBtw),
				currency);
	}

	public ParticulierListPaneController(ParticulierListController list, BtwController btw,
			Currency currency) {
		super(list, btw, currency);
	}
}
