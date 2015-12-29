package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.ParticulierListPane2.ParticulierModel2;

// TODO ParticulierListController
public class ParticulierListController2 extends AbstractListController<ParticulierArtikel2, ParticulierModel2> {

	public ParticulierListController2(Currency currency, Database db, BtwPercentage defaultBtw) {
		this(currency, db, defaultBtw, new ParticulierListPane2());
	}

	public ParticulierListController2(Currency currency, Database db, BtwPercentage defaultBtw,
			List<ParticulierArtikel2> input) {
		this(currency, db, defaultBtw);
		this.getUI().setData(this.modelToUI(input));
	}

	public ParticulierListController2(Currency currency, Database db, BtwPercentage defaultBtw,
			ParticulierListPane2 ui) {
		super(currency, db, defaultBtw, ui, ParticulierArtikel2Controller::new);
	}

	@Override
	protected List<ParticulierModel2> modelToUI(List<ParticulierArtikel2> list) {
		return list.stream().map(item -> {
			if (item instanceof EsselinkParticulierArtikel) {
				EsselinkParticulierArtikel artikel = (EsselinkParticulierArtikel) item;
				return new ParticulierModel2(artikel);
			}
			else if (item instanceof ParticulierArtikel2Impl) {
    			ParticulierArtikel2Impl artikel = (ParticulierArtikel2Impl) item;
    			return new ParticulierModel2(artikel);
			}
			else if (item instanceof InstantLoon2) {
				InstantLoon2 loon = (InstantLoon2) item;
				return new ParticulierModel2(loon);
			}
			else {
				assert item instanceof ProductLoon2;
				ProductLoon2 loon = (ProductLoon2) item;
				return new ParticulierModel2(loon);
			}
		}).collect(Collectors.toList());
	}

	@Override
	protected ItemList<ParticulierArtikel2> uiToModel(List<? extends ParticulierModel2> list) {
		return list.stream().map(item -> {
			EsselinkParticulierArtikel esselink = item.getEsselink();
			ParticulierArtikel2Impl ander = item.getAnder();
			InstantLoon2 instant = item.getInstant();
			ProductLoon2 product = item.getProduct();

			if (esselink != null) {
				return esselink;
			}
			else if (ander != null) {
				return ander;
			}
			else if (instant != null) {
				return instant;
			}
			else {
				assert product != null;
				return product;
			}
		}).collect(Collectors.toCollection(ItemList::new));
	}
}
