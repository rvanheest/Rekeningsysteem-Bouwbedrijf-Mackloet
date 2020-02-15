package org.rekeningsysteem.ui.list;

import javafx.scene.control.TableCell;
import org.rekeningsysteem.data.util.BtwPercentage;

public class BtwPercentageCell<T> extends TableCell<T, BtwPercentage> {

  @Override
  protected void updateItem(BtwPercentage item, boolean empty) {
    super.updateItem(item, empty);
    if (item == null)
      this.setText("");
    else {
      String s = String.valueOf(item.getPercentage()) + "%";
      this.setText(item.isVerlegd() ? s + ", verlegd" : s);
    }
  }
}
