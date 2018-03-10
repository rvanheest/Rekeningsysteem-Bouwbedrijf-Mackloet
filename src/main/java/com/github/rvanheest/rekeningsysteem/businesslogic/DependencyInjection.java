package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.database.EsselinkItemTable;
import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.esselinkItems.EsselinkItemHandler;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.offerText.DefaultOfferTextHandler;
import com.github.rvanheest.rekeningsysteem.pdf.PdfExporter;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import com.github.rvanheest.rekeningsysteem.xml.XmlLoader;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader;
import com.github.rvanheest.rekeningsysteem.xml.XmlSaver;
import com.github.rvanheest.rekeningsysteem.xml.XmlWriter;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.util.Locale;

public class DependencyInjection implements AutoCloseable {

  private static DependencyInjection __instance;

  public static void setInstance(DependencyInjection instance) {
    __instance = instance;
  }

  public static void destroyInstance() throws Exception {
    __instance.close();
    __instance = null;
  }

  public static DependencyInjection getInstance() {
    if (__instance == null)
      throw new NullPointerException("DependencyInjection is not yet instantiated. "
          + "Use DependencyInjection.setInstance() to do so.");
    return __instance;
  }

  public DependencyInjection(PropertiesConfiguration props) {
    this.configuration = props;
    this.dbConnection = this.newDatabaseConnection();
  }

  private final PropertiesConfiguration configuration;

  private final DatabaseConnection dbConnection;
  private final DebtorTable debtorTable = new DebtorTable();
  private final EsselinkItemTable esselinkTable = new EsselinkItemTable();
  private final InvoiceNumberTable invoiceNumberTable= new InvoiceNumberTable();

  @Override
  public void close() throws Exception {
    this.dbConnection.closeConnectionPool();
  }

  private DatabaseConnection newDatabaseConnection() {
    DatabaseConnection connection = new DatabaseConnection(this.configuration);
    connection.initConnectionPool();

    return connection;
  }

  protected SearchEngine<Debtor> newDebtorSearchEngine() {
    return new DebtorSearchEngine(this.dbConnection, this.debtorTable);
  }

  protected EsselinkItemSearchEngine newEsselinkItemSearchEngine() {
    return new EsselinkItemSearchEngine(this.dbConnection, this.esselinkTable);
  }

  protected EsselinkItemHandler newEsselinkItemHandler() {
    return new EsselinkItemHandler(this.esselinkTable);
  }

  protected InvoiceNumberGenerator newInvoiceNumberGenerator() {
    return new InvoiceNumberGenerator(this.invoiceNumberTable);
  }

  protected DefaultOfferTextHandler newDefaultOfferTextHandler() {
    return new DefaultOfferTextHandler(this.configuration);
  }

  protected PdfExporter newPdfExporter() {
    return new PdfExporter(this.configuration, Locale.forLanguageTag("nl-NL"));
  }

  protected XmlLoader newXmlLoader() throws ParserConfigurationException {
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    return new XmlReader(documentBuilder, Locale.forLanguageTag("nl-NL"), Monetary.getCurrency("EUR"));
  }

  protected XmlSaver newXmlSaver() throws ParserConfigurationException {
    DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    return new XmlWriter(documentBuilder, transformerFactory);
  }

  public SearchBoxPresenter<Debtor> newDebtorSearchBoxPresenter() {
    return new SearchBoxPresenter<>(this.newDebtorSearchEngine());
  }
}
