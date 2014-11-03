package org.rekeningsysteem.ui.offerte;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;

import rx.Observable;

public class OfferteController extends AbstractRekeningController {

	private final Observable<Offerte> model;
	private final OfferteHeaderController headerController;

	public OfferteController() {
		this(Currency.getInstance("EUR"));
	}

	public OfferteController(Currency currency) {
		this(new OfferteHeaderController(), new TextPaneController());
	}

	public OfferteController(Offerte input) {
		this(new OfferteHeaderController(input.getFactuurHeader(), input.isOndertekenen()),
				new TextPaneController(input.getTekst()));
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
		String offertenummer = this.getFactuurnummerFactory()
				.create(PropertyModelEnum.OFFERTENUMMER)
				.getFactuurnummer();
		this.headerController.initFactuurnummer(Optional.ofNullable(offertenummer));
	}
}
