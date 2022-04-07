package org.rekeningsysteem.exception;

public class XmlWriteException extends RuntimeException {

	public XmlWriteException(String msg) {
		super(msg);
	}

	public XmlWriteException(Throwable cause) {
		super(cause);
	}

	public XmlWriteException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
