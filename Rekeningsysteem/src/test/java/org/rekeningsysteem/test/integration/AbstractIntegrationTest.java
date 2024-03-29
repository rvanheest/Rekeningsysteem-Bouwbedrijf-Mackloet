package org.rekeningsysteem.test.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.File;

import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.exception.XmlWriteException;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.FactuurLoader;
import org.rekeningsysteem.io.FactuurSaver;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.xml.XmlReader4;
import org.rekeningsysteem.io.xml.XmlWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractIntegrationTest {

	private AbstractRekening rekening;
	private FactuurExporter exporter;
	private FactuurLoader loader;
	private FactuurSaver saver;
	private File pdfFile;
	private File xmlFile;
	@Mock private Logger logger;

	protected abstract AbstractRekening makeRekening();

	protected abstract File pdfFile();

	protected abstract File xmlFile();

	@Before
	public void setUp() throws ParserConfigurationException {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		this.rekening = this.makeRekening();
		this.exporter = new PdfExporter(false, this.logger);
		this.loader = new XmlReader4(documentBuilder);
		this.saver = new XmlWriter(documentBuilder, transformerFactory);
		this.pdfFile = this.pdfFile();
		this.xmlFile = this.xmlFile();
	}

	@Test
	public void testPdf() {
		this.exporter.export(this.rekening, this.pdfFile);

		verifyNoInteractions(this.logger);
	}

	// PdfException expected due to double spaces in filename
	@Test(expected = PdfException.class)
	public void testPdfWithDoubleSpacesInFileName() {
		File file = new File("src\\test\\resources\\pdf\\File  with double spaces.pdf");
		this.exporter.export(this.rekening, file);

		verifyNoInteractions(this.logger);
	}

	@Test
	public void testExportWithError() throws Exception {
		AbstractRekening rekening = mock(AbstractRekening.class);
		File file = mock(File.class);
		doThrow(new Exception("")).when(rekening).accept((RekeningVoidVisitor) any());

		this.exporter.export(rekening, file);

		verify(this.logger).error(anyString(), any(Exception.class));
	}

	@Test
	public void testXML() throws XmlWriteException {
		this.saver.save(this.rekening, this.xmlFile);

		this.loader.load(this.xmlFile)
			.test()
			.assertValue(this.rekening)
			.assertNoErrors()
			.assertComplete();
	}

	@Test(expected = XmlWriteException.class)
	public void testXmlWithException() throws Exception {
		AbstractRekening rekening = mock(AbstractRekening.class);
		File file = mock(File.class);

		doThrow(new Exception("")).when(rekening).accept(ArgumentMatchers.<RekeningVisitor<String>>any());

		this.saver.save(rekening, file);
	}
}
