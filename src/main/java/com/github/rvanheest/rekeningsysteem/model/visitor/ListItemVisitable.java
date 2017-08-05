package com.github.rvanheest.rekeningsysteem.model.visitor;

public interface ListItemVisitable {

  <T> T accept(ListItemVisitor<T> visitor);
}
