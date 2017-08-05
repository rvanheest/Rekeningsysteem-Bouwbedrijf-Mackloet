package com.github.rvanheest.rekeningsysteem.model.document.header;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class HeaderWithDescription extends Header {

  private final String description;

  public HeaderWithDescription(Debtor debiteur, LocalDate date, String invoiceNumber, String description) {
    super(debiteur, date, invoiceNumber);
    this.description = description;
  }

  public HeaderWithDescription(Debtor debiteur, LocalDate date, String description) {
    super(debiteur, date);
    this.description = description;
  }

  public HeaderWithDescription(Debtor debiteur, LocalDate date, Optional<String> invoiceNumber,
      String description) {
    super(debiteur, date, invoiceNumber);
    this.description = description;
  }

  public String getOmschrijving() {
    return this.description;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof HeaderWithDescription) {
      HeaderWithDescription that = (HeaderWithDescription) other;
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
    return "<Header[" + String.valueOf(this.getDebtor()) + ", "
        + String.valueOf(this.getDatum()) + ", "
        + String.valueOf(this.getInvoiceNumber()) + ", "
        + String.valueOf(this.description) + "]>";
  }
}
