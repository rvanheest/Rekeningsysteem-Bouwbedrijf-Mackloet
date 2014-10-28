package org.rekeningsysteem.ui.offerte;

import java.util.Currency;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.PropertyFactuurnummerManagerModule;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;
import org.rekeningsysteem.ui.AbstractRekeningController;

import rx.Observable;

import com.google.inject.Guice;

public class OfferteController extends AbstractRekeningController {

	private final Observable<Offerte> model;
	private final OfferteHeaderController headerController;

	public OfferteController() {
		this(Currency.getInstance("EUR"));
	}

	public OfferteController(Currency currency) {
		this(new OfferteHeaderController(), new TextPaneController());
	}

	public OfferteController(Observable<Offerte> input) {
		this(new OfferteHeaderController(input.map(Offerte::getFactuurHeader),
				input.map(Offerte::isOndertekenen)),
				new TextPaneController(input.map(Offerte::getTekst)));
	}

	public OfferteController(OfferteHeaderController header, TextPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()));
		this.model = Observable.combineLatest(header.getModel(), body.getModel(),
				header.getOndertekenen(), Offerte::new);
		this.headerController = header;
	}

	@Override
	public Observable<Offerte> getModel() {
		return this.model;
	}

	@Override
	public void initFactuurnummer() {
		FactuurnummerManager manager = Guice.createInjector(new PropertyFactuurnummerManagerModule(), new ConsoleLoggerModule(), new ConfigPropertiesModule())
				.getInstance(FactuurnummerManagerFactory.class).create(PropertyModelEnum.OFFERTENUMMER);
		String offertenummer = manager.getFactuurnummer();
		this.headerController.initFactuurnummer(Observable.from(offertenummer));
	}
}
