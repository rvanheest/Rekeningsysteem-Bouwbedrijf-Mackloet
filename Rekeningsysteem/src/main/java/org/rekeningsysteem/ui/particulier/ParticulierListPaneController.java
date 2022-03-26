package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

public class ParticulierListPaneController extends AbstractListPaneController<ParticulierArtikel> {

	public ParticulierListPaneController(Currency currency, Database db, BtwPercentages defaultBtw) {
		super(new ParticulierListController(currency, db, defaultBtw), currency);
	}

	public ParticulierListPaneController(Currency currency, Database db, BtwPercentages defaultBtw, ItemList<ParticulierArtikel> inputList) {
		super(new ParticulierListController(currency, db, defaultBtw, inputList), currency);
	}
}
