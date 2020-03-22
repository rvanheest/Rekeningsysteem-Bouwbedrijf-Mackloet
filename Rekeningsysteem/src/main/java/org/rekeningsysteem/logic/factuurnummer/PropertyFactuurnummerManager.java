package org.rekeningsysteem.logic.factuurnummer;

import java.time.LocalDate;
import java.util.Optional;

import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

public class PropertyFactuurnummerManager implements FactuurnummerManager {

	private final PropertiesWorker worker;
	private final PropertyKey key;
	private Optional<String> factNr;

	public PropertyFactuurnummerManager(PropertyKey key) {
		this(PropertiesWorker.getInstance(), key);
	}

	public PropertyFactuurnummerManager(PropertiesWorker worker, PropertyKey key) {
		this.worker = worker;
		this.key = key;
		this.factNr = Optional.empty();
	}

	@Override
	public String getFactuurnummer() {
		if (!this.factNr.isPresent()) {
			Optional<String> nr = this.worker.getProperty(this.key);
			String yearNow = String.valueOf(LocalDate.now().getYear());
			if (nr.map(s -> s.endsWith(yearNow)).orElse(false)) {
				// same year
				nr.map(s -> s.substring(0, s.lastIndexOf(yearNow)))
						.map(Integer::parseInt)
						.map(i -> i + 1)
						.map(String::valueOf)
						.map(s -> s.concat(yearNow))
						.ifPresent(s -> this.worker.setProperty(this.key, s));
				this.factNr = nr;
			}
			else {
				// first in current year
				this.factNr = Optional.of("1".concat(yearNow));
				this.factNr.ifPresent(s -> this.worker.setProperty(this.key, "2".concat(yearNow)));
			}
			return this.factNr.get();
		}
		return this.factNr.get();
	}
}
