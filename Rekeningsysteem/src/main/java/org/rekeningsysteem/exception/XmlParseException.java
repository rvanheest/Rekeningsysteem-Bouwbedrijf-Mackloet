package org.rekeningsysteem.exception;

public class XmlParseException extends Exception {

	public XmlParseException(String msg) {
		super(msg);
	}

	public XmlParseException(Throwable cause) {
		super(cause);
	}

	public XmlParseException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
