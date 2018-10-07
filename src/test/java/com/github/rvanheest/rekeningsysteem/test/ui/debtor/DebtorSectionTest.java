package com.github.rvanheest.rekeningsysteem.test.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSection;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.robot.Motion;
import org.testfx.service.query.NodeQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@Category(UITest.class)
@RunWith(MockitoJUnitRunner.class)
public class DebtorSectionTest extends ApplicationTest {

  @Mock private SearchEngine<Debtor> searchEngine;
  private HeaderManager headerManager;
  private DebtorSection ui;

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
    this.ui = new DebtorSection(this.searchEngine, this.headerManager);

    Scene scene = new Scene(this.ui);
    scene.getStylesheets().addAll("searchbox.css", "section.css");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    this.ui.dispose();
  }

  @Test
  public void testSelectDebtorFromSearchBox() {
    Debtor debtor = new Debtor("my-name", "my-street", "my-number", "1239AT", "my-city", "my-vatnumber");

    when(this.searchEngine.suggest(eq(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("m"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-n"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-na"))).thenReturn(Observable.just(Collections.singletonList(debtor)));
    when(this.searchEngine.suggest(eq("my-nam"))).thenReturn(Observable.just(Collections.singletonList(debtor)));
    when(this.searchEngine.suggest(eq("my-name"))).thenReturn(Observable.just(Collections.singletonList(debtor)));

    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();
    Node searchBox = lookup(".text-field").query();

    clickOn(searchBox)
        .write("my-name")
        .sleep(500, TimeUnit.MILLISECONDS)
        .moveTo(lookup(".search-menu-item").<Node> query(), Motion.DIRECT)
        .clickOn();

    debtorTestObserver
        .assertValue(debtor)
        .assertNoErrors()
        .assertNotComplete();

    verify(this.searchEngine).suggest(eq("my-name"));
    verifyNoMoreInteractions(this.searchEngine);
  }

  @Test
  public void testUnsetSaveDebtorAfterSelectDebtorFromSearchBox() {
    Debtor debtor = new Debtor("my-name", "my-street", "my-number", "1239AT", "my-city", "my-vatnumber");

    when(this.searchEngine.suggest(eq(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("m"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-n"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("my-na"))).thenReturn(Observable.just(Collections.singletonList(debtor)));
    when(this.searchEngine.suggest(eq("my-nam"))).thenReturn(Observable.just(Collections.singletonList(debtor)));
    when(this.searchEngine.suggest(eq("my-name"))).thenReturn(Observable.just(Collections.singletonList(debtor)));

    TestObserver<Boolean> saveDebtorTestObserver = this.headerManager.storeDebtorOnSave().skip(1L).test();
    Node searchBox = lookup(".text-field").query();
    Node checkBox = lookup(".check-box").query();

    clickOn(checkBox)
        .sleep(500, TimeUnit.MILLISECONDS)
        .clickOn(searchBox)
        .write("my-name")
        .sleep(500, TimeUnit.MILLISECONDS)
        .moveTo(lookup(".search-menu-item").<Node> query(), Motion.DIRECT)
        .clickOn()
        .sleep(500, TimeUnit.MILLISECONDS);

    saveDebtorTestObserver
        .assertValues(true, false)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWriteName() {
    TestObserver<String> nameIntentObserver = this.ui.nameIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(1).<Node> query())
        .write("my-name")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    nameIntentObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-n",
            "my-na",
            "my-nam",
            "my-name",
            ""
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("my-name", "", "", "", "", Optional.empty()),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testWriteStreet() {
    TestObserver<String> streetIntentObserver = this.ui.streetIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(2).<Node> query())
        .write("my-street")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    streetIntentObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-s",
            "my-st",
            "my-str",
            "my-stre",
            "my-stree",
            "my-street",
            ""
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("", "my-street", "", "", "", Optional.empty()),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testWriteNumber() {
    TestObserver<String> numberIntentObserver = this.ui.numberIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(3).<Node> query())
        .write("my-number")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    numberIntentObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-n",
            "my-nu",
            "my-num",
            "my-numb",
            "my-numbe",
            "my-number",
            ""
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("", "", "my-number", "", "", Optional.empty()),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testWriteZipcode() {
    TestObserver<String> zipcodeIntentObserver = this.ui.zipcodeIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(4).<Node> query())
        .write("1478GR")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    zipcodeIntentObserver
        .assertValues(
            "",
            "1",
            "14",
            "147",
            "1478",
            "1478G",
            "1478GR",
            ""
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("", "", "", "1478GR", "", Optional.empty()),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testWriteCity() {
    TestObserver<String> cityIntentObserver = this.ui.cityIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(5).<Node> query())
        .write("my-city")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    cityIntentObserver
        .assertValues(
            "",
            "m",
            "my",
            "my-",
            "my-c",
            "my-ci",
            "my-cit",
            "my-city",
            ""
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("", "", "", "", "my-city", Optional.empty()),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testWriteVatNumber() {
    TestObserver<Optional<String>> vatNumberIntentObserver = this.ui.vatNumberIntent().test();
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    NodeQuery textfieldsQuery = lookup(".text-field");

    clickOn(textfieldsQuery.nth(6).<Node> query())
        .write("my-vatnumber")
        .sleep(300, TimeUnit.MILLISECONDS)
        .push(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN))
        .push(KeyCode.DELETE)
        .sleep(300, TimeUnit.MILLISECONDS);

    vatNumberIntentObserver
        .assertValues(
            Optional.empty(),
            Optional.of("m"),
            Optional.of("my"),
            Optional.of("my-"),
            Optional.of("my-v"),
            Optional.of("my-va"),
            Optional.of("my-vat"),
            Optional.of("my-vatn"),
            Optional.of("my-vatnu"),
            Optional.of("my-vatnum"),
            Optional.of("my-vatnumb"),
            Optional.of("my-vatnumbe"),
            Optional.of("my-vatnumber"),
            Optional.empty()
        )
        .assertNoErrors()
        .assertNotComplete();

    debtorTestObserver
        .assertValues(
            new Debtor("", "", "", "", "", Optional.of("my-vatnumber")),
            new Debtor("", "", "", "", "", Optional.empty())
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }

  @Test
  public void testPressSaveToggle() {
    TestObserver<Boolean> storeDebtorOnSaveTestObserver = this.headerManager.storeDebtorOnSave().skip(1L).test();

    moveTo(lookup(".check-box").<CheckBox> query())
        .clickOn()
        .sleep(100, TimeUnit.MILLISECONDS)
        .clickOn()
        .sleep(100, TimeUnit.MILLISECONDS)
        .clickOn()
        .sleep(100, TimeUnit.MILLISECONDS);

    storeDebtorOnSaveTestObserver
        .assertValues(
            true,
            false,
            true
        )
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testWriteAllFields() {
    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    clickOn(lookup(".text-field").nth(1).<Node> query())
        .write("my-name")
        .clickOn(lookup(".text-field").nth(2).<Node> query())
        .write("my-street")
        .clickOn(lookup(".text-field").nth(3).<Node> query())
        .write("my-number")
        .clickOn(lookup(".text-field").nth(4).<Node> query())
        .write("1239AT")
        .clickOn(lookup(".text-field").nth(5).<Node> query())
        .write("my-city")
        .clickOn(lookup(".text-field").nth(6).<Node> query())
        .write("my-vatnumber")
        .sleep(300, TimeUnit.MILLISECONDS);

    debtorTestObserver
        .assertValues(
            new Debtor("my-name", "", "", "", ""),
            new Debtor("my-name", "my-street", "", "", ""),
            new Debtor("my-name", "my-street", "my-number", "", ""),
            new Debtor("my-name", "my-street", "my-number", "1239AT", ""),
            new Debtor("my-name", "my-street", "my-number", "1239AT", "my-city"),
            new Debtor("my-name", "my-street", "my-number", "1239AT", "my-city", "my-vatnumber")
        )
        .assertNoErrors()
        .assertNotComplete();

    verifyZeroInteractions(this.searchEngine);
  }
}
