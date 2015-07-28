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
	private final Observable<BtwPercentage> btwModel;

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
		super(new BtwListPane(list.getUI(), btw.getUI()));
		this.currency = currency;
		this.listModel = list.getModel();
		this.btwModel = btw.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<ParticulierArtikel>> getListModel() {
		return this.listModel;
	}

	public Observable<BtwPercentage> getBtwModel() {
		return this.btwModel;
	}
}
