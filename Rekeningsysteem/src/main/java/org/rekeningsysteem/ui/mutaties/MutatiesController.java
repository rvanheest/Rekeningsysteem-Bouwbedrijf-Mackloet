package org.rekeningsysteem.ui.mutaties;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;

public class MutatiesController extends AbstractRekeningController<MutatiesFactuur> {

	private final FactuurHeaderController header;
	private final MutatiesListPaneController list;

	public MutatiesController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public MutatiesController(PropertiesWorker properties, Database database) {
		this(
			properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")),
			database
		);
	}

	public MutatiesController(Currency currency, Database database) {
		this(
			new FactuurHeaderController(
				new FactuurHeader(
					new Debiteur("Woongoed GO", "Landbouwweg", "1", "3241MV", "Middelharnis", "NL.0025.45.094.B.01"),
					LocalDate.now()
				),
				database
			),
			new MutatiesListPaneController(currency)
		);
	}

	public MutatiesController(MutatiesFactuur input, Database database) {
		this(
			new FactuurHeaderController(input.getFactuurHeader(), database),
			new MutatiesListPaneController(input.getCurrency(), input.getItemList())
		);
	}

	public MutatiesController(FactuurHeaderController header, MutatiesListPaneController body) {
		super(
			new RekeningSplitPane(header.getUI(), body.getUI()),
			Observable.combineLatest(
				header.getModel(),
				body.getListModel(),
				(head, list) -> new MutatiesFactuur(head, body.getCurrency(), list)
			)
		);
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
			.apply(PropertyModelEnum.FACTUURNUMMER, PropertyModelEnum.FACTUURNUMMER_KENMERK)
			.getFactuurnummer();
		this.header.getFactuurnummerController()
			.getUI()
			.setFactuurnummer(Optional.ofNullable(factuurnummer));
	}

	@Override
	public Maybe<Boolean> getSaveSelected() {
		return this.header.getDebiteurController().isSaveSelected().firstElement();
	}
}
