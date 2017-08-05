package com.github.rvanheest.rekeningsysteem.model.normal;

import javax.money.MonetaryAmount;
import java.util.Objects;

public final class EsselinkItem {

  private final String itemId;
  private final String description;
  private final int amountPer;
  private final String unit;
  private final MonetaryAmount pricePerUnit;

  public EsselinkItem(String itemId, String description, int amountPer, String unit, MonetaryAmount pricePerUnit) {
    this.itemId = itemId;
    this.description = description;
    this.amountPer = amountPer;
    this.unit = unit;
    this.pricePerUnit = pricePerUnit;
  }

  public String getItemId() {
    return this.itemId;
  }

  public String getDescription() {
    return this.description;
  }

  public int getAmountPer() {
    return this.amountPer;
  }

  public String getUnit() {
    return this.unit;
  }

  public MonetaryAmount getPricePerUnit() {
    return this.pricePerUnit;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof EsselinkItem) {
      EsselinkItem that = (EsselinkItem) other;
      return Objects.equals(this.itemId, that.itemId)
          && Objects.equals(this.description, that.description)
          && Objects.equals(this.amountPer, that.amountPer)
          && Objects.equals(this.unit, that.unit)
          && Objects.equals(this.pricePerUnit, that.pricePerUnit);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.itemId, this.description, this.amountPer, this.unit, this.pricePerUnit);
  }

  @Override
  public String toString() {
    return "<EsselinkItem[" + String.valueOf(this.itemId) + ", "
        + String.valueOf(this.description) + ", "
        + String.valueOf(this.amountPer) + ", "
        + String.valueOf(this.unit) + ", "
        + String.valueOf(this.pricePerUnit) + "]>";
  }
}
