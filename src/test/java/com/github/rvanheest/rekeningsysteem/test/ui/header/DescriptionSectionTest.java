package com.github.rvanheest.rekeningsysteem.test.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.header.DescriptionSection;
import io.reactivex.observers.TestObserver;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;

import javax.money.Monetary;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Category(UITest.class)
public class DescriptionSectionTest extends ApplicationTest {

  private NormalInvoiceManager headerWithDescriptionManager;
  private DescriptionSection ui;

  @Override
  public void start(Stage stage) {
    NormalInvoice emptyInvoice = new NormalInvoice(
        new Header(
            new Debtor("", "", "", "", ""),
            LocalDate.of(2018, 7, 30)
        ),
        "",
        new ItemList<>(Monetary.getCurrency("EUR"))
    );
    this.headerWithDescriptionManager = new NormalInvoiceManager(emptyInvoice);
    this.ui = new DescriptionSection(this.headerWithDescriptionManager);

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
  public void testWriteDescription() {
    TestObserver<String> descriptionIntentObserver = this.ui.descriptionIntent().test();
    TestObserver<String> descriptionTestObserver = this.headerWithDescriptionManager.getDescription().skip(1L).test();

    clickOn(lookup(".text-area").<TextArea> query())
        .write("my-description")
        .sleep(300, TimeUnit.MILLISECONDS);

    descriptionIntentObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-d",
            "my-de",
            "my-des",
            "my-desc",
            "my-descr",
            "my-descri",
            "my-descrip",
            "my-descript",
            "my-descripti",
            "my-descriptio",
            "my-description"
        )
        .assertNoErrors()
        .assertNotComplete();

    descriptionTestObserver
        .assertValue("my-description")
        .assertNoErrors()
        .assertNotComplete();
  }
}
