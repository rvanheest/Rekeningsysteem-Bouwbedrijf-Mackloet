package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.ui.list.AbstractListItemController;

import rx.Observable;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel> {

	public ParticulierArtikelController(Currency currency) {
		this(new ParticulierArtikelPane(currency));
	}

	public ParticulierArtikelController(Currency currency, AnderArtikel input) {
		this(currency);
		this.getUI().setAnderArtikel(input);
	}

	public ParticulierArtikelController(ParticulierArtikelPane ui) {
		super(ui, Observable.merge(
				ui.getType().flatMap(type -> {
					switch (type) {
						case ESSELINK:
							return ui.getGebruiktEsselinkArtikel();
						case ANDER:
							return ui.getAnderArtikel();
						default:
							return null;
							// Does never happen!!!
					}
				}).sample(ui.getAddButtonEvent()).map(Optional::of),
				ui.getCancelButtonEvent().map(event -> Optional.<ParticulierArtikel> empty()))
				.first());
	}

	@Override
	public ParticulierArtikelPane getUI() {
		return (ParticulierArtikelPane) super.getUI();
	}
}
