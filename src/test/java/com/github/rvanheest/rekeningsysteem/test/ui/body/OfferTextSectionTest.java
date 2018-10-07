package com.github.rvanheest.rekeningsysteem.test.ui.body;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.body.OfferTextSection;
import com.github.rvanheest.rekeningsysteem.ui.header.DateSection;
import io.reactivex.observers.TestObserver;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Category(UITest.class)
public class OfferTextSectionTest extends ApplicationTest {

  private OfferManager offerManager;
  private OfferTextSection ui;

  @Override
  public void start(Stage stage) {
    Offer offer = new Offer(
        new Header(
            new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
            LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
            "12018"
        ),
        "",
        false
    );
    this.offerManager = new OfferManager(offer);
    this.ui = new OfferTextSection(this.offerManager);

    Scene scene = new Scene(this.ui);
    scene.getStylesheets().add("section.css");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    this.ui.dispose();
  }

  @Test
  public void testWriteOfferText() {
    TestObserver<String> offerTextIntentTestObserver = this.ui.offerTextIntent().test();
    TestObserver<String> offerTextTestObserver = this.offerManager.getText().skip(1L).test();

    clickOn(lookup(".text-area").<TextArea> query())
        .write("my-offer-text")
        .sleep(300, TimeUnit.MILLISECONDS);

    offerTextIntentTestObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-o",
            "my-of",
            "my-off",
            "my-offe",
            "my-offer",
            "my-offer-",
            "my-offer-t",
            "my-offer-te",
            "my-offer-tex",
            "my-offer-text"
        )
        .assertNoErrors()
        .assertNotComplete();

    offerTextTestObserver
        .assertValue("my-offer-text")
        .assertNoErrors()
        .assertNotComplete();
  }
}
