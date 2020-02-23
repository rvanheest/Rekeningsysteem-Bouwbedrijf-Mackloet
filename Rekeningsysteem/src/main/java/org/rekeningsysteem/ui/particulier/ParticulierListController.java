package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.ParticulierListPane.ParticulierModel;

public class ParticulierListController extends AbstractListController<ParticulierArtikel, ParticulierModel> {

	public ParticulierListController(Currency currency, Database db, BtwPercentages defaultBtw) {
		this(currency, db, defaultBtw, new ParticulierListPane());
	}

	public ParticulierListController(Currency currency, Database db, BtwPercentages defaultBtw,
			List<ParticulierArtikel> input) {
		this(currency, db, defaultBtw);
		this.getUI().setData(this.modelToUI(input));
	}

	public ParticulierListController(Currency currency, Database db, BtwPercentages defaultBtw,
			ParticulierListPane ui) {
		super(currency, db, defaultBtw, ui, ParticulierArtikelController::new);
	}

	@Override
	protected List<ParticulierModel> modelToUI(List<ParticulierArtikel> list) {
		return list.stream().map(item -> {
			if (item instanceof GebruiktEsselinkArtikel) {
				GebruiktEsselinkArtikel artikel = (GebruiktEsselinkArtikel) item;
				return new ParticulierModel(artikel);
			}
			else if (item instanceof AnderArtikel) {
    			AnderArtikel artikel = (AnderArtikel) item;
    			return new ParticulierModel(artikel);
			}
			else if (item instanceof InstantLoon) {
				InstantLoon loon = (InstantLoon) item;
				return new ParticulierModel(loon);
			}
			else {
				assert item instanceof ProductLoon;
				ProductLoon loon = (ProductLoon) item;
				return new ParticulierModel(loon);
			}
		}).collect(Collectors.toList());
	}

	@Override
	protected ItemList<ParticulierArtikel> uiToModel(List<? extends ParticulierModel> list) {
		return list.stream().map(item -> {
			GebruiktEsselinkArtikel esselink = item.getEsselink();
			AnderArtikel ander = item.getAnder();
			InstantLoon instant = item.getInstant();
			ProductLoon product = item.getProduct();

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
