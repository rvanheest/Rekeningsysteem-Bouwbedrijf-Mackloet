package com.github.rvanheest.rekeningsysteem.model.document.header;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Header {

  private final Debtor debtor;
  private final LocalDate date;
  private Optional<String> invoiceNumber;

  public Header(Debtor debtor, LocalDate date, String invoiceNumber) {
    this(debtor, date, Optional.of(invoiceNumber));
  }

  public Header(Debtor debtor, LocalDate date) {
    this(debtor, date, Optional.empty());
  }

  public Header(Debtor debtor, LocalDate date, Optional<String> invoiceNumber) {
    this.debtor = debtor;
    this.date = date;
    this.invoiceNumber = invoiceNumber;
  }

  public Debtor getDebtor() {
    return this.debtor;
  }

  public LocalDate getDate() {
    return this.date;
  }

  public Optional<String> getInvoiceNumber() {
    return this.invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = Optional.ofNullable(invoiceNumber);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Header) {
      Header that = (Header) other;
      return Objects.equals(this.debtor, that.debtor)
          && Objects.equals(this.date, that.date)
          && Objects.equals(this.invoiceNumber, that.invoiceNumber);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.debtor, this.date, this.invoiceNumber);
  }

  @Override
  public String toString() {
    return "<Header[" + String.valueOf(this.debtor) + ", "
        + String.valueOf(this.date) + ", "
        + String.valueOf(this.invoiceNumber) + "]>";
  }
}
