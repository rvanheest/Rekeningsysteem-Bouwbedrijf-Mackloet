package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier2.ParticulierListPane2.ParticulierModel2;

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
				EsselinkArtikel art = artikel.getArtikel();
				return new ParticulierModel2(art.getOmschrijving(),
						artikel.getMateriaal().getBedrag(), artikel.getMateriaalBtwPercentage(),
						artikel.getAantal(), art);
			}
			assert item instanceof ParticulierArtikel2Impl;
			ParticulierArtikel2Impl artikel = (ParticulierArtikel2Impl) item;
			return new ParticulierModel2(artikel.getOmschrijving(),
					artikel.getMateriaal().getBedrag(), artikel.getMateriaalBtwPercentage());
		}).collect(Collectors.toList());
	}

	@Override
	protected ItemList<ParticulierArtikel2> uiToModel(List<? extends ParticulierModel2> list) {
		return list.stream().map(item -> {
			String omschrijving = item.getOmschrijving();
			Geld materiaal = new Geld(item.getMateriaal());
			double btwPercentage = item.getMateriaalBtwPercentage();
			double aantal = item.getAantal();
			EsselinkArtikel artikel = item.getArtikel();
			
			if (artikel == null) {
				// it must be a AnderArtikel
				return new ParticulierArtikel2Impl(omschrijving, materiaal, btwPercentage);
			}
			else {
				return new EsselinkParticulierArtikel(artikel, aantal, btwPercentage);
			}
		}).collect(Collectors.toCollection(ItemList::new));
	}
}
