package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;

import java.util.Optional;

public class InvoiceNumberSectionPresenter extends BasePresenter<InvoiceNumberSection, Optional<String>> {

  private final HeaderManager headerManager;

  public InvoiceNumberSectionPresenter(HeaderManager headerManager) {
    this.headerManager = headerManager;
  }

  @Override
  protected void bindIntents() {
    subscribeViewState(this.headerManager.getHeader().map(Header::getInvoiceNumber), InvoiceNumberSection::render);
  }
}
