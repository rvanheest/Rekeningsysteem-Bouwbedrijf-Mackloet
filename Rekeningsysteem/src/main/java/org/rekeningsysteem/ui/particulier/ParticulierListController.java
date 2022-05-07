package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.ParticulierListPane.ParticulierModel;

public class ParticulierListController extends AbstractListController<ParticulierArtikel, ParticulierModel, ParticulierArtikelController> {

	public ParticulierListController(Currency currency, Database db, BtwPercentages defaultBtw) {
		this(currency, db, defaultBtw, new ParticulierListPane());
	}

	public ParticulierListController(Database db, BtwPercentages defaultBtw, ItemList<ParticulierArtikel> input) {
		this(input.getCurrency(), db, defaultBtw, new ParticulierListPane());
		this.setData(this.modelToUI(input.stream()));
	}

	public ParticulierListController(Currency currency, Database db, BtwPercentages defaultBtw, ParticulierListPane ui) {
		super(ui, () -> new ParticulierArtikelController(currency, db, defaultBtw));
	}

	@Override
	protected List<ParticulierModel> modelToUI(Stream<ParticulierArtikel> stream) {
		return stream.map(item -> switch (item) {
			case GebruiktEsselinkArtikel gebruiktEsselinkArtikel -> new ParticulierModel(gebruiktEsselinkArtikel);
			case AnderArtikel anderArtikel -> new ParticulierModel(anderArtikel);
			case InstantLoon instantLoon -> new ParticulierModel(instantLoon);
			case ProductLoon productLoon -> new ParticulierModel(productLoon);
			default -> throw new RuntimeException();
		}).collect(Collectors.toList());
	}

	@Override
	protected List<ParticulierArtikel> uiToModel(List<? extends ParticulierModel> list) {
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
		}).collect(Collectors.toList());
	}
}
