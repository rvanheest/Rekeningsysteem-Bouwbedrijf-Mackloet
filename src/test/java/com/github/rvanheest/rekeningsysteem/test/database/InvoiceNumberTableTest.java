package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import io.strati.functional.Optional;
import io.strati.functional.Try;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InvoiceNumberTableTest extends DatabaseFixture {

  private final InvoiceNumberTable table = new InvoiceNumberTable();

  @Test
  public void testGetInvoiceNumberNotExists() {
    Try<Optional<InvoiceNumber>> result = this.table.getInvoiceNumber(this.connection);
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());
  }

  @Test
  public void testGetInvoiceNumberAfterInit() {
    this.testInitInvoiceNumber();

    Try<Optional<InvoiceNumber>> result = this.table.getInvoiceNumber(this.connection);
    assertTrue(result.isSuccess());
    assertTrue(result.get().isPresent());
    assertEquals(new InvoiceNumber(1, LocalDate.now().getYear()), result.get().get());
  }

  @Test
  public void testInitInvoiceNumber() {
    Try<InvoiceNumber> result = this.table.initInvoiceNumber(this.connection);
    assertTrue(result.isSuccess());
    assertEquals(new InvoiceNumber(1, LocalDate.now().getYear()), result.get());
  }

  @Test
  public void testSetInvoiceNumber() {
    this.testInitInvoiceNumber();

    InvoiceNumber invoiceNumber = new InvoiceNumber(16, 2017);
    Try<InvoiceNumber> result = this.table.setInvoiceNumber(invoiceNumber, this.connection);
    result.get();
    assertTrue(result.isSuccess());
    assertEquals(invoiceNumber, result.get());

    Try<Optional<InvoiceNumber>> result2 = this.table.getInvoiceNumber(this.connection);
    assertTrue(result2.isSuccess());
    assertTrue(result2.get().isPresent());
    assertEquals(invoiceNumber, result2.get().get());
  }
}
