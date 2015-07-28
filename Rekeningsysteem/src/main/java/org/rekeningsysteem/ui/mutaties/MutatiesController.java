package org.rekeningsysteem.ui.mutaties;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;

import rx.Observable;

public class MutatiesController extends AbstractRekeningController<MutatiesFactuur> {

	private final FactuurHeaderController header;
	private final MutatiesListPaneController list;

	public MutatiesController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public MutatiesController(PropertiesWorker properties, Database database) {
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")), database);
	}

	public MutatiesController(Currency currency, Database database) {
		this(new FactuurHeaderController(new FactuurHeader(new Debiteur("Woongoed GO",
				"Landbouwweg", "1", "3241MV", "Middelharnis", "NL.0025.45.094.B.01"),
				LocalDate.now()), database), new MutatiesListPaneController(currency));
	}

	public MutatiesController(MutatiesFactuur input, Database database) {
		this(new FactuurHeaderController(input.getFactuurHeader(), database),
				new MutatiesListPaneController(input.getCurrency(), input.getItemList()));
	}

	public MutatiesController(FactuurHeaderController header, MutatiesListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						(head, list) -> new MutatiesFactuur(head, body.getCurrency(), list,
								new BtwPercentage(0.0, 0.0))));
		this.header = header;
		this.list = body;
	}

	public FactuurHeaderController getHeaderController() {
		return this.header;
	}

	public MutatiesListPaneController getListController() {
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
