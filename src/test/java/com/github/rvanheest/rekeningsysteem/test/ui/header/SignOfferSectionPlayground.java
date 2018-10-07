package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.ui.Playground;
import com.github.rvanheest.rekeningsysteem.ui.header.SignOfferSection;
import javafx.scene.Parent;

import java.time.LocalDate;

public class SignOfferSectionPlayground extends Playground {

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
    OfferManager headerManager = new OfferManager(emptyOffer);
    SignOfferSection ui = new SignOfferSection(headerManager);

    headerManager.getSign()
        .subscribe(
            System.out::println,
            System.err::println,
            () -> System.out.println("COMPLETED IS NOT EXPECTED TO EVER HAPPEN!!!")
        );

    return ui;
  }
}
