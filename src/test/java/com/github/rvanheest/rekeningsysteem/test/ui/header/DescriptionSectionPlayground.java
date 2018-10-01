package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.DescriptionManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.header.DescriptionSection;

import javax.money.Monetary;
import java.time.LocalDate;

public class DescriptionSectionPlayground extends Playground {

  @Override
  protected DescriptionSection uiElement() {
    NormalInvoice emptyInvoice = new NormalInvoice(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        new ItemList<>(Monetary.getCurrency("EUR"))
    );
    DescriptionManager descriptionManager = new NormalInvoiceManager(emptyInvoice);
    DescriptionSection ui = new DescriptionSection(descriptionManager);

    descriptionManager.getDescription()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return ui;
  }
}
