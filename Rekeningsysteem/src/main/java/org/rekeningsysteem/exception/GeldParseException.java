package org.rekeningsysteem.exception;

import java.text.ParseException;

public class GeldParseException extends ParseException {

	private static final long serialVersionUID = 8879311665577070103L;

	public GeldParseException(String err) {
		super(err, -1);
	}
}
