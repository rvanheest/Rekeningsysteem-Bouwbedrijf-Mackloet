package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.header.DateSection;
import io.reactivex.observers.TestObserver;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@Category(UITest.class)
public class DateSectionTest extends ApplicationTest {

  private HeaderManager headerManager;
  private DateSection ui;

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
    this.headerManager = new OfferManager(offer);
    this.ui = new DateSection(this.headerManager);

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
  public void testRenderDate() {
    LocalDate date = LocalDate.of(2018, 7, 30);
    String dateString = DateTimeFormatter.ofPattern("dd MMMM yyyy").format(date);
    this.headerManager.withDate(date);

    TextField tf = lookup(".date-picker").lookup(".date-picker-display-node").query();
    assertEquals(dateString, tf.getText());
  }

  @Test
  public void testChooseDate() {
    this.ui.render(LocalDate.of(2018, 7, 30));
    TestObserver<LocalDate> headerDateTestObserver = this.headerManager.getHeader()
        .map(Header::getDate)
        .skip(1L)
        .test();

    clickOn(lookup(".date-picker").lookup(".arrow-button").<StackPane> query())
        .clickOn(lookup(".date-picker-popup").lookup(".calendar-grid").lookup(".day-cell").nth(36).<DateCell> query())
        .sleep(300, TimeUnit.MILLISECONDS);

    headerDateTestObserver
        .assertValue(LocalDate.of(2018, 7, 31))
        .assertNoErrors()
        .assertNotComplete();
  }
}
