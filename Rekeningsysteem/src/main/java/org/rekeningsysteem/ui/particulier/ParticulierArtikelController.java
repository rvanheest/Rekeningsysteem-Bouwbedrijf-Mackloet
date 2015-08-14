package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.Optional;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.ui.list.AbstractListItemController;

public class ParticulierArtikelController extends AbstractListItemController<ParticulierArtikel> {

	public ParticulierArtikelController(Currency currency, BtwPercentage defaultBtw) {
		this(new ParticulierArtikelPane(currency), defaultBtw);
	}

	public ParticulierArtikelController(Currency currency, BtwPercentage defaultBtw,
			AnderArtikel input) {
		this(currency, defaultBtw);
		this.getUI().setAnderArtikel(input);
	}

	public ParticulierArtikelController(ParticulierArtikelPane ui, BtwPercentage defaultBtw) {
		super(ui, ui.getType().flatMap(type -> {
			switch (type) {
				case ESSELINK:
					return ui.getGebruiktEsselinkArtikel();
				case ANDER:
					return ui.getAnderArtikel();
				default:
					return null;
					// Does never happen!!!
			}
		}).sample(ui.getAddButtonEvent()).map(Optional::of)
				.mergeWith(ui.getCancelButtonEvent().map(event -> Optional.empty()))
				.first());
		ui.setBtwPercentage(defaultBtw.getMateriaalPercentage());
	}

	@Override
	public ParticulierArtikelPane getUI() {
		return (ParticulierArtikelPane) super.getUI();
	}
}
