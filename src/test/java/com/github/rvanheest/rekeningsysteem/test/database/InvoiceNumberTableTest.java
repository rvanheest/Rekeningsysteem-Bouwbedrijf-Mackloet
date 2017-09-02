package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class InvoiceNumberTableTest extends DatabaseFixture {

  private final InvoiceNumberTable table = new InvoiceNumberTable();

  @Test
  public void testGetInvoiceNumberNotExists() {
    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGetInvoiceNumberAfterInit() {
    this.testInitInvoiceNumber();

    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertValue(new InvoiceNumber(1, LocalDate.now().getYear()))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testInitInvoiceNumber() {
    this.databaseAccess.doTransactionSingle(this.table.initInvoiceNumber())
        .test()
        .assertValue(new InvoiceNumber(1, LocalDate.now().getYear()))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testSetInvoiceNumber() {
    this.testInitInvoiceNumber();

    InvoiceNumber invoiceNumber = new InvoiceNumber(16, 2017);
    this.databaseAccess.doTransactionSingle(this.table.setInvoiceNumber(invoiceNumber))
        .test()
        .assertValue(invoiceNumber)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertValue(invoiceNumber)
        .assertNoErrors()
        .assertComplete();
  }
}
