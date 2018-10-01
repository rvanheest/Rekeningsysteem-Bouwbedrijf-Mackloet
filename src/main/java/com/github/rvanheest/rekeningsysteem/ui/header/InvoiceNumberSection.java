package com.github.rvanheest.rekeningsysteem.ui.header;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.ui.lib.AbstractSection;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.disposables.Disposable;
import javafx.scene.control.Label;

import java.util.Optional;

public class InvoiceNumberSection extends AbstractSection implements View, Disposable {

  private final String emptyText;
  private final Label invoiceNumberLabel = new Label();

  private final InvoiceNumberSectionPresenter presenter;

  public InvoiceNumberSection(HeaderManager headerManager, InvoiceNumberType type) {
    super(type.getName());

    this.emptyText = String.format("Er is nog geen %s toegekend aan deze %s", type.getNameLowercase(), type.getType());
    this.presenter = new InvoiceNumberSectionPresenter(headerManager);
    this.presenter.attachView(this);

    this.getChildren().add(this.invoiceNumberLabel);
  }

  public void render(Optional<String> invoiceNumber) {
    this.invoiceNumberLabel.setText(invoiceNumber.orElse(this.emptyText));
  }

  @Override
  public void dispose() {
    this.presenter.dispose();
  }

  @Override
  public boolean isDisposed() {
    return this.presenter.isDisposed();
  }

  public enum InvoiceNumberType {
    INVOICE("Factuurnummer", "factuurnummer", "factuur"),
    OFFER("Offertenummer", "offertenummer", "offerte");

    private final String name;
    private final String name_lowercase;
    private final String type;

    InvoiceNumberType(String name, String name_lowercase, String type) {
      this.name = name;
      this.name_lowercase = name_lowercase;
      this.type = type;
    }

    public String getName() {
      return this.name;
    }

    public String getNameLowercase() {
      return this.name_lowercase;
    }

    public String getType() {
      return this.type;
    }
  }
}
