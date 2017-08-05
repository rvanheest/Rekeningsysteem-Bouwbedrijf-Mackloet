package com.github.rvanheest.rekeningsysteem.model.mutation;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractInvoice;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;

public class MutationInvoice extends AbstractInvoice<MutationListItem> {

  public MutationInvoice(Header header, ItemList<MutationListItem> itemList) {
    super(header, itemList);
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
  public String toString() {
    return "<MutationInvoice[" + String.valueOf(this.getHeader()) + ", " + String.valueOf(this.getItemList()) + "]>";
  }
}
