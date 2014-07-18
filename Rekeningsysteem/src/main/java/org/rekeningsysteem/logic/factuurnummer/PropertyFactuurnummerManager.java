package org.rekeningsysteem.logic.factuurnummer;

import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PropertyFactuurnummerManager implements FactuurnummerManager {

	private final PropertiesWorker worker;
	private final PropertyKey key;
	private String factNr;

	@Inject
	public PropertyFactuurnummerManager(PropertiesWorker worker,
			@Assisted PropertyKey key) {
		this.worker = worker;
		this.key = key;
	}

	@Override
	public String getFactuurnummer() {
		if (this.factNr == null) {
			String factnr = this.worker.getProperty(this.key);
			String yearNow = String.valueOf(new Datum().getJaar());
			String newFnr;
			if (factnr.endsWith(yearNow)) {
				// same year
				String[] oldNr = factnr.split(yearNow);
				int newNumber = Integer.parseInt(oldNr[0]) + 1;
				newFnr = String.valueOf(newNumber) + yearNow;
			}
			else {
				// other year
				newFnr = "1" + yearNow;
				factnr = newFnr;
			}
			this.worker.setProperty(this.key, newFnr);
			this.factNr = factnr;

			return factnr;
		}
		return this.factNr;
	}
}
