package com.github.rvanheest.rekeningsysteem.test.esselinkItems;

import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import org.javamoney.moneta.Money;

import java.util.Arrays;
import java.util.List;

public interface EsselinkItemFixture {

  default List<EsselinkItem> getEsselinkItems() {
    return Arrays.asList(
        new EsselinkItem("123456", "test123", 13, "foo", Money.of(15.93, "EUR")),
        new EsselinkItem("456789", "test456", 10, "bar", Money.of(15.93, "EUR")),
        new EsselinkItem("789123", "test789", 10, "baz", Money.of(15.93, "EUR")),
        new EsselinkItem("147258", "test147", 17, "qux", Money.of(15.93, "EUR")),
        new EsselinkItem("258369", "test258", 11, "quux", Money.of(15.93, "EUR")),
        new EsselinkItem("369147", "test369", 19, "corge", Money.of(15.93, "EUR")),
        new EsselinkItem("159357", "test159", 19, "grault", Money.of(15.93, "EUR")),
        new EsselinkItem("357159", "test357", 12, "garply", Money.of(15.93, "EUR")));
  }
}
