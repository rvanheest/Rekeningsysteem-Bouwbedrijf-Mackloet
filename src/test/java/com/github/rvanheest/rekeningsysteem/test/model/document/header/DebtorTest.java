package com.github.rvanheest.rekeningsysteem.test.model.document.header;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DebtorTest extends EqualsHashCodeTest {

  private Debtor debiteur;
  private final int id = 30071992;
  private final String name = "xxx";
  private final String street = "yyy";
  private final String number = "112";
  private final String zipcode = "1234AB";
  private final String city = "zzz";
  private final String vatNumber = "31071992";

  @Override
  protected Debtor makeInstance() {
    return new Debtor(this.id, this.name, this.street, this.number, this.zipcode, this.city, this.vatNumber);
  }

  @Override
  protected Debtor makeNotInstance() {
    return new Debtor(this.id, this.name + "x", this.street, this.number, this.zipcode, this.city, this.vatNumber);
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.debiteur = this.makeInstance();
  }

  @Test
  public void testGetDebtorID() {
    assertEquals(Optional.of(this.id), this.debiteur.getDebtorID());
  }

  @Test
  public void testGetDebtorIDEmpty() {
    assertEquals(Optional.empty(),
        new Debtor(this.name, this.street, this.number, this.zipcode, this.city, this.vatNumber).getDebtorID());
  }

  @Test
  public void testGetName() {
    assertEquals(this.name, this.debiteur.getName());
  }

  @Test
  public void testGetStreet() {
    assertEquals(this.street, this.debiteur.getStreet());
  }

  @Test
  public void testGetNumber() {
    assertEquals(this.number, this.debiteur.getNumber());
  }

  @Test
  public void testGetZipcode() {
    assertEquals(this.zipcode, this.debiteur.getZipcode());
  }

  @Test
  public void testGetCity() {
    assertEquals(this.city, this.debiteur.getCity());
  }

  @Test
  public void testGetBtwNummer() {
    assertEquals(Optional.of(this.vatNumber), this.debiteur.getVatNumber());
  }

  @Test
  public void testGetBtwNummerEmpty() {
    assertEquals(Optional.empty(),
        new Debtor(this.name, this.street, this.number, this.zipcode, this.city).getVatNumber());
  }

  @Test
  public void testEqualsFalseOtherDebtorID() {
    Debtor deb2 = new Debtor(this.id + 1, this.name, this.street, this.number, this.zipcode, this.city, this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherNaam() {
    Debtor deb2 = new Debtor(".", this.street, this.number, this.zipcode, this.city, this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherStraat() {
    Debtor deb2 = new Debtor(this.name, ".", this.number, this.zipcode, this.city, this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherNummer() {
    Debtor deb2 = new Debtor(this.name, this.street, ".", this.zipcode, this.city, this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherPostcode() {
    Debtor deb2 = new Debtor(this.name, this.street, this.number, ".", this.city, this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherPlaats() {
    Debtor deb2 = new Debtor(this.name, this.street, this.number, this.zipcode, ".", this.vatNumber);
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testEqualsFalseOtherBtwNummer() {
    Debtor deb2 = new Debtor(this.name, this.street, this.number, this.zipcode, this.city, ".");
    assertFalse(this.debiteur.equals(deb2));
  }

  @Test
  public void testToString() {
    assertEquals("<Debtor[Optional[30071992], xxx, yyy, 112, 1234AB, zzz, Optional[31071992]]>",
        this.debiteur.toString());
  }
}
