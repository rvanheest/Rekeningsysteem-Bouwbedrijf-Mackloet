package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.header.InvoiceNumberSection;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@Category(UITest.class)
public class InvoiceNumberSectionTest extends ApplicationTest {

  private HeaderManager headerManager;
  private InvoiceNumberSection ui;

  @Override
  public void start(Stage stage) {
    Offer offer = new Offer(
        new Header(
            new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
            LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE)
            // no invoiceNumber yet
        ),
        "",
        false
    );
    this.headerManager = new OfferManager(offer);
    this.ui = new InvoiceNumberSection(this.headerManager, InvoiceNumberSection.InvoiceNumberType.INVOICE);
    Button button = new Button("assign invoice number");
    button.addEventHandler(ActionEvent.ACTION, e -> this.headerManager.withInvoiceNumber("my-invoice-number"));

    Scene scene = new Scene(new VBox(this.ui, button));
    scene.getStylesheets().add("section.css");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    this.ui.dispose();
  }

  @Test
  public void testRenderEmptyState() {
    verifyThat(lookup(".label").nth(1), hasText("Er is nog geen factuurnummer toegekend aan deze factuur"));
  }

  @Test
  public void testRenderAssignedState() {
    clickOn(lookup(".button").<Button> query());

    verifyThat(lookup(".label").nth(1), hasText("my-invoice-number"));
  }
}
