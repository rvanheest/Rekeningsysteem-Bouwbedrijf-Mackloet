package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.WorkingPaneController;
import org.rekeningsysteem.ui.btw.BtwController;
import org.rekeningsysteem.ui.list.BtwListPane;

import rx.Observable;

public class ParticulierListPaneController extends WorkingPaneController {

	private final Currency currency;
	private final Observable<ItemList<ParticulierArtikel>> listModel;

	public ParticulierListPaneController(Currency currency, BtwPercentage defaultBtw) {
		this(new ParticulierListController(currency, defaultBtw), currency);
	}

	public ParticulierListPaneController(Currency currency, BtwPercentage defaultBtw,
			ItemList<ParticulierArtikel> inputList) {
		this(new ParticulierListController(currency, defaultBtw, inputList), currency);
	}

	public ParticulierListPaneController(ParticulierListController list, Currency currency) {
		super(new BtwListPane(list.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<ParticulierArtikel>> getListModel() {
		return this.listModel;
	}
}
