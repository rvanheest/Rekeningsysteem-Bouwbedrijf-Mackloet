package org.rekeningsysteem.ui.aangenomen;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;

import rx.Observable;

public class AangenomenController extends AbstractRekeningController<AangenomenFactuur> {

	private final OmschrFactuurHeaderController header;
	private final AangenomenListPaneController list;

	public AangenomenController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public AangenomenController(PropertiesWorker properties, Database database) {
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

	public AangenomenController(Currency currency, BtwPercentage btw, Database database) {
		this(new OmschrFactuurHeaderController(database), new AangenomenListPaneController(currency, btw));
	}

	public AangenomenController(AangenomenFactuur input, Database database) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader(), database),
				new AangenomenListPaneController(input.getCurrency(), input.getItemList(),
						input.getBtwPercentage()));
	}

	public AangenomenController(OmschrFactuurHeaderController header,
			AangenomenListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
		Observable.combineLatest(header.getModel(), body.getListModel(),
				body.getBtwModel(),
				(head, list, btw) -> new AangenomenFactuur(head, body.getCurrency(), list, btw)));
		this.header = header;
		this.list = body;
	}

	public OmschrFactuurHeaderController getHeaderController() {
		return this.header;
	}

	public AangenomenListPaneController getListController() {
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
