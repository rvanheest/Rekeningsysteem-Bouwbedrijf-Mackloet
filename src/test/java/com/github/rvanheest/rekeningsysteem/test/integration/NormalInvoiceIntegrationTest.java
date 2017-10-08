package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class NormalInvoiceIntegrationTest extends AbstractDocumentIntegrationTest {

  private void addArtikels(ItemList<NormalListItem> list, CurrencyUnit currency) throws DifferentCurrencyException {
    EsselinkItem sub1 = new EsselinkItem("2018021117", "Product 1", 1, "Zak", Money.of(5.16, currency));
    EsselinkItem sub2 = new EsselinkItem("2003131360", "Product 2", 1, "zak", Money.of(129.53, currency));
    EsselinkItem sub3 = new EsselinkItem("2003131060", "Product 3", 1, "set", Money.of(35.96, currency));
    EsselinkItem sub4 = new EsselinkItem("2003131306", "Product 4", 1, "zak", Money.of(9.47, currency));
    EsselinkItem sub5 = new EsselinkItem("4010272112", "Product 5", 1, "Stuks", Money.of(17.18, currency));
    EsselinkItem sub6 = new EsselinkItem("2009200131", "Product 6", 1, "Stuks", Money.of(6.84, currency));
    EsselinkItem sub7 = new EsselinkItem("2009200105", "Product 7", 1, "Stuks", Money.of(7.44, currency));

    list.add(new EsselinkListItem(sub1, 8, 19.0));
    list.add(new EsselinkListItem(sub2, 1, 19.0));
    list.add(new EsselinkListItem(sub3, 1, 19.0));
    list.add(new EsselinkListItem(sub4, 1, 19.0));
    list.add(new EsselinkListItem(sub5, 1, 19.0));
    list.add(new EsselinkListItem(sub6, 1, 19.0));
    list.add(new EsselinkListItem(sub7, 1, 19.0));
    list.add(new SimpleListItem("Stucloper + trapfolie", Money.of(15.00, currency), 19.0));
    list.add(new SimpleListItem("Kitwerk", Money.of(149.50, currency), 19.0));
  }

  private void addLoon(ItemList<NormalListItem> list, CurrencyUnit currency) throws DifferentCurrencyException {
    list.add(new HourlyWage("Uurloon à 38.50", 25, Money.of(38.50, currency), 6.0));
    list.add(new HourlyWage("test123", 12, Money.of(12.50, currency), 6.0));
    list.add(new DefaultWage("foobar", Money.of(40.00, currency), 6.0));
  }

  @Override
  protected NormalInvoice makeDocument(Header header) throws DifferentCurrencyException {
    String description = "Voor u verrichte werkzaamheden betreffende renovatie badkamervloer i.v.m. lekkage";

    CurrencyUnit currency = Monetary.getCurrency("EUR");
    ItemList<NormalListItem> itemList = new ItemList<>(currency);
    this.addArtikels(itemList, currency);
    this.addLoon(itemList, currency);

    return new NormalInvoice(header, description, itemList);
  }
}
