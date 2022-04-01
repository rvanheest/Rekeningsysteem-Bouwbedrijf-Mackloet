package org.rekeningsysteem.ui.offerte;

import java.util.Optional;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.AbstractRekeningController;

public class OfferteController extends AbstractRekeningController<Offerte> {

	private final OfferteHeaderController header;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public OfferteController(Database database, Logger logger) {
		this(new OfferteHeaderController(database), new TextPaneController(logger));
	}

	public OfferteController(Offerte input, Database database) {
		this(
			new OfferteHeaderController(input.getFactuurHeader(), input.isOndertekenen(), database),
			new TextPaneController(input.getTekst())
		);
	}

	public OfferteController(OfferteHeaderController header, TextPaneController textPane) {
		super(
			new RekeningSplitPane(header.getUI(), textPane.getUI()),
			Observable.combineLatest(
				header.getModel(),
				textPane.getModel(),
				header.getOndertekenenModel(),
				Offerte::new
			)
		);
		this.header = header;
		this.disposable.addAll(header, textPane);
	}

	@Override
	public void initFactuurnummer() {
		String offertenummer = this.getFactuurnummerFactory()
			.apply(PropertyModelEnum.OFFERTENUMMER, PropertyModelEnum.OFFERTENUMMER_KENMERK)
			.getFactuurnummer();
		this.header.getOffertenummerController().setFactuurnummer(Optional.ofNullable(offertenummer));
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
