package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.pdf.PdfExporter;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.xml.XmlLoader;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader1;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader2;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader3;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader4;
import com.github.rvanheest.rekeningsysteem.xml.XmlSaver;
import com.github.rvanheest.rekeningsysteem.xml.XmlWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractDocumentIntegrationTest implements DatabaseFixture, ConfigurationFixture {

  private DatabaseConnection databaseAccess;

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() throws Exception {
    this.databaseAccess = this.initDatabaseConnection();
  }

  @After
  public void tearDown() throws Exception {
    this.databaseAccess.closeConnectionPool();
  }

  Header getHeaderWithoutInvoiceNumber() {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
    LocalDate date = LocalDate.of(2017, 7, 31);
    return new Header(debtor, date);
  }

  private Header getHeaderWithInvoiceNumber() {
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

    this.databaseAccess.doTransactionSingle(connection -> table.initInvoiceNumber()
        .apply(connection)
        .doOnSuccess(in -> assertEquals(new InvoiceNumber(1, currentYear), in))
        .flatMap(in -> table.setInvoiceNumber(invoiceNumber).apply(connection))
        .doOnSuccess(in -> assertEquals(invoiceNumber, in))
        .flatMap(in -> generator.calculateAndPersist().apply(connection))
        .doOnSuccess(document::initInvoiceNumber))
        .test()
        .assertValue(formattedNextInvoiceNumber)
        .assertNoErrors()
        .assertComplete();

    Optional<String> result = document.getHeader().getInvoiceNumber();
    assertTrue(result.isPresent());
    assertEquals(formattedNextInvoiceNumber, result.get());
  }

  @Test
  public void testExportPdf() throws Exception {
    PdfExporter exporter = new PdfExporter(this.getConfiguration(), Locale.forLanguageTag("nl-NL"));
    AbstractDocument document = this.makeDocument(this.getHeaderWithInvoiceNumber());
    Path pdf = this.getTestDir().resolve("document.pdf");

    assertFalse(Files.exists(pdf));

    exporter.export(document, pdf);

    assertTrue(Files.exists(pdf));
  }

  @Test
  public void testSaveXml() throws Exception {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    XmlSaver saver = new XmlWriter(builder, transformerFactory);
    XmlLoader reader = new XmlReader(builder,
        new XmlReader4(builder),
        new XmlReader3(builder),
        new XmlReader2(builder),
        new XmlReader1(builder, Locale.forLanguageTag("nl-NL"), Monetary.getCurrency("EUR")));

    AbstractDocument document = this.makeDocument(this.getHeaderWithInvoiceNumber());
    Path xml = this.getTestDir().resolve("document.xml");

    assertFalse(Files.exists(xml));
    saver.save(document, xml);
    assertTrue(Files.exists(xml));
    reader.load(xml).test()
        .assertValue(document)
        .assertNoErrors()
        .assertComplete();
  }
}
