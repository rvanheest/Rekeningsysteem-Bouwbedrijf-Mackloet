package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.test.database.DatabaseFixture;
import io.strati.functional.Try;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractDocumentIntegrationTest extends DatabaseFixture {

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp () throws Exception {
    this.resetTestDir();
    super.setUp();
  }

  protected Header getHeaderWithoutInvoiceNumber() {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
    LocalDate date = LocalDate.of(2017, 7, 31);
    return new Header(debtor, date);
  }

  protected Header getHeaderWithInvoiceNumber() {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
    LocalDate date = LocalDate.of(2017, 7, 31);
    return new Header(debtor, date, "12017");
  }

  protected abstract AbstractDocument makeDocument(Header header) throws Exception;

  @Test
  public void testInitializeInvoiceNumber() throws Exception {
    InvoiceNumberTable table = new InvoiceNumberTable();
    InvoiceNumberGenerator generator = new InvoiceNumberGenerator(table);
    int currentYear = LocalDate.now().getYear();
    InvoiceNumber invoiceNumber = new InvoiceNumber(12, currentYear);
    String formattedNextInvoiceNumber = new InvoiceNumber(13, currentYear).formatted();
    AbstractDocument document = this.makeDocument(this.getHeaderWithoutInvoiceNumber());

    assertFalse(document.getHeader().getInvoiceNumber().isPresent());

    Try<String> nextInvoiceNumber = databaseAccess.doTransaction(connection -> table.initInvoiceNumber(connection)
        .ifSuccess(in -> assertEquals(new InvoiceNumber(1, currentYear), in))
        .flatMap(in -> table.setInvoiceNumber(invoiceNumber, connection))
        .ifSuccess(in -> assertEquals(invoiceNumber, in))
        .flatMap(in -> generator.calculateAndPersist(connection))
        .ifSuccess(in -> assertEquals(formattedNextInvoiceNumber, in))
        .ifSuccess(document::initInvoiceNumber));
    assertTrue(nextInvoiceNumber.isSuccess());
    assertEquals(formattedNextInvoiceNumber, nextInvoiceNumber.get());

    Optional<String> result = document.getHeader().getInvoiceNumber();
    assertTrue(result.isPresent());
    assertEquals(formattedNextInvoiceNumber, result.get());
  }
}
