package com.github.rvanheest.rekeningsysteem.test.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.OfferManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSearchBox;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.robot.Motion;
import org.testfx.service.query.NodeQuery;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@RunWith(MockitoJUnitRunner.class)
public class DebtorSearchBoxTest extends ApplicationTest {

  @Mock private SearchEngine<Debtor> searchEngine;
  private HeaderManager headerManager;
  private DebtorSearchBox searchBox;
  private Node textfield;

  private List<Debtor> testDebtors() {
    return Arrays.asList(
        new Debtor("n1", "s1", "n1", "zc1", "c1"),
        new Debtor("n2", "s2", "n2", "zc2", "c2", "v2"),
        new Debtor("n3", "s3", "n3", "zc3", "c3", "v3")
    );
  }

  private NodeQuery searchMenuItemsQuery() {
    return lookup(".context-menu").lookup(".search-menu-item");
  }

  private NodeQuery searchMenuItemQuery(int n) {
    return this.searchMenuItemsQuery().nth(n);
  }

  private Node searchMenuItem(int n) {
    return this.searchMenuItemQuery(n).query();
  }

  private NodeQuery searchInfoBoxQuery() {
    return lookup("#search-info-box");
  }

  private NodeQuery searchInfoBoxNameQuery() {
    return searchInfoBoxQuery().lookup("#search-info-name");
  }

  private NodeQuery searchInfoBoxDescriptionQuery(int n) {
    return searchInfoBoxQuery().lookup("#search-info-description").nth(n);
  }

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
    this.searchBox = new DebtorSearchBox(this.searchEngine, this.headerManager);

    Scene scene = new Scene(this.searchBox);
    scene.getStylesheets().add("searchbox.css");
    stage.setScene(scene);
    stage.show();

    this.textfield = lookup("#searchbox").lookup(".text-field").query();
  }

  @Override
  public void stop() {
    this.textfield = null;

    if (!this.searchBox.isDisposed())
      this.searchBox.dispose();
    this.searchBox = null;
  }

  @Test
  public void testTypeTooLess() {
    when(this.searchEngine.suggest(eq(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("t"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("te"))).thenReturn(Observable.just(Collections.emptyList()));

    clickOn(this.textfield)
        .write("te")
        .sleep(500, TimeUnit.MILLISECONDS);

    verify(this.searchEngine, never()).suggest(eq(""));
    verify(this.searchEngine, never()).suggest(eq("t"));
    verify(this.searchEngine, times(1)).suggest(eq("te"));
    assertTrue(searchMenuItemsQuery().queryAll().isEmpty());
  }

  @Test
  public void testPerformSearch() {
    List<Debtor> debtors = this.testDebtors();
    when(this.searchEngine.suggest(eq(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("t"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("te"))).thenReturn(Observable.just(Collections.emptyList()));
    when(this.searchEngine.suggest(eq("tes"))).thenReturn(Observable.just(debtors.subList(0, 2)));
    when(this.searchEngine.suggest(eq("test"))).thenReturn(Observable.just(debtors));

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS);

    List<String> names = debtors.stream().map(Debtor::getName).collect(Collectors.toList());
    for (int i = 0; i < names.size(); i++) {
      verifyThat(searchMenuItemQuery(i).lookup(".label"), hasText(names.get(i)));
    }

    verify(this.searchEngine, never()).suggest(eq(""));
    verify(this.searchEngine, never()).suggest(eq("t"));
    verify(this.searchEngine, never()).suggest(eq("te"));
    verify(this.searchEngine, never()).suggest(eq("tes"));
    verify(this.searchEngine, times(1)).suggest(eq("test"));

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testShowInfoBox() {
    List<Debtor> debtors = this.testDebtors();
    when(this.searchEngine.suggest(eq("test"))).thenReturn(Observable.just(debtors));

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS)
        .moveTo(this.searchMenuItem(0));

    Debtor debtor = debtors.get(0);
    verifyThat(searchInfoBoxQuery(), NodeMatchers.isVisible());
    verifyThat(searchInfoBoxNameQuery(), hasText(debtor.getName()));
    verifyThat(searchInfoBoxDescriptionQuery(0), hasText(debtor.getStreet() + " " + debtor.getNumber()));
    verifyThat(searchInfoBoxDescriptionQuery(1), hasText(debtor.getZipcode() + "  " + debtor.getCity().toUpperCase()));
    assertNull(searchInfoBoxDescriptionQuery(2).query());

    verify(this.searchEngine).suggest(any());

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testMoveOverItemsShowTooltips() {
    List<Debtor> debtors = this.testDebtors();
    when(this.searchEngine.suggest(eq("test"))).thenReturn(Observable.just(debtors));

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS);

    moveTo(this.searchMenuItem(0), Motion.DIRECT);
    assertNull(searchInfoBoxDescriptionQuery(2).query());

    moveTo(this.searchMenuItem(1), Motion.DIRECT);
    Debtor debtor1 = debtors.get(1);
    Optional<String> vatNumber1 = debtor1.getVatNumber();
    if (vatNumber1.isPresent())
      verifyThat(searchInfoBoxDescriptionQuery(2), hasText("BTW nummer: " + vatNumber1.get()));
    else
      fail(String.format("No VatNumber in debtor1: %s", debtor1));

    moveTo(this.searchMenuItem(2), Motion.DIRECT);
    Debtor debtor2 = debtors.get(2);
    Optional<String> vatNumber2 = debtor2.getVatNumber();
    if (vatNumber2.isPresent())
      verifyThat(searchInfoBoxDescriptionQuery(2), hasText("BTW nummer: " + vatNumber2.get()));
    else
      fail(String.format("No VatNumber in debtor2: %s", debtor2));

    verify(this.searchEngine).suggest(eq("test"));

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testClickOnSearchResult() {
    List<Debtor> debtors = this.testDebtors();
    when(this.searchEngine.suggest(eq("test"))).thenReturn(Observable.just(debtors));

    TestObserver<Debtor> debtorTestObserver = this.headerManager.getDebtor().skip(1L).test();

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS)
        .moveTo(this.searchMenuItem(2), Motion.DIRECT)
        .clickOn();

    debtorTestObserver
        .assertValue(debtors.get(2))
        .assertNoErrors()
        .assertNotComplete();
  }
}
