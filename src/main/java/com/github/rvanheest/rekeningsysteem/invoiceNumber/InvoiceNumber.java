package com.github.rvanheest.rekeningsysteem.invoiceNumber;

import java.time.LocalDate;
import java.util.Objects;

public class InvoiceNumber {

  private final int number;
  private final int year;

  public InvoiceNumber(int number, int year) {
    this.number = number;
    this.year = year;
  }

  public int getNumber() {
    return this.number;
  }

  public int getYear() {
    return this.year;
  }

  public String formatted() {
    return String.valueOf(this.number) + String.valueOf(this.year);
  }

  public InvoiceNumber next() {
    int currentYear = LocalDate.now().getYear();
    if (currentYear == this.year)
      return new InvoiceNumber(this.number + 1, this.year);
    else
      return new InvoiceNumber(1, currentYear);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof InvoiceNumber) {
      InvoiceNumber that = (InvoiceNumber) other;
      return Objects.equals(this.number, that.number)
          && Objects.equals(this.year, that.year);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.number, this.year);
  }

  @Override
  public String toString() {
    return "<InvoiceNumber[" + String.valueOf(this.number) + ", " + String.valueOf(this.year) + "]>";
  }
}
