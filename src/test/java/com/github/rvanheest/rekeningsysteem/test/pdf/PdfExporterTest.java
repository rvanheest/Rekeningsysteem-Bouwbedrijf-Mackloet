package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.pdf.PdfDocumentVisitor;
import com.github.rvanheest.rekeningsysteem.pdf.PdfExporter;
import com.github.rvanheest.rekeningsysteem.pdf.PdfTemplateVisitor;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PdfExporterTest implements TestSupportFixture {

  static class MockedOffer extends Offer {

    public MockedOffer() {
      super(null, "", false);
    }
  }

  private PdfExporter exporter;
  @Mock private PdfDocumentVisitor documentVisitor;
  @Mock private PdfTemplateVisitor templateVisitor;

  @Before
  public void setUp() throws IOException {
    this.resetTestDir();
    this.exporter = new PdfExporter(this.documentVisitor, this.templateVisitor,
        Files.createDirectories(this.getTestDir().resolve("staging")));
  }

  @Test
  public void testExport() throws Exception {
    Offer offer = mock(MockedOffer.class);
    Path template = Files.write(this.getTestDir().resolve("template.tex"), this.content(), StandardCharsets.UTF_8);

    when(offer.accept(eq(this.templateVisitor))).thenReturn(template);
    when(offer.accept(eq(this.documentVisitor))).thenReturn(converter -> {});

    Path pdf = this.getTestDir().resolve("offer.pdf");
    this.exporter.export(offer, pdf);

    assertTrue(Files.exists(pdf));
  }

  private List<String> content() {
    return Arrays.asList("\\documentclass{article}", "\\begin{document}", "", "this is a test", "", "\\end{document}");
  }
}
