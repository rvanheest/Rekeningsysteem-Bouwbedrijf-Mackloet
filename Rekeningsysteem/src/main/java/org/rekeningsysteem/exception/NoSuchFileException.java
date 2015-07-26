package org.rekeningsysteem.exception;

public class NoSuchFileException extends Exception {

	private static final long serialVersionUID = -5684032056630170886L;

	public NoSuchFileException(String error) {
		super(error);
	}
}
