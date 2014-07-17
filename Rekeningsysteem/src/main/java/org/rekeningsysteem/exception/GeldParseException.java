package org.rekeningsysteem.exception;

import java.text.ParseException;

public class GeldParseException extends ParseException {

	private static final long serialVersionUID = 1L;

	public GeldParseException(String err) {
		super(err, -1);
	}
}
