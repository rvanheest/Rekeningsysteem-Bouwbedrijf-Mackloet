package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;

import rx.Observable;

// TODO ParticulierController
public class Particulier2Controller extends AbstractRekeningController<ParticulierFactuur2> {

	private final OmschrFactuurHeaderController header;
	private final ParticulierListPaneController2 list;

	public Particulier2Controller(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public Particulier2Controller(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), getDefaultBtwPercentage(properties), database);
	}

	public Particulier2Controller(Currency currency, BtwPercentage defaultBtw, Database database) {
		this(new OmschrFactuurHeaderController(database),
				new ParticulierListPaneController2(currency, database, defaultBtw));
	}

	public Particulier2Controller(ParticulierFactuur2 input, PropertiesWorker properties,
			Database database) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader(), database),
				new ParticulierListPaneController2(input.getCurrency(), database,
						getDefaultBtwPercentage(properties), input.getItemList()));
	}

	public Particulier2Controller(OmschrFactuurHeaderController header,
			ParticulierListPaneController2 body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						(head, list) -> new ParticulierFactuur2(head, body.getCurrency(),
								list)));
		this.header = header;
		this.list = body;
	}

	public OmschrFactuurHeaderController getHeaderController() {
		return this.header;
	}

	public ParticulierListPaneController2 getListController() {
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
