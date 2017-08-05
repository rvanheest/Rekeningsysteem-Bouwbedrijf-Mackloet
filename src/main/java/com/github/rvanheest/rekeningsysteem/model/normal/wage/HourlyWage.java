package com.github.rvanheest.rekeningsysteem.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public final class HourlyWage extends AbstractWage {

  private final double hours;
  private final MonetaryAmount wagePerHour;
  private final double wageTaxPercentage;

  public HourlyWage(String description, double hours, MonetaryAmount wagePerHour, double wageTaxPercentage) {
    super(description);
    this.hours = hours;
    this.wagePerHour = wagePerHour;
    this.wageTaxPercentage = wageTaxPercentage;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.wagePerHour.getCurrency();
  }

  public double getHours() {
    return this.hours;
  }

  public MonetaryAmount getWagePerHour() {
    return this.wagePerHour;
  }

  @Override
  public MonetaryAmount getWage() {
    return this.wagePerHour.multiply(this.hours);
  }

  @Override
  public double getWageTaxPercentage() {
    return this.wageTaxPercentage;
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other instanceof HourlyWage) {
      HourlyWage that = (HourlyWage) other;
      return Objects.equals(this.hours, that.hours)
          && Objects.equals(this.wagePerHour, that.wagePerHour)
          && Objects.equals(this.wageTaxPercentage, that.wageTaxPercentage);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.hours, this.wagePerHour, this.wageTaxPercentage);
  }

  @Override
  public String toString() {
    return "<HourlyWage[" + String.valueOf(this.getDescription()) + ", "
        + String.valueOf(this.hours) + ", "
        + String.valueOf(this.wagePerHour) + ", "
        + String.valueOf(this.wageTaxPercentage) + "]>";
  }
}
