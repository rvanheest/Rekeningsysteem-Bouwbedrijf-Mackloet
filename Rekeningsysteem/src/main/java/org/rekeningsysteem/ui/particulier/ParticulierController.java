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
		this(properties.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")),
				properties.getProperty(PropertyModelEnum.LOONBTWPERCENTAGE)
						.map(Double::parseDouble)
						.<BtwPercentage> flatMap(l -> properties
								.getProperty(PropertyModelEnum.MATERIAALBTWPERCENTAGE)
								.map(Double::parseDouble)
								.map(m -> new BtwPercentage(l, m)))
						.orElse(new BtwPercentage(6, 21)), database);
	}

	public ParticulierController(Currency currency, BtwPercentage btw, Database database) {
		this(new OmschrFactuurHeaderController(database), new ParticulierListPaneController(currency, btw),
				new LoonListPaneController(currency));
	}

	public ParticulierController(ParticulierFactuur input, Database database) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader(), database),
				new ParticulierListPaneController(input.getCurrency(), input.getItemList(), input.getBtwPercentage()),
				new LoonListPaneController(input.getCurrency(), input.getLoonList()));
	}

	public ParticulierController(OmschrFactuurHeaderController header,
			ParticulierListPaneController body, LoonListPaneController loon) {
		super(new RekeningSplitPane(header.getUI(), body.getUI(), loon.getUI()),
				Observable.combineLatest(header.getModel(), body.getListModel(),
						loon.getModel(), body.getBtwModel(),
						(head, list, loonList, btw) -> new ParticulierFactuur(head, body.getCurrency(), list, loonList, btw)));
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
