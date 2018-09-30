package com.github.rvanheest.rekeningsysteem.ui.lib;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

public class ZipcodeTextField extends TextField {

  public ZipcodeTextField() {
    this.setTextFormatter(new TextFormatter<>(ZipcodeTextField::filter));
  }

  private static TextFormatter.Change filter(TextFormatter.Change change) {
    String newText = change.getControlNewText();
    int length = newText.length();
    if (length <= 4 && newText.matches("[0-9]*") ||
        length == 5 && newText.matches("[0-9]{4}[a-zA-Z]") ||
        length == 6 && newText.matches("[0-9]{4}[a-zA-Z]{2}")) {
      change.setText(change.getText().toUpperCase());
      return change;
    }
    else
      return null;
  }
}
