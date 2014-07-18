package org.rekeningsysteem.logic.factuurnummer;

import java.util.Optional;

import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class PropertyFactuurnummerManager implements FactuurnummerManager {

	private final PropertiesWorker worker;
	private final PropertyKey key;
	private Optional<String> factNr;

	@Inject
	public PropertyFactuurnummerManager(PropertiesWorker worker,
			@Assisted PropertyKey key) {
		this.worker = worker;
		this.key = key;
		this.factNr = Optional.empty();
	}

	@Override
	public Optional<String> getFactuurnummer() {
		if (!this.factNr.isPresent()) {
			Optional<String> nr = this.worker.getProperty(this.key);
			String yearNow = String.valueOf(new Datum().getJaar());
			if (nr.map(s -> s.endsWith(yearNow)).orElse(false)) {
				// same year
				nr.map(s -> s.substring(0, s.indexOf(yearNow)))
						.map(Integer::parseInt)
						.map(i -> i + 1)
						.map(String::valueOf)
						.map(s -> s.concat(yearNow))
						.ifPresent(s -> this.worker.setProperty(this.key, s));
				this.factNr = nr;
			}
			else if (nr.isPresent()) {
				// other year
				this.factNr = Optional.of("1".concat(yearNow));
				this.factNr.ifPresent(s -> this.worker.setProperty(this.key, s));
			}
			return this.factNr;
		}
		return this.factNr;
	}

//	@Override
//	public Optional<String> getFactuurnummer() {
//		if (this.factNr == null) {
//			String factnr = this.worker.getProperty(this.key);
//			String yearNow = String.valueOf(new Datum().getJaar());
//			String newFnr;
//			if (factnr.endsWith(yearNow)) {
//				// same year
//				String[] oldNr = factnr.split(yearNow);
//				int newNumber = Integer.parseInt(oldNr[0]) + 1;
//				newFnr = String.valueOf(newNumber) + yearNow;
//			}
//			else {
//				// other year
//				newFnr = "1" + yearNow;
//				factnr = newFnr;
//			}
//			this.worker.setProperty(this.key, newFnr);
//			this.factNr = factnr;
//
//			return factnr;
//		}
//		return this.factNr;
//	}
}
