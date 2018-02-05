package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.debtor.DebtorInfoBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import static org.junit.Assert.assertNull;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class DebtorInfoBoxTest extends ApplicationTest {

  private DebtorInfoBox infoBox;

  private final Debtor debtor1 = new Debtor("n1", "s1", "n1", "zc1", "c1");
  private final Debtor debtor2 = new Debtor("n2", "s2", "n2", "zc2", "c2");
  private final Debtor debtor3 = new Debtor("n3", "s3", "n3", "zc3", "c3", "vn3");
  private final Debtor debtor4 = new Debtor("n4", "s4", "n4", "zc4", "c4", "vn4");

  private NodeQuery searchInfoBoxQuery() {
    return lookup("#search-info-box");
  }

  private NodeQuery searchInfoBoxNameQuery() {
    return searchInfoBoxQuery().lookup("#search-info-name");
  }

  private NodeQuery searchInfoBoxDescriptionQuery(int n) {
    return searchInfoBoxQuery().lookup("#search-info-description").nth(n);
  }

  private void verifyShowing(Debtor debtor) {
    verifyThat(searchInfoBoxNameQuery(), hasText(debtor.getName()));
    verifyThat(searchInfoBoxDescriptionQuery(0), hasText(debtor.getStreet() + " " + debtor.getNumber()));
    verifyThat(searchInfoBoxDescriptionQuery(1), hasText(debtor.getZipcode() + "  " + debtor.getCity().toUpperCase()));
    if (debtor.getVatNumber().isPresent())
      verifyThat(searchInfoBoxDescriptionQuery(2), hasText("BTW nummer: " + debtor.getVatNumber().get()));
    else
      assertNull(searchInfoBoxDescriptionQuery(2).query());
  }

  @Override
  public void start(Stage stage) {
    this.infoBox = new DebtorInfoBox();

    Scene scene = new Scene(this.infoBox);
    scene.getStylesheets().add("searchbox.css");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() {
    this.infoBox = null;
  }

  @Test
  public void testDisplayDebtor() {
    interact(() -> this.infoBox.setContent(this.debtor1));

    verifyShowing(this.debtor1);
  }

  @Test
  public void testDisplayDebtorWithVatNumber() {
    interact(() -> this.infoBox.setContent(this.debtor3));

    verifyShowing(this.debtor3);
  }

  @Test
  public void testDisplayTwoDebtorsWithoutVatNumber() {
    interact(() -> {
      this.infoBox.setContent(this.debtor1);
      this.infoBox.setContent(this.debtor2);
    });

    verifyShowing(this.debtor2);
  }

  @Test
  public void testDisplayDebtorWithoutVatNumberThenWithVatNumber() {
    interact(() -> {
      this.infoBox.setContent(this.debtor1);
      this.infoBox.setContent(this.debtor3);
    });

    verifyShowing(this.debtor3);
  }

  @Test
  public void testDisplayDebtorWithVatNumberThenWithoutVatNumber() {
    interact(() -> {
      this.infoBox.setContent(this.debtor3);
      this.infoBox.setContent(this.debtor1);
    });

    verifyShowing(this.debtor1);
  }

  @Test
  public void testDisplayTwoDebtorsWithVatNumber() {
    interact(() -> {
      this.infoBox.setContent(this.debtor3);
      this.infoBox.setContent(this.debtor4);
    });

    verifyShowing(this.debtor4);
  }
}
