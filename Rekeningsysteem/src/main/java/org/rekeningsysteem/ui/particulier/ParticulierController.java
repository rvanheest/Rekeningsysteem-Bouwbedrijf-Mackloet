package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.particulier.loon.LoonListPaneController;

import rx.Observable;

public class ParticulierController extends AbstractRekeningController<ParticulierFactuur> {

	private final OmschrFactuurHeaderController header;
	private final ParticulierListPaneController list;
	private final LoonListPaneController loon;

	public ParticulierController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public ParticulierController(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), getDefaultBtwPercentage(properties), database);
	}

	public ParticulierController(Currency currency, BtwPercentage defaultBtw, Database database) {
		this(new OmschrFactuurHeaderController(database),
				new ParticulierListPaneController(currency, database, defaultBtw),
				new LoonListPaneController(currency, defaultBtw));
	}

	public ParticulierController(ParticulierFactuur input, PropertiesWorker properties,
			Database database) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader(), database),
				new ParticulierListPaneController(input.getCurrency(), database,
						getDefaultBtwPercentage(properties), input.getItemList()),
				new LoonListPaneController(input.getCurrency(),
						getDefaultBtwPercentage(properties), input.getLoonList()));
	}

	public ParticulierController(OmschrFactuurHeaderController header,
			ParticulierListPaneController body, LoonListPaneController loon) {
		super(new RekeningSplitPane(header.getUI(), body.getUI(), loon.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						loon.getListModel(),
						(head, list, loonList) -> new ParticulierFactuur(head, body.getCurrency(),
								list, loonList)));
		this.header = header;
		this.list = body;
		this.loon = loon;
	}

	public OmschrFactuurHeaderController getHeaderController() {
		return this.header;
	}

	public ParticulierListPaneController getListController() {
		return this.list;
	}

	public LoonListPaneController getLoonController() {
		return this.loon;
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
