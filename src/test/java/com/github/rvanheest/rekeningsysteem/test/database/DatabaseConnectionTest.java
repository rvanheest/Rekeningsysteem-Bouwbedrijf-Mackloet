package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import io.strati.functional.Try;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseConnectionTest extends DatabaseFixture {

  @Test
  public void doTransactionReturnsFunctionResult() {
    Try<String> result = this.databaseAccess.doTransaction(conn -> Try.success("foo"));
    assertTrue(result.isSuccess());
    assertEquals("foo", result.get());
  }

  @Test
  public void doTransactionFailsWhenFunctionFails() {
    Exception ex = new IllegalArgumentException("foobar");
    Try<String> result = this.databaseAccess.doTransaction(conn -> Try.failure(ex));
    assertTrue(result.isFailure());
    result.ifFailure(e -> assertEquals(ex, e));
  }

  @Test
  public void doTransactionFailsWhenFunctionClosesConnection() {
    Try<Void> result = this.databaseAccess.doTransaction(connection -> Try.ofFailable(connection::close));
    assertTrue(result.isFailure());
    result.ifFailure(e -> assertEquals(SQLException.class, e.getClass()));
  }

  @Test
  public void doTransactionRollsBackChangesWhenErrorOccurs() throws SQLException {
    try (Statement statement = this.connection.createStatement()) {
      String sql = "CREATE TABLE test_table (col1 INTEGER);";
      statement.execute(sql);
    }

    Exception exception = new IllegalArgumentException("foobar");

    Try<Void> result = this.databaseAccess.doTransaction(conn -> {
      try (Statement statement = conn.createStatement()) {
        statement.executeUpdate("INSERT INTO test_table VALUES (1);");
      }

      // check that the content was added properly
      try (Statement statement = conn.createStatement();
          ResultSet res = statement.executeQuery("SELECT * FROM test_table;")) {
        List<Integer> nums = new LinkedList<>();
        while (res.next()) {
          nums.add(res.getInt("col1"));
        }
        assertEquals(Collections.singletonList(1), nums);
      }

      return Try.failure(exception);
    });

    assertTrue(result.isFailure());
    result.ifFailure(e -> assertEquals(exception, e));

    // the current content should not have test_table in the database
    try (Statement statement = this.connection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * FROM test_table;")) {
      List<Integer> nums = new LinkedList<>();
      while (res.next()) {
        nums.add(res.getInt("col1"));
      }
      assertTrue(nums.isEmpty());
    }
  }
}
