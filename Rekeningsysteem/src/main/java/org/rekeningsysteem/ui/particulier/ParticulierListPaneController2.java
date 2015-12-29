package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

//TODO ParticulierListPaneController
public class ParticulierListPaneController2 extends AbstractListPaneController<ParticulierArtikel2> {

	public ParticulierListPaneController2(Currency currency, Database db,
			BtwPercentage defaultBtw) {
		super(new ParticulierListController2(currency, db, defaultBtw), currency);
	}

	public ParticulierListPaneController2(Currency currency, Database db,
			BtwPercentage defaultBtw, ItemList<ParticulierArtikel2> inputList) {
		super(new ParticulierListController2(currency, db, defaultBtw, inputList), currency);
	}
}
