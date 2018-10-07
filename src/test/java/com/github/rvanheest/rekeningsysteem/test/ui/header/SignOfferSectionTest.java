package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.header.SignOfferSection;
import io.reactivex.observers.TestObserver;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Category(UITest.class)
public class SignOfferSectionTest extends ApplicationTest {

  private OfferManager offerManager;
  private SignOfferSection ui;

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
    this.ui = new SignOfferSection(this.offerManager);

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
  public void testClickCheckBox() {
    TestObserver<Boolean> signOfferTestObserver = this.offerManager.getSign().skip(1L).test();

    CheckBox checkBox = lookup(".check-box").<CheckBox> query();
    clickOn(checkBox)
        .clickOn(checkBox)
        .clickOn(checkBox)
        .sleep(300, TimeUnit.MILLISECONDS);

    signOfferTestObserver
        .assertValues(true, false, true)
        .assertNoErrors()
        .assertNotComplete();
  }
}
