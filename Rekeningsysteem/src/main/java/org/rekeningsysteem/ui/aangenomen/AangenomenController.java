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

@Deprecated
public class AangenomenController extends AbstractRekeningController<AangenomenFactuur> {

	private final OmschrFactuurHeaderController header;
	private final AangenomenListPaneController list;

	public AangenomenController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public AangenomenController(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), getDefaultBtwPercentage(properties), database);
	}

	public AangenomenController(Currency currency, BtwPercentage defaultBtw, Database database) {
		this(new OmschrFactuurHeaderController(database),
				new AangenomenListPaneController(currency, defaultBtw));
	}

	public AangenomenController(AangenomenFactuur input, PropertiesWorker properties, Database database) {
		this(new OmschrFactuurHeaderController(input.getFactuurHeader(), database),
				new AangenomenListPaneController(input.getCurrency(),
						getDefaultBtwPercentage(properties), input.getItemList()));
	}

	public AangenomenController(OmschrFactuurHeaderController header,
			AangenomenListPaneController body) {
		super(new RekeningSplitPane(header.getUI(), body.getUI()),
		Observable.combineLatest(header.getModel(), body.getListModel(),
				(head, list) -> new AangenomenFactuur(head, body.getCurrency(), list)));
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
