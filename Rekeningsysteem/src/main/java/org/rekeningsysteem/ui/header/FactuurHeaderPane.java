package org.rekeningsysteem.ui.header;

import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.offerte.OndertekenenPane;

public class FactuurHeaderPane extends WorkingPane {

	private static final String title = "Debiteur & Datum";

	public FactuurHeaderPane(DebiteurPane debiteur, DatumPane datum, FactuurnummerPane factuurnummer) {
		super(title, debiteur, datum, factuurnummer);
	}

	public FactuurHeaderPane(DebiteurPane debiteur, DatumPane datum, FactuurnummerPane factuurnummer, OmschrijvingPane omschrijving) {
		super(title, debiteur, datum, factuurnummer, omschrijving);
	}

	public FactuurHeaderPane(DebiteurPane debiteur, DatumPane datum, FactuurnummerPane factuurnummer, OndertekenenPane ondertekenen) {
		super(title, debiteur, datum, factuurnummer, ondertekenen);
	}
}
