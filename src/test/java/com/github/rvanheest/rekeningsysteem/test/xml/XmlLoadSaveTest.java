package com.github.rvanheest.rekeningsysteem.test.xml;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.xml.XmlLoader;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader;
import com.github.rvanheest.rekeningsysteem.xml.XmlSaver;
import com.github.rvanheest.rekeningsysteem.xml.XmlWriter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class XmlLoadSaveTest implements TestSupportFixture {

  private XmlLoader loader;
  private XmlSaver saver;

  private static Path xml1Dir;
  private static Path xml2Dir;
  private static Path xml3Dir;
  private static Path xml4Dir;

  @BeforeClass
  public static void setUpClass() throws URISyntaxException {
    TestSupportFixture.slfBridger();
    Path xml = Paths.get(XmlReader1Test.class.getClassLoader().getResource("xml").toURI());
    xml1Dir = xml.resolve("xml1");
    xml2Dir = xml.resolve("xml2");
    xml3Dir = xml.resolve("xml3");
    xml4Dir = xml.resolve("xml4");
  }

  @Before
  public void setUp() throws ParserConfigurationException, IOException {
    this.resetTestDir();
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    this.loader = new XmlReader(documentBuilder, Locale.forLanguageTag("nl-NL"), Monetary.getCurrency("EUR"));
    this.saver = new XmlWriter(documentBuilder, transformerFactory);
  }

  private static boolean test(AbstractDocument doc1, AbstractDocument doc2) {
    assertEquals(doc1, doc2);
    return doc1.equals(doc2);
  }

  private void test(Path path1, Path path2) {
    this.loader.load(path1)
        .doOnSuccess(doc -> this.saver.save(doc, path2))
        .flatMap(doc -> this.loader.load(path2), XmlLoadSaveTest::test)
        .test()
        .assertValue(true)
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testXml1MutatiesFactuur() {
    Path path1 = xml1Dir.resolve("MutatiesFactuur.xml");
    Path path2 = this.getTestDir().resolve("MutatiesFactuur1.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml1Offer() {
    Path path1 = xml1Dir.resolve("Offerte.xml");
    Path path2 = this.getTestDir().resolve("Offerte1.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml1PartFactuur() {
    Path path1 = xml1Dir.resolve("PartFactuur.xml");
    Path path2 = this.getTestDir().resolve("PartFactuur1.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml1ParticulierFactuur1() {
    Path path1 = xml1Dir.resolve("ParticulierFactuur1.xml");
    Path path2 = this.getTestDir().resolve("ParticulierFactuur11.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml1ParticulierFactuur2() {
    Path path1 = xml1Dir.resolve("ParticulierFactuur2.xml");
    Path path2 = this.getTestDir().resolve("ParticulierFactuur21.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml1ReparatiesFactuur() {
    Path path1 = xml1Dir.resolve("ReparatiesFactuur.xml");
    Path path2 = this.getTestDir().resolve("ReparatiesFactuur1.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml2AangenomenFactuur() {
    Path path1 = xml2Dir.resolve("aangenomenFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("AangenomenFactuur2.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml2MutatiesFactuur() {
    Path path1 = xml2Dir.resolve("mutatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("MutatiesFactuur2.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml2Offer() {
    Path path1 = xml2Dir.resolve("offerteXMLTest.xml");
    Path path2 = this.getTestDir().resolve("Offerte2.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml2ParticulierFactuur() {
    Path path1 = xml2Dir.resolve("particulierFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ParticulierFactuur2.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml2ReparatiesFactuur() {
    Path path1 = xml2Dir.resolve("reparatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ReparatiesFactuur2.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml3AangenomenFactuur() {
    Path path1 = xml3Dir.resolve("aangenomenFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("AangenomenFactuur3.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml3MutatiesFactuur() {
    Path path1 = xml3Dir.resolve("mutatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("MutatiesFactuur3.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml3Offer() {
    Path path1 = xml3Dir.resolve("offerteXMLTest.xml");
    Path path2 = this.getTestDir().resolve("Offerte3.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml3ParticulierFactuur() {
    Path path1 = xml3Dir.resolve("particulierFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ParticulierFactuur3.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml3ReparatiesFactuur() {
    Path path1 = xml3Dir.resolve("reparatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ReparatiesFactuur3.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml4MutatiesFactuur() {
    Path path1 = xml4Dir.resolve("mutatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("MutatiesFactuur4.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml4Offer() {
    Path path1 = xml4Dir.resolve("offerteXMLTest.xml");
    Path path2 = this.getTestDir().resolve("Offerte4.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml4ParticulierFactuur() {
    Path path1 = xml4Dir.resolve("particulierFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ParticulierFactuur4.xml");

    this.test(path1, path2);
  }

  @Test
  public void testXml4ReparatiesFactuur() {
    Path path1 = xml4Dir.resolve("reparatiesFactuurXMLTest.xml");
    Path path2 = this.getTestDir().resolve("ReparatiesFactuur4.xml");

    this.test(path1, path2);
  }
}
