package org.rekeningsysteem.ui.offerte;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;

import rx.Observable;

public class OfferteController extends AbstractRekeningController<Offerte> {

	private final OfferteHeaderController headerController;

	public OfferteController() {
		this(PropertiesWorker.getInstance());
	}

	public OfferteController(PropertiesWorker properties) {
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")));
	}

	public OfferteController(Currency currency) {
		this(new OfferteHeaderController(), new TextPaneController());
	}

	public OfferteController(Offerte input) {
		this(new OfferteHeaderController(input.getFactuurHeader(), input.isOndertekenen()),
				new TextPaneController(input.getTekst()));
	}

	public OfferteController(OfferteHeaderController header, TextPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getModel(),
				header.getOndertekenen(), Offerte::new));
		this.headerController = header;
	}

	@Override
	public void initFactuurnummer() {
		String offertenummer = this.getFactuurnummerFactory()
				.call(PropertyModelEnum.OFFERTENUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(offertenummer));
	}
}
