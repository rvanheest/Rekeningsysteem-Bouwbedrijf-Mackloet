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

	public PropertyFactuurnummerManager(PropertyKey nummerKey, PropertyKey kenmerkKey) {
		this(PropertiesWorker.getInstance(), nummerKey, kenmerkKey);
	}

	public PropertyFactuurnummerManager(PropertiesWorker worker, PropertyKey nummerKey, PropertyKey kenmerkKey) {
		this(worker, nummerKey, CompositeFactuurnummerFormatter.Create(worker, kenmerkKey));
	}

	public PropertyFactuurnummerManager(PropertiesWorker worker, PropertyKey key, FactuurnummerFormatter formatter) {
		this.worker = worker;
		this.key = key;
		this.factNr = Optional.empty();
		this.formatter = formatter;
	}

	@Override
	public String getFactuurnummer() {
		return this.factNr
			.orElseGet(() -> {
				String yearNow = String.valueOf(LocalDate.now().getYear());

				Factuurnummer factuurnummer = this.worker.getProperty(this.key)
					.filter(s -> this.formatter.heeftJaar(s, yearNow))
					.map(s -> this.formatter.parse(s, yearNow))
					.orElseGet(() -> new Factuurnummer(yearNow));

				this.worker.setProperty(this.key, this.formatter.format(factuurnummer.next()));

				String result = this.formatter.format(factuurnummer);
				this.factNr = Optional.of(result);
				return result;
			});
	}
}
