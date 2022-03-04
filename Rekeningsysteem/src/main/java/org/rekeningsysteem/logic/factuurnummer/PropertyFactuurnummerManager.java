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
		if (this.factNr.isPresent()) return this.factNr.get();
		
		Optional<String> nr = this.worker.getProperty(this.key);
		String yearNow = String.valueOf(LocalDate.now().getYear());
		if (nr.map(s -> factuurnummerHeeftJaar(s, yearNow)).orElse(false)) {
			// same year
			nr.map(s -> parseFactuurnummer(s, yearNow))
					.map(Factuurnummer::next)
					.map(this::format)
					.ifPresent(s -> this.worker.setProperty(this.key, s));
			this.factNr = nr;
		}
		else {
			// first in current year
			Factuurnummer initial = new Factuurnummer(yearNow);
			this.factNr = Optional.of(format(initial));
			Factuurnummer next = initial.next();
			this.factNr.ifPresent(s -> this.worker.setProperty(this.key, format(next)));
		}
		return this.factNr.get();
	}

	private boolean factuurnummerHeeftJaar(String factuurnummer, String jaar) {
		return factuurnummer.endsWith(jaar);
	}

	private Factuurnummer parseFactuurnummer(String s, String jaar) {
		String nr = s.substring(0, s.lastIndexOf(jaar));
		int nummer = Integer.parseInt(nr);
		return new Factuurnummer(jaar, nummer);
	}

	private String format(Factuurnummer nr) {
		return nr.getNummer() + nr.getJaar();
	}

}
