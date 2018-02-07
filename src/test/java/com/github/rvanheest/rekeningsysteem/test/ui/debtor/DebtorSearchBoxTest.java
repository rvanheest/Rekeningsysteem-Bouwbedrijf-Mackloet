package com.github.rvanheest.rekeningsysteem.test.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.DependencyInjection;
import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.test.TestDependencyInjection;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorSearchBox;
import io.reactivex.Observable;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@RunWith(MockitoJUnitRunner.class)
public class DebtorSearchBoxTest extends ApplicationTest {

  @Mock private SearchEngine<Debtor> searchEngine;
  private DebtorSearchBox searchBox;
  private Node textfield;

  private void setDependencyInjection() {
    DependencyInjection injection = new TestDependencyInjection() {

      @Override
      protected SearchEngine<Debtor> newDebtorSearchEngine() {
        return searchEngine;
      }
    };
    DependencyInjection.setInstance(injection);
  }

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
    this.setDependencyInjection();
    this.searchBox = new DebtorSearchBox();

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
    when(searchEngine.suggest(matches(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(searchEngine.suggest(matches("t"))).thenReturn(Observable.just(Collections.emptyList()));
    when(searchEngine.suggest(matches("te"))).thenReturn(Observable.just(Collections.emptyList()));

    clickOn(this.textfield)
        .write("te")
        .sleep(500, TimeUnit.MILLISECONDS);

    verify(searchEngine, never()).suggest(matches(""));
    verify(searchEngine, never()).suggest(matches("t"));
    verify(searchEngine, times(1)).suggest(matches("te"));
    assertTrue(searchMenuItemsQuery().queryAll().isEmpty());
  }

  @Test
  public void testPerformSearch() {
    List<Debtor> debtors = this.testDebtors();
    when(searchEngine.suggest(matches(""))).thenReturn(Observable.just(Collections.emptyList()));
    when(searchEngine.suggest(matches("t"))).thenReturn(Observable.just(Collections.emptyList()));
    when(searchEngine.suggest(matches("te"))).thenReturn(Observable.just(Collections.emptyList()));
    when(searchEngine.suggest(matches("tes"))).thenReturn(Observable.just(debtors.subList(0, 2)));
    when(searchEngine.suggest(matches("test"))).thenReturn(Observable.just(debtors));

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS);

    List<String> names = debtors.stream().map(Debtor::getName).collect(Collectors.toList());
    for (int i = 0; i < names.size(); i++) {
      verifyThat(searchMenuItemQuery(i).lookup(".label"), hasText(names.get(i)));
    }

    verify(searchEngine, never()).suggest(matches(""));
    verify(searchEngine, never()).suggest(matches("t"));
    verify(searchEngine, never()).suggest(matches("te"));
    verify(searchEngine, never()).suggest(matches("tes"));
    verify(searchEngine, times(1)).suggest(matches("test"));

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testShowInfoBox() {
    List<Debtor> debtors = this.testDebtors();
    when(searchEngine.suggest(matches("test"))).thenReturn(Observable.just(debtors));

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

    verify(searchEngine).suggest(any());

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testMoveOverItemsShowTooltips() {
    List<Debtor> debtors = this.testDebtors();
    when(searchEngine.suggest(matches("test"))).thenReturn(Observable.just(debtors));

    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS);

    moveTo(this.searchMenuItem(0), Motion.DIRECT);
    assertNull(searchInfoBoxDescriptionQuery(2).query());

    moveTo(this.searchMenuItem(1), Motion.DIRECT);
    verifyThat(searchInfoBoxDescriptionQuery(2), hasText("BTW nummer: " + debtors.get(1).getVatNumber().get()));

    moveTo(this.searchMenuItem(2), Motion.DIRECT);
    verifyThat(searchInfoBoxDescriptionQuery(2), hasText("BTW nummer: " + debtors.get(2).getVatNumber().get()));

    verify(searchEngine).suggest(matches("test"));

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }
}
