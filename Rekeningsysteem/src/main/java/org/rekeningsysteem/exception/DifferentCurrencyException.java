package org.rekeningsysteem.exception;

import java.util.Currency;

public class DifferentCurrencyException extends Exception {

	private static final long serialVersionUID = 2071625979931226332L;

	public DifferentCurrencyException(Currency c1, Currency c2) {
		super("Currency " + c1 + " is not equal to currency " + c2);
	}
}
