package com.github.rvanheest.rekeningsysteem.test.esselinkItems;

import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
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

  default List<EsselinkItem> getAllItemsFromTable(Connection connection) throws SQLException {
    try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Artikellijst;")) {
      List<EsselinkItem> result = new LinkedList<>();

      while (resultSet.next()) {
        String itemId = resultSet.getString("artikelnummer");
        String description = resultSet.getString("omschrijving");
        int amountPer = resultSet.getInt("prijsPer");
        String unit = resultSet.getString("eenheid");
        MonetaryAmount pricePerUnit = Money.of(resultSet.getDouble("verkoopprijs"), "EUR");

        result.add(new EsselinkItem(itemId, description, amountPer, unit, pricePerUnit));
      }

      return result;
    }
  }
}
