package com.github.rvanheest.rekeningsysteem.exception;

public class XmlWriteException extends Exception {

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
