package com.github.rvanheest.rekeningsysteem.test.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class OfferManagerTest extends AbstractDocumentManagerTest<Offer> {

  private OfferManager offerManager;

  private final Offer emptyOffer = new Offer(
      new Header(
          new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
          LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
          "12018"
      ),
      "",
      false
  );

  @Override
  protected Offer emptyDoc() {
    return this.emptyOffer;
  }

  @Override
  protected Offer createDocument(Header header) {
    return new Offer(
        header,
        this.emptyOffer.getText(),
        this.emptyOffer.isSign()
    );
  }

  @Override
  protected OfferManager createDocumentManager() {
    return new OfferManager(this.emptyOffer);
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    this.offerManager = this.createDocumentManager();
  }

  @Test
  public void testWithText() {
    TestObserver<Debtor> debtorTestObserver = this.offerManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.offerManager.getHeader().skip(1L).test();
    TestObserver<String> textTestObserver = this.offerManager.getText().skip(1L).test();
    TestObserver<Boolean> signTestObserver = this.offerManager.getSign().skip(1L).test();
    TestObserver<Offer> offerTestObserver = this.offerManager.getDocument().skip(1L).test();

    String expectedText = "my-text";
    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Offer expectedOffer = new Offer(
        expectedHeader,
        expectedText,
        false
    );

    this.offerManager.withText(expectedText)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    textTestObserver
        .assertValue(expectedText)
        .assertNoErrors()
        .assertNotComplete();

    signTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    offerTestObserver
        .assertValue(expectedOffer)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWithSign() {
    TestObserver<Debtor> debtorTestObserver = this.offerManager.getDebtor().skip(1L).test();
    TestObserver<Header> headerTestObserver = this.offerManager.getHeader().skip(1L).test();
    TestObserver<String> textTestObserver = this.offerManager.getText().skip(1L).test();
    TestObserver<Boolean> signTestObserver = this.offerManager.getSign().skip(1L).test();
    TestObserver<Offer> offerTestObserver = this.offerManager.getDocument().skip(1L).test();

    boolean expectedSign = true;
    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    Offer expectedOffer = new Offer(
        expectedHeader,
        "",
        expectedSign
    );

    this.offerManager.withSign(expectedSign)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    debtorTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    headerTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    textTestObserver
        .assertNoValues()
        .assertNoErrors()
        .assertNotComplete();

    signTestObserver
        .assertValue(expectedSign)
        .assertNoErrors()
        .assertNotComplete();

    offerTestObserver
        .assertValue(expectedOffer)
        .assertNoErrors()
        .assertNotComplete();
  }
}
