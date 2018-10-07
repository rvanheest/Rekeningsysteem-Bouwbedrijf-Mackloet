package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;

import java.util.Objects;

public class DebtorSectionViewState {

  private final Debtor debtor;
  private final boolean storeDebtorOnSave;

  public DebtorSectionViewState(Debtor debtor, boolean storeDebtorOnSave) {
    this.debtor = debtor;
    this.storeDebtorOnSave = storeDebtorOnSave;
  }

  public Debtor getDebtor() {
    return this.debtor;
  }

  public boolean isStoreDebtorOnSave() {
    return this.storeDebtorOnSave;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof DebtorSectionViewState) {
      DebtorSectionViewState that = (DebtorSectionViewState) other;
      return Objects.equals(this.debtor, that.debtor)
          && Objects.equals(this.storeDebtorOnSave, that.storeDebtorOnSave);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.debtor, this.storeDebtorOnSave);
  }

  @Override
  public String toString() {
    return "<DebtorSectionViewState[" + String.valueOf(this.debtor) + ", "
        + String.valueOf(this.storeDebtorOnSave) + "]>";
  }
}
