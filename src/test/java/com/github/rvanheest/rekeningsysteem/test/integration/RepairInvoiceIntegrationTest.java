package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class RepairInvoiceIntegrationTest extends AbstractDocumentIntegrationTest {

  private void addItems(ItemList<RepairListItem> list, CurrencyUnit currency) throws DifferentCurrencyException {
    list.add(new RepairListItem("Bonnummer", "110543", Money.of(77.00, currency), Money.of(6.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111558", Money.of(77.00, currency), Money.of(9.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111518", Money.of(57.75, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111660", Money.of(77.00, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111563", Money.of(115.50, currency), Money.of(13.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111625", Money.of(57.75, currency), Money.of(15.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111764", Money.of(77.00, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111751", Money.of(77.00, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111745", Money.of(38.50, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111571", Money.of(57.75, currency), Money.of(3.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111876", Money.of(77.00, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111716", Money.of(154.00, currency), Money.of(7.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111854", Money.of(154.00, currency), Money.of(183.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111912", Money.of(38.50, currency), Money.of(9.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111940", Money.of(154.00, currency), Money.of(9.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111928", Money.of(77.00, currency), Money.of(4.50, currency)));
    list.add(new RepairListItem("Bonnummer", "111723", Money.of(115.50, currency), Money.of(0.00, currency)));
    list.add(new RepairListItem("Bonnummer", "111963", Money.of(299.26, currency), Money.of(448.88, currency)));
    list.add(new RepairListItem("Bonnummer", "111739", Money.of(408.16, currency), Money.of(136.52, currency)));
    list.add(new RepairListItem("Bonnummer", "111091", Money.of(1451.27, currency), Money.of(967.51, currency)));
    list.add(new RepairListItem("Bonnummer", "111409", Money.of(2546.57, currency), Money.of(1697.72, currency)));
    list.add(new RepairListItem("Bonnummer", "111272", Money.of(3630.66, currency), Money.of(2420.44, currency)));
    list.add(new RepairListItem("Bonnummer", "111148", Money.of(3878.20, currency), Money.of(2585.46, currency)));
  }

  @Override
  protected RepairInvoice makeDocument(Header header) throws DifferentCurrencyException {
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    ItemList<RepairListItem> itemList = new ItemList<>(currency);
    this.addItems(itemList, currency);
    this.addItems(itemList, currency);
    this.addItems(itemList, currency);

    return new RepairInvoice(header, itemList);
  }
}
