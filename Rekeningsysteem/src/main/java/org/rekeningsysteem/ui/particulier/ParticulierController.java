package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.list.ListPaneController;

public class ParticulierController extends AbstractRekeningController<ParticulierFactuur> {

	private final OmschrFactuurHeaderController header;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public ParticulierController(Database database) {
		this(PropertiesWorker.getInstance(), database);
	}

	public ParticulierController(PropertiesWorker properties, Database database) {
		this(getDefaultCurrency(properties), getDefaultBtwPercentage(properties), database);
	}

	public ParticulierController(Currency currency, BtwPercentages defaultBtw, Database database) {
		this(
			new OmschrFactuurHeaderController(database),
			new ListPaneController<>(new ParticulierListController(currency, database, defaultBtw), currency)
		);
	}

	public ParticulierController(ParticulierFactuur input, PropertiesWorker properties, Database database) {
		this(
			new OmschrFactuurHeaderController(input.getFactuurHeader(), input.getOmschrijving(), database),
			new ListPaneController<>(new ParticulierListController(database, getDefaultBtwPercentage(properties), input.getItemList()), input.getItemList().getCurrency())
		);
	}

	public ParticulierController(OmschrFactuurHeaderController header, ListPaneController<ParticulierArtikel> body) {
		super(
			new RekeningSplitPane(header.getUI(), body.getUI()),
			Observable.combineLatest(
				header.getModel(),
				body.getListModel(),
				(head, list) -> new ParticulierFactuur(head.getLeft(), head.getRight(), list)
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
		return this.header.isDebiteurSaveSelected().firstElement();
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
