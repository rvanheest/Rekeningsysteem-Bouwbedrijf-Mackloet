package org.rekeningsysteem.logic.factuurnummer;

public class JaarKenmerkNummerFormatter implements FactuurnummerFormatter {
	
	private final String kenmerk;

	public JaarKenmerkNummerFormatter(String kenmerk) {
		this.kenmerk = kenmerk;
	}

	@Override
	public boolean heeftJaar(String factuurnummer, String jaar) {
		return factuurnummer.startsWith(jaar);
	}

	@Override
	public Factuurnummer parse(String factuurnummerString, String jaar) {
		
		return null;
	}

	@Override
	public String format(Factuurnummer nr) {
		return null;
	}
}
