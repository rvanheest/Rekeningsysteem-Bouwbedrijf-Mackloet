package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.loon.LoonListPane.LoonModel;

public class LoonListController extends AbstractListController<AbstractLoon, LoonModel> {

	public LoonListController(Currency currency) {
		this(currency, new LoonListPane());
	}

	public LoonListController(Currency currency, List<AbstractLoon> input) {
		this(currency);
		this.getUI().setData(this.modelToUI(input));
	}

	public LoonListController(Currency currency, LoonListPane ui) {
		super(currency, ui, LoonController::new);
	}

	@Override
	protected List<LoonModel> modelToUI(List<AbstractLoon> list) {
		return list.stream().map(item -> {
			if (item instanceof InstantLoon) {
				InstantLoon loon = (InstantLoon) item;
				return new LoonModel(loon.getOmschrijving(), "", null, loon.getLoon().getBedrag());
			}
			else {
				assert item instanceof ProductLoon;
				ProductLoon loon = (ProductLoon) item;
				return new LoonModel(loon.getOmschrijving(), String.valueOf(loon.getUren()),
						loon.getUurloon(), loon.getLoon().getBedrag());
			}
		}).collect(Collectors.toList());
	}

	@Override
	protected ItemList<AbstractLoon> uiToModel(List<? extends LoonModel> list) {
		return list.stream().map(item -> {
			String omschrijving = item.getOmschrijving();
			String uren = item.getUren();
			Geld uurloon = item.getUurloon();
			Geld loon = new Geld(item.getLoon());
			
			if (uurloon == null) {
				return new InstantLoon(omschrijving, loon);
			}
			else {
				return new ProductLoon(omschrijving, Double.parseDouble(uren), uurloon);
			}
		}).collect(Collectors.toCollection(ItemList::new));
	}
}
