package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.ui.list.AbstractListController;
import org.rekeningsysteem.ui.particulier.loon.LoonListPane.LoonModel;

@Deprecated
public class LoonListController extends AbstractListController<AbstractLoon, LoonModel> {

	public LoonListController(Currency currency, BtwPercentage defaultBtw) {
		this(currency, defaultBtw, new LoonListPane());
	}

	public LoonListController(Currency currency, BtwPercentage defaultBtw,
			List<AbstractLoon> input) {
		this(currency, defaultBtw);
		this.getUI().setData(this.modelToUI(input));
	}

	public LoonListController(Currency currency, BtwPercentage defaultBtw, LoonListPane ui) {
		super(currency, defaultBtw, ui, LoonController::new);
	}

	@Override
	protected List<LoonModel> modelToUI(List<AbstractLoon> list) {
		return list.stream().map(item -> {
			if (item instanceof InstantLoon) {
				InstantLoon loon = (InstantLoon) item;
				return new LoonModel(loon.getOmschrijving(), "", null,
						loon.getLoon().getBedrag(), loon.getLoonBtwPercentage());
			}
			else {
				assert item instanceof ProductLoon;
				ProductLoon loon = (ProductLoon) item;
				return new LoonModel(loon.getOmschrijving(), String.valueOf(loon.getUren()),
						loon.getUurloon(), loon.getLoon().getBedrag(), loon.getLoonBtwPercentage());
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
			double percentage = item.getBtwPercentage();
			
			if (uurloon == null) {
				return new InstantLoon(omschrijving, loon, percentage);
			}
			else {
				return new ProductLoon(omschrijving, Double.parseDouble(uren),
						uurloon, percentage);
			}
		}).collect(Collectors.toCollection(ItemList::new));
	}
}
