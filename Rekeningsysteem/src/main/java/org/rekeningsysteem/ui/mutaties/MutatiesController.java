package org.rekeningsysteem.ui.mutaties;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;
import org.rekeningsysteem.ui.list.ListPaneController;

public class MutatiesController extends AbstractRekeningController<MutatiesFactuur> {

	private final FactuurHeaderController header;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public MutatiesController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public MutatiesController(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), database);
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
			new ListPaneController<>(new MutatiesListController(currency), currency)
		);
	}

	public MutatiesController(MutatiesFactuur input, Database database) {
		this(
			new FactuurHeaderController(input.header(), database),
			new ListPaneController<>(new MutatiesListController(input.itemList()), input.itemList().getCurrency())
		);
	}

	public MutatiesController(FactuurHeaderController header, ListPaneController<MutatiesInkoopOrder> body) {
		super(
			new RekeningSplitPane(header.getUI(), body.getUI()),
			Observable.combineLatest(header.getModel(), body.getListModel(), MutatiesFactuur::new)
		);
		this.header = header;
		this.disposable.addAll(header, body);
	}

	@Override
	public void initFactuurnummer() {
		String factuurnummer = this.getFactuurnummerFactory()
			.apply(PropertyModelEnum.FACTUURNUMMER, PropertyModelEnum.FACTUURNUMMER_KENMERK)
			.getFactuurnummer();
		this.header.setFactuurnummer(Optional.ofNullable(factuurnummer));
	}

	@Override
	public Maybe<Boolean> getSaveSelected() {
		return this.header.getDebiteurController().isSaveSelected().firstElement();
	}

	@Override
	public boolean isDisposed() {
		return super.isDisposed() && this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		super.dispose();
		this.disposable.dispose();
	}
}
