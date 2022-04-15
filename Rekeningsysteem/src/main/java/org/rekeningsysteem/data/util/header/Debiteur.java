package org.rekeningsysteem.data.util.header;

import java.util.Optional;

public record Debiteur(Optional<Integer> debiteurID, String naam, String straat, String nummer, String postcode, String plaats, Optional<String> btwNummer) {

	public Debiteur(String naam, String straat, String nummer, String postcode, String plaats) {
		this(naam, straat, nummer, postcode, plaats, Optional.empty());
	}

	public Debiteur(String naam, String straat, String nummer, String postcode, String plaats, String btwNummer) {
		this(naam, straat, nummer, postcode, plaats, Optional.ofNullable(btwNummer));
	}

	public Debiteur(String naam, String straat, String nummer, String postcode, String plaats, Optional<String> btwNummer) {
		this(Optional.empty(), naam, straat, nummer, postcode, plaats, btwNummer);
	}

	public Debiteur(int debiteurId, String naam, String straat, String nummer, String postcode, String plaats) {
		this(debiteurId, naam, straat, nummer, postcode, plaats, null);
	}

	public Debiteur(int debiteurId, String naam, String straat, String nummer, String postcode, String plaats, String btwNummer) {
		this(Optional.of(debiteurId), naam, straat, nummer, postcode, plaats, Optional.ofNullable(btwNummer));
	}
}
