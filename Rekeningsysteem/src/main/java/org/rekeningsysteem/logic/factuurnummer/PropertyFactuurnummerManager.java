package org.rekeningsysteem.logic.factuurnummer;

import java.time.LocalDate;
import java.util.Optional;

import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

public class PropertyFactuurnummerManager implements FactuurnummerManager {

	private final PropertiesWorker worker;
	private final PropertyKey key;
	private Optional<String> factNr;
	private final FactuurnummerFormatter formatter;

	public PropertyFactuurnummerManager(PropertyKey key) {
		this(PropertiesWorker.getInstance(), key, new NummerJaarFormatter());
	}

	public PropertyFactuurnummerManager(PropertiesWorker worker, PropertyKey key, FactuurnummerFormatter formatter) {
		this.worker = worker;
		this.key = key;
		this.factNr = Optional.empty();
		this.formatter = formatter;
	}

	@Override
	public String getFactuurnummer() {
		if (this.factNr.isPresent()) return this.factNr.get();

		Optional<String> nr = this.worker.getProperty(this.key);
		String yearNow = String.valueOf(LocalDate.now().getYear());
		if (nr.map(s -> this.formatter.heeftJaar(s, yearNow)).orElse(false)) {
			// same year
			nr.map(s -> this.formatter.parse(s, yearNow))
					.map(Factuurnummer::next)
					.map(this.formatter::format)
					.ifPresent(s -> this.worker.setProperty(this.key, s));
			this.factNr = nr;
		}
		else {
			// first in current year
			Factuurnummer initial = new Factuurnummer(yearNow);
			this.factNr = Optional.of(this.formatter.format(initial));
			Factuurnummer next = initial.next();
			this.factNr.ifPresent(s -> this.worker.setProperty(this.key, this.formatter.format(next)));
		}
		return this.factNr.get();
	}
}
