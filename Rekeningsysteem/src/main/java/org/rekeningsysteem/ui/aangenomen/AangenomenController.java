package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;

import org.rekeningsysteem.application.working.AbstractRekeningController;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.PropertyFactuurnummerManagerModule;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;

import rx.Observable;

import com.google.inject.Guice;

public class AangenomenController extends AbstractRekeningController {

	private final Observable<AangenomenFactuur> model;
	private final OmschrFactuurHeaderController headerController;

	public AangenomenController() {
		this(Currency.getInstance("EUR"));
	}

	public AangenomenController(Currency currency) {
		this(new OmschrFactuurHeaderController(), new AangenomenListPaneController(Observable.from(currency)));
	}

	public AangenomenController(Observable<AangenomenFactuur> input) {
		this(new OmschrFactuurHeaderController(input.map(AangenomenFactuur::getFactuurHeader)),
				new AangenomenListPaneController(input.map(AangenomenFactuur::getCurrency),
						input.map(AangenomenFactuur::getItemList),
						input.map(AangenomenFactuur::getBtwPercentage)));
	}

	public AangenomenController(OmschrFactuurHeaderController header,
			AangenomenListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()));
		this.model = body.getCurrency().flatMap(c -> Observable.combineLatest(header.getModel(),
				body.getListModel(), body.getBtwModel(),
				(head, list, btw) -> new AangenomenFactuur(head, c, list, btw)));
		this.headerController = header;
	}

	public Observable<AangenomenFactuur> getModel() {
		return this.model;
	}

	@Override
	public void initFactuurnummer() {
		FactuurnummerManager manager = Guice.createInjector(new PropertyFactuurnummerManagerModule(), new ConsoleLoggerModule(), new ConfigPropertiesModule())
				.getInstance(FactuurnummerManagerFactory.class).create(PropertyModelEnum.FACTUURNUMMER);
		String factuurnummer = manager.getFactuurnummer();
		this.headerController.initFactuurnummer(Observable.from(factuurnummer));
	}
}
