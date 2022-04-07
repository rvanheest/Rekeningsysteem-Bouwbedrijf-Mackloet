package org.rekeningsysteem.exception;

public class PdfException extends Exception {

	private static final long serialVersionUID = 5773190986626550090L;

	public PdfException(String err) {
		super(err);
	}

	public PdfException(String err, Throwable cause) {
		super(err, cause);
	}
}
