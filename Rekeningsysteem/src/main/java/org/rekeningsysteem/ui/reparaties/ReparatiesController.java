package org.rekeningsysteem.ui.reparaties;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.properties.Optionals;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;

import rx.Observable;

public class ReparatiesController extends AbstractRekeningController<ReparatiesFactuur> {

	private final FactuurHeaderController headerController;

	public ReparatiesController() {
		this(PropertiesWorker.getInstance());
	}

	public ReparatiesController(PropertiesWorker properties) {
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")),
				Optionals.zip(properties.getProperty(PropertyModelEnum.LOONBTWPERCENTAGE)
						.map(Double::parseDouble),
						properties.getProperty(PropertyModelEnum.MATERIAALBTWPERCENTAGE)
								.map(Double::parseDouble), BtwPercentage::new)
						.orElse(new BtwPercentage(0.0, 0.0)));
	}

	public ReparatiesController(Currency currency, BtwPercentage btw) {
		this(new FactuurHeaderController(), new ReparatiesListPaneController(currency, btw));
	}

	public ReparatiesController(ReparatiesFactuur input) {
		this(new FactuurHeaderController(input.getFactuurHeader()),
				new ReparatiesListPaneController(input.getCurrency(), input.getItemList(),
						input.getBtwPercentage()));
	}

	public ReparatiesController(FactuurHeaderController header, ReparatiesListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						body.getBtwModel(), (head, list, btw) ->
						new ReparatiesFactuur(head, body.getCurrency(), list, btw)));
		this.headerController = header;
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
				.call(PropertyModelEnum.FACTUURNUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(factuurnummer));
	}
}
