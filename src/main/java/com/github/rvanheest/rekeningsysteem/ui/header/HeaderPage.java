package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
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
      NormalInvoiceManager invoiceManager) {
    return new HeaderPage(
        new DebtorSection(searchEngine, invoiceManager),
        new DateSection(invoiceManager),
        new InvoiceNumberSection(invoiceManager, InvoiceNumberSection.InvoiceNumberType.INVOICE),
        new DescriptionSection(invoiceManager)
    );
  }

  public static HeaderPage createOfferHeaderPage(SearchEngine<Debtor> searchEngine, OfferManager offerManager) {
    return new HeaderPage(
        new DebtorSection(searchEngine, offerManager),
        new DateSection(offerManager),
        new InvoiceNumberSection(offerManager, InvoiceNumberSection.InvoiceNumberType.OFFER),
        new SignOfferSection(offerManager)
    );
  }
}
