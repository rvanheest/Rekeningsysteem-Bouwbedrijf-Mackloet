package org.rekeningsysteem.ui.reparaties;

import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.FactuurHeaderController;
import org.rekeningsysteem.ui.list.ListPaneController;

public class ReparatiesController extends AbstractRekeningController<ReparatiesFactuur> {

	private final FactuurHeaderController header;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public ReparatiesController(Database database) {
		this(
			PropertiesWorker.getInstance()
				.getProperty(PropertyModelEnum.VALUTAISO4217)
				.map(Currency::getInstance)
				.orElse(Currency.getInstance("EUR")),
			database
		);
	}

	public ReparatiesController(Currency currency, Database database) {
		this(
			new FactuurHeaderController(
				new FactuurHeader(
					new Debiteur("Woongoed GO", "Landbouwweg", "1", "3241MV", "Middelharnis", "NL.0025.45.094.B.01"),
					LocalDate.now()
				),
				database
			),
			new ListPaneController<>(new ReparatiesListController(currency), currency)
		);
	}

	public ReparatiesController(ReparatiesFactuur input, Database database) {
		this(
			new FactuurHeaderController(input.getFactuurHeader(), database),
			new ListPaneController<>(new ReparatiesListController(input.getCurrency(), input.getItemList()), input.getCurrency())
		);
	}

	public ReparatiesController(FactuurHeaderController header, ListPaneController<ReparatiesInkoopOrder> body) {
		super(
			new RekeningSplitPane(header.getUI(), body.getUI()),
			Observable.combineLatest(
				header.getModel(),
				body.getListModel(),
				(head, list) -> new ReparatiesFactuur(head, body.getCurrency(), list)
			)
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
