package com.github.rvanheest.rekeningsysteem.test.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.AbstractDocumentManager;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import io.reactivex.observers.TestObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public abstract class AbstractDocumentManagerTest<Doc extends AbstractDocument> {

  private AbstractDocumentManager<Doc> docManager;

  protected abstract Doc emptyDoc();

  protected abstract Doc createDocument(Header header);

  protected abstract AbstractDocumentManager<Doc> createDocumentManager();

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() {
    this.docManager = this.createDocumentManager();
  }

  @After
  public void tearDown() {
    this.docManager.dispose();
  }

  @Test
  public void testInitialDocumentIsEmpty() {
    Doc emptyDoc = this.emptyDoc();
    this.docManager.getDocument()
        .test()
        .assertValue(emptyDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorName() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "my-name", "", "", "", "", Optional.empty());
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorName("my-name")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorStreet() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "", "my-street", "", "", "", Optional.empty());
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorStreet("my-street")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorNumber() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "", "", "my-number", "", "", Optional.empty());
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorNumber("my-number")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorZipcode() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "", "", "", "my-zipcode", "", Optional.empty());
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorZipcode("my-zipcode")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorCity() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "", "", "", "", "my-city", Optional.empty());
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorCity("my-city")
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtorVatNumber() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(Optional.empty(), "", "", "", "", "", Optional.of("my-vatnumber"));
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtorVatNumber(Optional.of("my-vatnumber"))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDebtor() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    Debtor expectedDebtor = new Debtor(1, "my-name", "my-street", "my-number", "my-zipcode", "my-city", "my-vatnumber");
    Header expectedHeader = new Header(
        expectedDebtor,
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDebtor(expectedDebtor)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertValue(expectedDebtor)
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithStoreDebtorOnSave() {
    TestObserver<Boolean> storeDebtorTestObserver = this.docManager.storeDebtorOnSave().skip(1L).test();

    this.docManager.withStoreDebtorOnSave(true)
        .andThen(this.docManager.withStoreDebtorOnSave(true))
        .andThen(this.docManager.withStoreDebtorOnSave(false))
        .andThen(this.docManager.withStoreDebtorOnSave(true))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    storeDebtorTestObserver
        .assertValues(true, false, true)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithDate() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    LocalDate expectedDate = LocalDate.parse("2018-07-31", DateTimeFormatter.ISO_DATE);
    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        expectedDate,
        "12018"
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withDate(expectedDate)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithInvoiceNumber() {
    TestObserver<Debtor> debtorTestObserver = this.docManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.docManager.getHeader().skip(1L).test();
    TestObserver<Doc> docTestObserver = this.docManager.getDocument().skip(1L).test();

    String expectedInvoiceNumber = "22018";
    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        expectedInvoiceNumber
    );
    Doc expectedDoc = this.createDocument(expectedHeader);

    this.docManager.withInvoiceNumber(expectedInvoiceNumber)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertValue(expectedHeader)
        .assertNoErrors()
        .assertNotComplete();

    docTestObserver
        .assertValue(expectedDoc)
        .assertNoErrors()
        .assertNotComplete();
  }
}
