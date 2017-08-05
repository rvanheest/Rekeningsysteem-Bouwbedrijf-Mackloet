package com.github.rvanheest.rekeningsysteem.model.repair;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractInvoice;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;

public class RepairInvoice extends AbstractInvoice<RepairListItem> {

  public RepairInvoice(Header header, ItemList<RepairListItem> itemList) {
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
    return "<RepairInvoice[" + String.valueOf(this.getHeader()) + ", " + String.valueOf(this.getItemList()) + "]>";
  }
}
