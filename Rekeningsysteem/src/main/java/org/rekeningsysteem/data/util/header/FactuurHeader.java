package org.rekeningsysteem.data.util.header;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public record FactuurHeader(Debiteur debiteur, LocalDate datum, Optional<String> factuurnummer) {

	public FactuurHeader(Debiteur debiteur, LocalDate datum, String factuurnummer) {
		this(debiteur, datum, Optional.of(factuurnummer));
	}

	public FactuurHeader(Debiteur debiteur, LocalDate datum) {
		this(debiteur, datum, Optional.empty());
	}
}
