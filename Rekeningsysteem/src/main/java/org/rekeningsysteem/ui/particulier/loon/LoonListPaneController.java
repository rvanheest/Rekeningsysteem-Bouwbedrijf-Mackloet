package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.List;

import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;

public class LoonListPaneController extends WorkingPaneController {
	
	private final Currency currency;
	private final Observable<ItemList<AbstractLoon>> listModel;

	public LoonListPaneController(Currency currency) {
		this(new LoonListController(currency), currency);
	}

	public LoonListPaneController(Currency currency, List<AbstractLoon> input) {
		this(new LoonListController(currency, input), currency);
	}

	public LoonListPaneController(LoonListController contr, Currency currency) {
		super(new LoonWorkingPane(contr.getUI()));
		
		this.currency = currency;
		this.listModel = contr.getModel();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public Observable<ItemList<AbstractLoon>> getModel() {
		return this.listModel;
	}
}
