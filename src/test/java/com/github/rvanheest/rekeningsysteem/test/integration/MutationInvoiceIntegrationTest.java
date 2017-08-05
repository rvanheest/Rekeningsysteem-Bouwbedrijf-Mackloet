package com.github.rvanheest.rekeningsysteem.test.integration;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class MutationInvoiceIntegrationTest extends AbstractDocumentIntegrationTest {

  @Override
  protected MutationInvoice makeDocument() throws DifferentCurrencyException {
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    ItemList<MutationListItem> itemList = new ItemList<>(currency);
    itemList.add(new MutationListItem("Bonnummer", "111390", Money.of(4971.96, currency)));
    itemList.add(new MutationListItem("Bonnummer", "111477", Money.of(4820.96, currency)));
    itemList.add(new MutationListItem("Bonnummer", "112308", Money.of(5510.74, currency)));

    return new MutationInvoice(this.getHeader(), itemList);
  }
}
