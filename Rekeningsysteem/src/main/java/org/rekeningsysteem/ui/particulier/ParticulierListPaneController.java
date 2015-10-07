package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListPaneController;

@Deprecated
public class ParticulierListPaneController extends AbstractListPaneController<ParticulierArtikel> {

	public ParticulierListPaneController(Currency currency, Database db,
			BtwPercentage defaultBtw) {
		super(new ParticulierListController(currency, db, defaultBtw), currency);
	}

	public ParticulierListPaneController(Currency currency, Database db,
			BtwPercentage defaultBtw, ItemList<ParticulierArtikel> inputList) {
		super(new ParticulierListController(currency, db, defaultBtw, inputList), currency);
	}
}
