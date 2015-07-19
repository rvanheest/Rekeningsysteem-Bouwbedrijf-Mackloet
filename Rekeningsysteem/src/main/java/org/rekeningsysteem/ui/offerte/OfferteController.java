package org.rekeningsysteem.ui.offerte;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;

import rx.Observable;

public class OfferteController extends AbstractRekeningController<Offerte> {

	private final OfferteHeaderController header;
	private final TextPaneController textPane;

	public OfferteController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public OfferteController(PropertiesWorker properties, Database database) {
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")), database);
	}

	public OfferteController(Currency currency, Database database) {
		this(new OfferteHeaderController(database), new TextPaneController());
	}

	public OfferteController(Offerte input, Database database) {
		this(new OfferteHeaderController(input.getFactuurHeader(), input.isOndertekenen(), database),
				new TextPaneController(input.getTekst()));
	}

	public OfferteController(OfferteHeaderController header, TextPaneController textPane) {
		super(new RekeningSplitPane(header.getUI(), textPane.getUI()),
				Observable.combineLatest(header.getModel(), textPane.getModel(),
				header.getOndertekenenModel(), Offerte::new));
		this.header = header;
		this.textPane = textPane;
	}

	public OfferteHeaderController getHeaderController() {
		return this.header;
	}
	
	public TextPaneController getTextPaneController() {
		return this.textPane;
	}

	@Override
	public void initFactuurnummer() {
		String offertenummer = this.getFactuurnummerFactory()
				.call(PropertyModelEnum.OFFERTENUMMER)
				.getFactuurnummer();
		this.header.getOffertenummerController()
				.getUI()
				.setFactuurnummer(Optional.ofNullable(offertenummer));
	}

	@Override
	public Observable<Boolean> getSaveSelected() {
		return this.header.getDebiteurController().isSaveSelected().first();
	}
}
