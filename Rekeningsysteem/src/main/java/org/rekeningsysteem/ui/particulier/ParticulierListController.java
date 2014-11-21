package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.ParticulierListPane.ParticulierModel;

public class ParticulierListController extends AbstractListController<ParticulierArtikel, ParticulierModel> {

	public ParticulierListController(Currency currency) {
		this(currency, new ParticulierListPane());
	}

	public ParticulierListController(Currency currency, List<ParticulierArtikel> input) {
		this(currency);
		this.getUI().setData(this.modelToUI(input));
	}

	public ParticulierListController(Currency currency, ParticulierListPane ui) {
		super(currency, ui, ParticulierArtikelController::new);
	}
	
	@Override
	protected List<ParticulierModel> modelToUI(List<ParticulierArtikel> list) {
		return list.stream().map(item -> {
			if (item instanceof GebruiktEsselinkArtikel) {
				GebruiktEsselinkArtikel artikel = (GebruiktEsselinkArtikel) item;
				EsselinkArtikel art = artikel.getArtikel();
				return new ParticulierModel(art.getArtikelNummer(), art.getOmschrijving(),
						String.valueOf(art.getPrijsPer()), art.getEenheid(), art.getVerkoopPrijs().getBedrag(),
						String.valueOf(artikel.getAantal()));
			}
			else {
				assert item instanceof AnderArtikel;
				AnderArtikel artikel = (AnderArtikel) item;
				return new ParticulierModel("", artikel.getOmschrijving(), "", "",
						artikel.getMateriaal().getBedrag(), "");
			}
		}).collect(Collectors.toList());
	}
	
	@Override
	protected ItemList<ParticulierArtikel> uiToModel(List<? extends ParticulierModel> list) {
		return list.stream().map(item -> {
			String artikelNummer = item.getArtikelNummer();
			String omschrijving = item.getOmschrijving();
			String prijsPer = item.getPrijsPer();
			String eenheid = item.getEenheid();
			Geld verkoopPrijs = new Geld(item.getVerkoopPrijs());
			String aantal = item.getAantal();
			
			if ("".equals(artikelNummer)) {
				return new AnderArtikel(omschrijving, verkoopPrijs);
			}
			else {
				return new GebruiktEsselinkArtikel(new EsselinkArtikel(artikelNummer, omschrijving,
						Integer.parseInt(prijsPer), eenheid, verkoopPrijs),
						Double.parseDouble(aantal));
			}
		}).collect(Collectors.toCollection(ItemList::new));
	}
}
