package com.github.rvanheest.rekeningsysteem.model.offer;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;

import java.util.Objects;

public class Offer extends AbstractDocument {

  private final String text;
  private final boolean sign;

  public Offer(Header header, String text, boolean sign) {
    super(header);
    this.text = text;
    this.sign = sign;
  }

  public String getText() {
    return this.text;
  }

  public boolean isSign() {
    return this.sign;
  }

  @Override
  public <T> T accept(DocumentVisitor<T> visitor) throws Exception {
    return visitor.visit(this);
  }

  @Override
  public void accept(DocumentVoidVisitor visitor) throws Exception {
    visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other instanceof Offer) {
      Offer that = (Offer) other;
      return Objects.equals(this.text, that.text)
          && Objects.equals(this.sign, that.sign);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.text, this.sign);
  }

  @Override
  public String toString() {
    return "<Offer[" + String.valueOf(this.getHeader()) + ", "
        + String.valueOf(this.text) + ", "
        + String.valueOf(this.sign) + "]>";
  }
}
