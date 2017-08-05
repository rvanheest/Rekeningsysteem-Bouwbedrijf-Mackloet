package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.junit.BeforeClass;

import java.time.LocalDate;

public abstract class AbstractDocumentIntegrationTest {

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  protected Header getHeader() {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
    LocalDate date = LocalDate.of(2017, 7, 31);
    return new Header(debtor, date, "12017");
  }

  protected abstract AbstractDocument makeDocument() throws Exception;
}
