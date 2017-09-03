package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.pdf.PdfConverter;
import com.github.rvanheest.rekeningsysteem.pdf.PdfDocumentVisitor;
import com.github.rvanheest.rekeningsysteem.pdf.PdfListItemVisitor;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.format.MonetaryAmountFormat;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PdfDocumentVisitorTest {

  static class MockedPdfConverter extends PdfConverter {
    public MockedPdfConverter() {
      super(Paths.get(""));
    }
  }

  private PdfDocumentVisitor visitor;

  @Mock private MonetaryAmountFormat moneyFormatter;
  @Mock private PdfListItemVisitor pdfListItemVisitor;
  private final Locale locale = Locale.forLanguageTag("nl-NL");
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() {
    this.visitor = new PdfDocumentVisitor("dd-MM-yyyy", this.locale, this.moneyFormatter, this.pdfListItemVisitor);
  }

  private void verifyReplaceDebtor(PdfConverter converter, Debtor debtor) {
    verify(converter).replace("DebiteurNaam", debtor.getName());
    verify(converter).replace("DebiteurStraat", debtor.getStreet());
    verify(converter).replace("DebiteurNummer", debtor.getNumber());
    verify(converter).replace("DebiteurPostcode", debtor.getZipcode());
    verify(converter).replace("DebiteurPlaats", debtor.getCity());
    verify(converter).replace("HasDebiteurBtwNummer", debtor.getVatNumber().isPresent());
    verify(converter).replace("DebiteurBtwNummer", debtor.getVatNumber().orElse(""));
  }

  private void verifyReplaceHeader(PdfConverter converter, Header header) {
    this.verifyReplaceDebtor(converter, header.getDebtor());
    header.getInvoiceNumber().ifPresent(s -> verify(converter).replace("Factuurnummer", s));
    LocalDate date = header.getDate();
    verify(converter).replace("Datum",
        String.format("%02d-%02d-%d", date.getDayOfMonth(), date.getMonthValue(), date.getYear()));
  }

  private void verifyReplaceCurrency(PdfConverter converter) {
    verify(converter).replace("Valuta", "€");
  }

  @Test
  public void testVisitMutationInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    MutationListItem item1 = new MutationListItem("descr1", "itemId1", Money.of(12.34, this.currency));
    MutationListItem item2 = new MutationListItem("descr2", "itemId2", Money.of(23.45, this.currency));
    MutationListItem item3 = new MutationListItem("descr3", "itemId3", Money.of(34.56, this.currency));
    ItemList<MutationListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    MutationInvoice invoice = new MutationInvoice(header, itemList);

    List<String> item1Output = Arrays.asList("descr1", "itemId1", "12,34");
    List<String> item2Output = Arrays.asList("descr2", "itemId2", "23,45");
    List<String> item3Output = Arrays.asList("descr3", "itemId3", "34,56");

    when(this.pdfListItemVisitor.visit(eq(item1))).thenReturn(item1Output);
    when(this.pdfListItemVisitor.visit(eq(item2))).thenReturn(item2Output);
    when(this.pdfListItemVisitor.visit(eq(item3))).thenReturn(item3Output);
    when(this.moneyFormatter.format(any())).thenReturn("foo");

    PdfConverter converter = mock(MockedPdfConverter.class);
    this.visitor.visit(invoice).accept(converter);

    verify(this.moneyFormatter).format(eq(Money.of(70.35, this.currency)));
    verifyNoMoreInteractions(this.moneyFormatter);

    verifyReplaceHeader(converter, invoice.getHeader());
    verifyReplaceCurrency(converter);
    verify(converter).replace("bonList", Arrays.asList(item1Output, item2Output, item3Output));
    verify(converter).replace("TotaalBedrag", "foo");
    verifyNoMoreInteractions(converter);
  }

  @Test
  public void testVisitOfferDocument() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    Offer offer = new Offer(header, "abc", true);

    PdfConverter converter = mock(MockedPdfConverter.class);
    this.visitor.visit(offer).accept(converter);

    verifyZeroInteractions(this.moneyFormatter, this.pdfListItemVisitor);

    verifyReplaceHeader(converter, offer.getHeader());
    verify(converter).replace("Tekst", offer.getText());
    if (offer.isSign())
      verify(converter).replace("Ondertekenen", "true");
    else
      verify(converter).replace("Ondertekenen", "false");
    verifyNoMoreInteractions(converter);
  }

  @Test
  public void testVisitNormalInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    SimpleListItem item1 = new SimpleListItem("descr1", Money.of(12.34, this.currency), 25);
    DefaultWage item2 = new DefaultWage("descr2", Money.of(23.45, this.currency), 16);
    HourlyWage item3 = new HourlyWage("descr3", 3.14, Money.of(34.56, this.currency), 16);
    ItemList<NormalListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    NormalInvoice invoice = new NormalInvoice(header, "invoice descr", itemList);

    List<String> item1Output = Arrays.asList("descr1", "12,34", "25");
    List<String> item2Output = Arrays.asList("descr2", "23,45", "16");
    List<String> item3Output = Arrays.asList("3.14 uren à 34.56", "108,52", "16");

    List<String> tax1Output = Arrays.asList("16.0", "131,97", "21,12");
    List<String> tax2Output = Arrays.asList("25.0", "12,34", "3,09");

    when(this.pdfListItemVisitor.visit(eq(item1))).thenReturn(item1Output);
    when(this.pdfListItemVisitor.visit(eq(item2))).thenReturn(item2Output);
    when(this.pdfListItemVisitor.visit(eq(item3))).thenReturn(item3Output);
    when(this.moneyFormatter.format(any())).thenReturn("144,31", "131,97", "21,12", "12,34", "3,09", "168,52");

    PdfConverter converter = mock(MockedPdfConverter.class);
    this.visitor.visit(invoice).accept(converter);

    verify(this.moneyFormatter).format(eq(Money.of(144.3084, this.currency)));
    verify(this.moneyFormatter).format(eq(Money.of(131.9684, this.currency)));
    verify(this.moneyFormatter).format(eq(Money.of(21.114944, this.currency)));
    verify(this.moneyFormatter).format(eq(Money.of(12.34, this.currency)));
    verify(this.moneyFormatter).format(eq(Money.of(3.085, this.currency)));
    verify(this.moneyFormatter).format(eq(Money.of(168.508344, this.currency)));
    verifyNoMoreInteractions(this.moneyFormatter);

    verifyReplaceHeader(converter, invoice.getHeader());
    verify(converter).replace("Omschrijving", invoice.getDescription());
    verifyReplaceCurrency(converter);
    verify(converter).replace("artikelList", Arrays.asList(item1Output, item2Output, item3Output));
    verify(converter).replace("SubTotaalBedrag", "144,31");
    verify(converter).replace("btwList", Arrays.asList(tax1Output, tax2Output));
    verify(converter).replace("TotaalBedrag", "168,52");
    verifyNoMoreInteractions(converter);
  }

  @Test
  public void testVisitRepairInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    RepairListItem item1 = new RepairListItem("descr1", "itemId1", Money.of(12.34, this.currency),
        Money.of(96, this.currency));
    RepairListItem item2 = new RepairListItem("descr2", "itemId2", Money.of(23.45, this.currency),
        Money.of(97, this.currency));
    RepairListItem item3 = new RepairListItem("descr3", "itemId3", Money.of(34.56, this.currency),
        Money.of(98, this.currency));
    ItemList<RepairListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    RepairInvoice invoice = new RepairInvoice(header, itemList);

    List<String> item1Output = Arrays.asList("descr1", "itemId1", "12,34", "96");
    List<String> item2Output = Arrays.asList("descr2", "itemId2", "23,45", "97");
    List<String> item3Output = Arrays.asList("descr3", "itemId3", "34,56", "98");

    when(this.pdfListItemVisitor.visit(eq(item1))).thenReturn(item1Output);
    when(this.pdfListItemVisitor.visit(eq(item2))).thenReturn(item2Output);
    when(this.pdfListItemVisitor.visit(eq(item3))).thenReturn(item3Output);
    when(this.moneyFormatter.format(any())).thenReturn("bar");

    PdfConverter converter = mock(MockedPdfConverter.class);
    this.visitor.visit(invoice).accept(converter);

    verify(this.moneyFormatter).format(eq(Money.of(361.35, this.currency)));
    verifyNoMoreInteractions(this.moneyFormatter);

    verifyReplaceHeader(converter, invoice.getHeader());
    verifyReplaceCurrency(converter);
    verify(converter).replace("bonList", Arrays.asList(item1Output, item2Output, item3Output));
    verify(converter).replace("TotaalBedrag", "bar");
    verifyNoMoreInteractions(converter);
  }
}
