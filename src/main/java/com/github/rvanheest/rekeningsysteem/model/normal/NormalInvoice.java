package com.github.rvanheest.rekeningsysteem.model.normal;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractInvoice;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;

import java.util.Objects;

public class NormalInvoice extends AbstractInvoice<NormalListItem> {

  private final String description;

  public NormalInvoice(Header header, String description, ItemList<NormalListItem> itemList) {
    super(header, itemList);
    this.description = description;
  }

  public String getDescription() {
    return this.description;
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
    if (other instanceof NormalInvoice) {
      NormalInvoice that = (NormalInvoice) other;
      return super.equals(that)
          && Objects.equals(this.description, that.description);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.description);
  }

  @Override
  public String toString() {
    return "<NormalInvoice[" + String.valueOf(this.getHeader()) + ", "
        + String.valueOf(this.getDescription()) + ", "
        + String.valueOf(this.getItemList()) + "]>";
  }
}
