package org.rekeningsysteem.ui.reparaties;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;

import rx.Observable;

public class ReparatiesController extends AbstractRekeningController<ReparatiesFactuur> {

	private final FactuurHeaderController header;
	private final ReparatiesListPaneController list;

	public ReparatiesController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public ReparatiesController(PropertiesWorker properties, Database database) {
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")), database);
	}

	public ReparatiesController(Currency currency, Database database) {
		this(new FactuurHeaderController(new FactuurHeader(new Debiteur("Woongoed GO",
				"Landbouwweg", "1", "3241MV", "Middelharnis", "NL.0025.45.094.B.01"),
				LocalDate.now()), database), new ReparatiesListPaneController(currency));
	}

	public ReparatiesController(ReparatiesFactuur input, Database database) {
		this(new FactuurHeaderController(input.getFactuurHeader(), database),
				new ReparatiesListPaneController(input.getCurrency(), input.getItemList()));
	}

	public ReparatiesController(FactuurHeaderController header, ReparatiesListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						(head, list) -> new ReparatiesFactuur(head, body.getCurrency(), list,
								new BtwPercentage(0.0, 0.0))));
		this.header = header;
		this.list = body;
	}

	public FactuurHeaderController getHeaderController() {
		return this.header;
	}

	public ReparatiesListPaneController getListController() {
		return this.list;
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
				.call(PropertyModelEnum.FACTUURNUMMER)
				.getFactuurnummer();
		this.header.getFactuurnummerController()
				.getUI()
				.setFactuurnummer(Optional.ofNullable(factuurnummer));
	}

	@Override
	public Observable<Boolean> getSaveSelected() {
		return this.header.getDebiteurController().isSaveSelected().first();
	}
}
