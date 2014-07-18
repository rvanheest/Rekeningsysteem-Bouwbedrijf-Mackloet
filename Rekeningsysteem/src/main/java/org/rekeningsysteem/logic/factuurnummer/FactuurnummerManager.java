package org.rekeningsysteem.logic.factuurnummer;

/**
 * Interface voor de communicatie met de manager van het factuurnummer. Deze manager moet een
 * factuurnummer geven/genereren voor de factuur. Om het factuurnummer van de manager te vragen,
 * moet de methode {@link #getFactuurnummer()} worden geïmplementeerd.
 * 
 * @author Richard van Heest
 */
public interface FactuurnummerManager {

	/**
	 * Retourneert het gevraagde factuurnummer.
	 * 
	 * @return het gevraagde factuurnummer.
	 */
	String getFactuurnummer();
}
