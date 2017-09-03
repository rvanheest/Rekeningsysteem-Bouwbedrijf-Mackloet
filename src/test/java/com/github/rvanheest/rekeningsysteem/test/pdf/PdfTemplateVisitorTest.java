package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.pdf.PdfTemplateVisitor;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PdfTemplateVisitorTest implements ConfigurationFixture {

  static class MockedMutationInvoice extends MutationInvoice {
    public MockedMutationInvoice() {
      super(null, null);
    }
  }

  static class MockedOffer extends Offer {
    public MockedOffer() {
      super(null, "", false);
    }
  }

  static class MockedNormalInvoice extends NormalInvoice {
    public MockedNormalInvoice() {
      super(null, "", new ItemList<>(null));
    }
  }

  static class MockedRepairInvoice extends RepairInvoice {
    public MockedRepairInvoice() {
      super(null, null);
    }
  }

  private PdfTemplateVisitor visitor;

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    this.visitor = new PdfTemplateVisitor(this.getConfiguration());
  }

  @Test
  public void testVisitMutationInvoice() throws URISyntaxException {
    assertEquals(
        this.getTestDir().resolve("pdftemplate/MutatiesFactuur.tex"),
        this.visitor.visit(mock(MockedMutationInvoice.class)));
  }

  @Test
  public void testVisitOffer() throws URISyntaxException {
    assertEquals(
        this.getTestDir().resolve("pdftemplate/Offerte.tex"),
        this.visitor.visit(mock(MockedOffer.class)));
  }

  @Test
  public void testVisitNormalInvoice() throws URISyntaxException {
    assertEquals(
        this.getTestDir().resolve("pdftemplate/ParticulierFactuur.tex"),
        this.visitor.visit(mock(MockedNormalInvoice.class)));
  }

  @Test
  public void testVisitRepairInvoice() throws URISyntaxException {
    assertEquals(
        this.getTestDir().resolve("pdftemplate/ReparatiesFactuur.tex"),
        this.visitor.visit(mock(MockedRepairInvoice.class)));
  }
}
