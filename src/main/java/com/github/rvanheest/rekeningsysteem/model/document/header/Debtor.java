package com.github.rvanheest.rekeningsysteem.model.document.header;

import java.util.Objects;
import java.util.Optional;

public final class Debtor {

  private final Optional<Integer> id;
  private final String name;
  private final String street;
  private final String number;
  private final String zipcode;
  private final String city;
  private final Optional<String> vatNumber;

  public Debtor(String name, String street, String number, String zipcode, String city) {
    this(name, street, number, zipcode, city, Optional.empty());
  }

  public Debtor(String name, String street, String number, String zipcode, String city, String vatNumber) {
    this(name, street, number, zipcode, city, Optional.ofNullable(vatNumber));
  }

  public Debtor(String name, String street, String number, String zipcode, String city, Optional<String> vatNumber) {
    this(Optional.empty(), name, street, number, zipcode, city, vatNumber);
  }

  public Debtor(int debiteurId, String name, String street, String number, String zipcode, String city) {
    this(Optional.of(debiteurId), name, street, number, zipcode, city, Optional.empty());
  }

  public Debtor(int debiteurId, String name, String street, String number, String zipcode, String city, String vatNumber) {
    this(Optional.of(debiteurId), name, street, number, zipcode, city, Optional.ofNullable(vatNumber));
  }

  public Debtor(Optional<Integer> id, String name, String street, String number, String zipcode,
      String city, Optional<String> vatNumber) {
    this.id = id;
    this.name = name;
    this.street = street;
    this.number = number;
    this.zipcode = zipcode;
    this.city = city;
    this.vatNumber = vatNumber;
  }

  public Optional<Integer> getDebtorID() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getStreet() {
    return this.street;
  }

  public String getNumber() {
    return this.number;
  }

  public String getZipcode() {
    return this.zipcode;
  }

  public String getCity() {
    return this.city;
  }

  public Optional<String> getVatNumber() {
    return this.vatNumber;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Debtor) {
      Debtor that = (Debtor) other;
      return Objects.equals(this.id, that.id)
          && Objects.equals(this.name, that.name)
          && Objects.equals(this.street, that.street)
          && Objects.equals(this.number, that.number)
          && Objects.equals(this.zipcode, that.zipcode)
          && Objects.equals(this.city, that.city)
          && Objects.equals(this.vatNumber, that.vatNumber);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.street, this.number,
        this.zipcode, this.city, this.vatNumber);
  }

  @Override
  public String toString() {
    return "<Debtor[" + String.valueOf(this.id) + ", "
        + String.valueOf(this.name) + ", "
        + String.valueOf(this.street) + ", "
        + String.valueOf(this.number) + ", "
        + String.valueOf(this.zipcode) + ", "
        + String.valueOf(this.city) + ", "
        + String.valueOf(this.vatNumber) + "]>";
  }
}
