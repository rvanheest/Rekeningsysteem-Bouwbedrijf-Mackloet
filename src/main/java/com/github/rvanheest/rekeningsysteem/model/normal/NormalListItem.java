package com.github.rvanheest.rekeningsysteem.model.normal;

import java.util.Objects;

public abstract class NormalListItem implements TaxedListItem {

  private final String description;

  public NormalListItem(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof NormalListItem) {
      NormalListItem that = (NormalListItem) other;
      return Objects.equals(this.description, that.description);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.description);
  }
}
