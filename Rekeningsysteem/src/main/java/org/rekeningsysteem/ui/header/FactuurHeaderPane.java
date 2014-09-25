package org.rekeningsysteem.ui.header;

import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.offerte.OndertekenenPane;

public class FactuurHeaderPane extends WorkingPane {

	public FactuurHeaderPane(DebiteurPane debiteur,
			DatumPane datum, FactuurnummerPane factuurnummer) {
		super(debiteur, datum, factuurnummer);
	}

	public FactuurHeaderPane(DebiteurPane debiteur, DatumPane datum,
			FactuurnummerPane factuurnummer, OmschrijvingPane omschrijving) {
		super(debiteur, datum, factuurnummer, omschrijving);
	}

	public FactuurHeaderPane(DebiteurPane debiteur, DatumPane datum,
			FactuurnummerPane factuurnummer, OndertekenenPane ondertekenen) {
		super(debiteur, datum, factuurnummer, ondertekenen);
	}

	@Override
	public String getTitle() {
		return "Debiteur & Datum";
	}
}
