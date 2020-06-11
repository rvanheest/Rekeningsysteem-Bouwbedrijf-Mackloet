package org.rekeningsysteem.exception;

public class PdfException extends RuntimeException {

	private static final long serialVersionUID = 5773190986626550090L;

	public PdfException(String err) {
		super(err);
	}
}
