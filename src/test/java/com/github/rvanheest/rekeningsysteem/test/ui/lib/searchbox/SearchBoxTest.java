package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBox;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;
import org.testfx.robot.Motion;
import org.testfx.service.query.NodeQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testfx.api.FxAssert.verifyThat;

@Category(UITest.class)
public class SearchBoxTest extends ApplicationTest {
  
  private SearchBox<String> ui;
  private Node textfield;
  private Node clearButton;

  private SearchBox<String> createUI() {
    return new SearchBox<String>("default text") {

      @Override
      protected SearchBoxPresenter<String> createPresenter() {
        return new SearchBoxPresenter<>(new SearchEngine<String>() {

          @Override
          public Observable<List<String>> suggest(String text) {
            if (text.length() <= 2)
              return Observable.just(Collections.emptyList());
            return Observable.range(1, 3)
                .map(i -> String.format("%d. %s", i, text))
                .toList()
                .toObservable();
          }
        });
      }

      @Override
      protected Node displaySuggestion(String suggestion) {
        return new Label(String.format("label %s", suggestion));
      }

      @Override
      protected SearchInfoBox<String> createInfoBox() {
        return new TestInfoBox();
      }
    };
  }

  @Override
  public void start(Stage stage) {
    this.ui = this.createUI();

    Scene scene = new Scene(this.ui);
    scene.getStylesheets().add("searchbox.css");
    stage.setScene(scene);
    stage.show();
  }

  @Before
  public void setUp() {
    this.textfield = lookup("#searchbox").lookup(".text-field").query();
    this.clearButton = lookup("#searchbox").lookup(".button").query();
  }

  @After
  public void tearDown() {
    this.textfield = null;
    this.clearButton = null;
  }

  @Override
  public void stop() {
    if (!this.ui.isDisposed())
      this.ui.dispose();
    this.ui = null;
  }

  private NodeQuery searchMenuItemQuery(int n) {
    return lookup(".context-menu").lookup(".search-menu-item").nth(n);
  }

  private Node searchMenuItem(int n) {
    return searchMenuItemQuery(n).query();
  }

  @Test
  public void testTypeText() {
    verifyThat(this.textfield, TextInputControlMatchers.hasText(""));

    clickOn(this.textfield)
      .write("test");

    verifyThat(this.textfield, TextInputControlMatchers.hasText("test"));
  }

  @Test
  public void testButtonVisibility() {
    verifyThat(this.clearButton, NodeMatchers.isInvisible());

    clickOn(this.textfield)
        .write("test");

    verifyThat(this.clearButton, NodeMatchers.isVisible());
  }

  @Test
  public void testStreamTypedText() {
    TestObserver<String> output = this.ui.textTypedIntent().test();

    clickOn(this.textfield)
        .write("test");

    output.assertValues("", "t", "te", "tes", "test")
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testClearTextfield() {
    clickOn(this.textfield)
        .write("test")
        .clickOn(this.clearButton);

    verifyThat(this.textfield, TextInputControlMatchers.hasText(""));
    verifyThat(this.clearButton, NodeMatchers.isInvisible());
  }

  @Test
  public void testFocusOnTextfieldAfterClear() {
    clickOn(this.textfield)
        .write("test")
        .clickOn(this.clearButton)
        .write("my text"); // no clicking on the textfield after clicking on button

    verifyThat(this.textfield, TextInputControlMatchers.hasText("my text"));
  }

  @Test
  public void testVisibleSearchResults() {
    clickOn(this.textfield)
        .write("test")
        .sleep(300, TimeUnit.MILLISECONDS);

    Window contextMenu = window(w -> w instanceof ContextMenu);
    verifyThat(contextMenu, WindowMatchers.isShowing());
  }

  @Test
  public void testSearchResultContents() {
    clickOn(this.textfield)
        .write("test")
        .sleep(300, TimeUnit.MILLISECONDS);

    List<String> texts = Arrays.asList("label 1. test", "label 2. test", "label 3. test");

    for (int i = 0; i < texts.size(); i++) {
      verifyThat(this.searchMenuItemQuery(i).lookup(".label"), LabeledMatchers.hasText(texts.get(i)));
    }
  }

  @Test
  public void testClickOnSearchResult() {
    TestObserver<String> selected = this.ui.selectedItemIntent().test();

    clickOn(this.textfield)
        .write("test")
        .sleep(300, TimeUnit.MILLISECONDS)
        .clickOn(this.searchMenuItem(1));

    selected.assertValue("2. test")
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testInvisibleAfterClickOnSearchResult() {
    clickOn(this.textfield)
        .write("test")
        .sleep(300, TimeUnit.MILLISECONDS);

    Window contextMenu = window(w -> w instanceof ContextMenu);
    clickOn(this.searchMenuItem(1));

    verifyThat(contextMenu, WindowMatchers.isNotShowing());
  }

  @Test
  public void testShowTooltipOnHover() {
    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS)
        .moveTo(this.searchMenuItem(1));

    NodeQuery tooltip = lookup("#search-info-box");
    verifyThat(tooltip, NodeMatchers.isVisible());
    verifyThat(tooltip.lookup("#search-info-name"), LabeledMatchers.hasText("2. test"));

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }

  @Test
  public void testMoveOverItemsShowTooltips() {
    clickOn(this.textfield)
        .write("test")
        .sleep(500, TimeUnit.MILLISECONDS);

    for (int i = 0; i < 3; i++) {
      moveTo(this.searchMenuItem(i), Motion.DIRECT);

      NodeQuery tooltip = lookup("#search-info-box");
      verifyThat(tooltip, NodeMatchers.isVisible());
      verifyThat(tooltip.lookup("#search-info-name"), LabeledMatchers.hasText((i + 1) + ". test"));
    }

    // clean up tooltip
    type(KeyCode.ESCAPE);
  }
}
