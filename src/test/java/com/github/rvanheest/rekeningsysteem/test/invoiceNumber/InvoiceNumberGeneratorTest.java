package com.github.rvanheest.rekeningsysteem.test.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import io.strati.functional.Optional;
import io.strati.functional.Try;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InvoiceNumberGeneratorTest extends DatabaseFixture {

  private final InvoiceNumberTable table = new InvoiceNumberTable();
  private final InvoiceNumberGenerator generator = new InvoiceNumberGenerator(table);
  private final int yearNow = LocalDate.now().getYear();

  @Test
  public void testGenerateFirstInvoiceNumber() {
    // make sure table is empty
    Try<Optional<InvoiceNumber>> result = this.table.getInvoiceNumber(this.connection);
    assertTrue(result.isSuccess());
    assertTrue(result.get().isEmpty());

    Try<String> invoiceNumber = this.generator.calculateAndPersist(this.connection);
    assertTrue(invoiceNumber.isSuccess());
    assertEquals("1" + this.yearNow, invoiceNumber.get());
  }

  @Test
  public void testGenerateNextInvoiceNumber() {
    this.testGenerateFirstInvoiceNumber();

    Try<String> invoiceNumber = this.generator.calculateAndPersist(this.connection);
    assertTrue(invoiceNumber.isSuccess());
    assertEquals("2" + this.yearNow, invoiceNumber.get());
  }

  @Test
  public void testGenerateNextWithFormerYear() {
    this.table.setInvoiceNumber(new InvoiceNumber(15, 2013), this.connection);

    Try<String> invoiceNumber = this.generator.calculateAndPersist(this.connection);
    assertTrue(invoiceNumber.isSuccess());
    assertEquals("1" + this.yearNow, invoiceNumber.get());
  }
}
