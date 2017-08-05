package com.github.rvanheest.rekeningsysteem.model.document;

import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitable;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitable;

import java.util.Objects;

public abstract class AbstractDocument implements DocumentVisitable, DocumentVoidVisitable {

  private final Header header;

  public AbstractDocument(Header header) {
    this.header = header;
  }

  public Header getHeader() {
    return header;
  }

  // TODO initInvoiceNumber

  @Override
  public boolean equals(Object other) {
    if (other instanceof AbstractDocument) {
      AbstractDocument that = (AbstractDocument) other;
      return Objects.equals(this.header, that.header);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.header);
  }
}
