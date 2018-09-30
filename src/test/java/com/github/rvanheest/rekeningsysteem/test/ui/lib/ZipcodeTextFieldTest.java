package com.github.rvanheest.rekeningsysteem.test.ui.lib;

import com.github.rvanheest.rekeningsysteem.test.UITest;
import com.github.rvanheest.rekeningsysteem.ui.lib.ZipcodeTextField;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.TextInputControlMatchers;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.DELETE;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.V;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static org.testfx.api.FxAssert.verifyThat;

@Category(UITest.class)
public class ZipcodeTextFieldTest extends ApplicationTest {

  private ZipcodeTextField ui;
  private TextField helper;

  @Override
  public void start(Stage stage) {
    this.ui = new ZipcodeTextField();
    this.helper = new TextField("hello");

    stage.setScene(new Scene(new VBox(this.ui, this.helper)));
    stage.show();

    verifyThat(this.ui, TextInputControlMatchers.hasText(""));
  }

  @Test
  public void testTypeValidZipcode() {
    String input = "7890AB";
    clickOn(this.ui)
        .write(input);

    verifyThat(this.ui, TextInputControlMatchers.hasText(input));
  }

  @Test
  public void testLowercaseBecomesUppercase() {
    clickOn(this.ui)
        .write("7890ab");

    verifyThat(this.ui, TextInputControlMatchers.hasText("7890AB"));
  }

  @Test
  public void testTypeTooLong() {
    clickOn(this.ui)
        .write("7890ABCDEF");

    verifyThat(this.ui, TextInputControlMatchers.hasText("7890AB"));
  }

  @Test
  public void testTypeInBetween() {
    clickOn(this.ui)
        .write("7890AB")
        .type(LEFT, 4)
        .write('4');

    verifyThat(this.ui, TextInputControlMatchers.hasText("7890AB"));
  }

  @Test
  public void testTypeAlphaOnFirstNumericSpot() {
    clickOn(this.ui)
        .write("A");

    verifyThat(this.ui, TextInputControlMatchers.hasText(""));
  }

  @Test
  public void testTypeAlphaOnMiddleNumericSpot() {
    clickOn(this.ui)
        .write("12")
        .write("A");

    verifyThat(this.ui, TextInputControlMatchers.hasText("12"));
  }

  @Test
  public void testTypeAlphaOnLastNumericSpot() {
    clickOn(this.ui)
        .write("123")
        .write("A");

    verifyThat(this.ui, TextInputControlMatchers.hasText("123"));
  }

  @Test
  public void testTypeNumericOnFirstAlphaSpot() {
    clickOn(this.ui)
        .write("1234")
        .write("5");

    verifyThat(this.ui, TextInputControlMatchers.hasText("1234"));
  }

  @Test
  public void testTypeNumericOnSecondAlphaSpot() {
    clickOn(this.ui)
        .write("1234A")
        .write("5");

    verifyThat(this.ui, TextInputControlMatchers.hasText("1234A"));
  }

  @Test
  public void testPasteValidFullSelection() {
    clickOn(this.ui)
        .write("1234AB");

    verifyThat(this.ui, TextInputControlMatchers.hasText("1234AB"));

    clickOn(this.helper)
        .push(new KeyCodeCombination(A, CONTROL_DOWN))
        .type(DELETE)
        .write("9876ZY")
        .push(new KeyCodeCombination(A, CONTROL_DOWN))
        .push(new KeyCodeCombination(C, CONTROL_DOWN))
        .clickOn(this.ui)
        .push(new KeyCodeCombination(A, CONTROL_DOWN))
        .push(new KeyCodeCombination(V, CONTROL_DOWN));

    verifyThat(this.ui, TextInputControlMatchers.hasText("9876ZY"));
  }

  @Test
  public void testPasteInvalidFullSelection() {
    clickOn(this.ui)
        .write("1234AB");

    verifyThat(this.ui, TextInputControlMatchers.hasText("1234AB"));

    clickOn(this.helper)
        .push(new KeyCodeCombination(A, CONTROL_DOWN))
        .push(new KeyCodeCombination(C, CONTROL_DOWN))
        .clickOn(this.ui)
        .push(new KeyCodeCombination(V, CONTROL_DOWN));

    verifyThat(this.ui, TextInputControlMatchers.hasText("1234AB"));
  }
}
