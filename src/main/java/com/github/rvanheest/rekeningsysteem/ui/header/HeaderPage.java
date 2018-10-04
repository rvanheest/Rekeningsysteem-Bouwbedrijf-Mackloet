package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderWithDescriptionManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractPage;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;

public final class HeaderPage extends AbstractPage {

  private static final String title = "Debiteur & Datum";

  private HeaderPage(AbstractSection... sections) {
    super(title, sections);
  }

  public static HeaderPage createInvoiceHeaderPage(SearchEngine<Debtor> searchEngine, HeaderManager headerManager) {
    return new HeaderPage(
        new DebtorSection(searchEngine, headerManager),
        new DateSection(headerManager),
        new InvoiceNumberSection(headerManager, InvoiceNumberSection.InvoiceNumberType.INVOICE)
    );
  }

  public static HeaderPage createNormalInvoiceHeaderPage(SearchEngine<Debtor> searchEngine,
      HeaderWithDescriptionManager headerManager) {
    return new HeaderPage(
        new DebtorSection(searchEngine, headerManager),
        new DateSection(headerManager),
        new InvoiceNumberSection(headerManager, InvoiceNumberSection.InvoiceNumberType.INVOICE),
        new DescriptionSection(headerManager)
    );
  }

  public static HeaderPage createOfferHeaderPage(SearchEngine<Debtor> searchEngine, HeaderManager headerManager) {
    return new HeaderPage(
        new DebtorSection(searchEngine, headerManager),
        new DateSection(headerManager),
        new InvoiceNumberSection(headerManager, InvoiceNumberSection.InvoiceNumberType.OFFER)
    );
  }
}
