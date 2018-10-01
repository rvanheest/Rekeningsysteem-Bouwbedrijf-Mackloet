package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.header.InvoiceNumberSection;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class InvoiceNumberSectionPlayground extends Playground {

  @Override
  protected Parent uiElement() {
    Offer emptyOffer = new Offer(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        false
    );
    HeaderManager headerManager = new OfferManager(emptyOffer);
    InvoiceNumberSection ui = new InvoiceNumberSection(headerManager, InvoiceNumberSection.InvoiceNumberType.INVOICE);

    headerManager.getHeader().map(Header::getInvoiceNumber)
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    Button button = new Button("set invoice number");
    JavaFxObservable.actionEventsOf(button).subscribe(e -> headerManager.withInvoiceNumber("hello"));

    return new VBox(ui, button);
  }
}
