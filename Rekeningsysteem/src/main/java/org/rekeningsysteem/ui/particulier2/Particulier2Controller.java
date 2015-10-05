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

public class Particulier2Controller extends AbstractRekeningController<ParticulierFactuur2> {

	private final OmschrFactuurHeaderController header;

	public Particulier2Controller(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public Particulier2Controller(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), getDefaultBtwPercentage(properties), database);
	}

	public Particulier2Controller(Currency currency, BtwPercentage defaultBtw, Database database) {
		this(new OmschrFactuurHeaderController(database));
	}

	public Particulier2Controller(OmschrFactuurHeaderController header /* add others */) {
		super(new RekeningSplitPane(header.getUI()),
				header.getModel().map(head -> new ParticulierFactuur2(head, null, null)));
		// TODO others
		
		this.header = header;
	}

	public OmschrFactuurHeaderController getHeaderController() {
		return this.header;
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
