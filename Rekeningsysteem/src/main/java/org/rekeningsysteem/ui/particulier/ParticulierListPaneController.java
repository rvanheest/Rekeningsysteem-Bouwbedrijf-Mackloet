package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class ParticulierListPaneController extends AbstractListPaneController<ParticulierArtikel> {

	public ParticulierListPaneController(Currency currency, BtwPercentage defaultBtw) {
		super(new ParticulierListController(currency, defaultBtw), currency);
	}

	public ParticulierListPaneController(Currency currency, BtwPercentage defaultBtw,
			ItemList<ParticulierArtikel> inputList) {
		super(new ParticulierListController(currency, defaultBtw, inputList), currency);
	}
}
