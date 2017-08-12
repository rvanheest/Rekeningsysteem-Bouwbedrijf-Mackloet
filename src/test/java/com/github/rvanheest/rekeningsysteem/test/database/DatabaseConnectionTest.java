package com.github.rvanheest.rekeningsysteem.test.database;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseConnectionTest extends DatabaseFixture {

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    super.setUp();
  }

  @Test
  public void testDoTransactionObservableReturnsFunctionResult() {
    this.databaseAccess.doTransactionObservable(conn -> Observable.just("foo", "bar"))
        .test()
        .assertValues("foo", "bar")
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDoTransactionObservableFailsWhenFunctionFails() {
    Exception ex = new IllegalArgumentException("foobar");
    this.databaseAccess.doTransactionObservable(conn -> Observable.just(1, 2, 3).concatWith(Observable.error(ex)))
        .test()
        .assertValues(1, 2, 3)
        .assertError(ex)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionObservableFailsWhenFunctionClosesConnection() {
    this.databaseAccess.doTransactionObservable(connection -> Observable.just(1).doOnNext(t -> connection.close()))
        .test()
        .assertValue(1)
        .assertError(SQLException.class)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionObservableRollsBackChangesWhenErrorOccurs() throws SQLException {
    try (Statement statement = this.connection.createStatement()) {
      String sql = "CREATE TABLE test_table (col1 INTEGER);";
      statement.execute(sql);
    }

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionObservable(conn -> {
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

      return Observable.error(exception);
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

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

  @Test
  public void testDoTransactionSingleReturnsFunctionResult() {
    this.databaseAccess.doTransactionSingle(conn -> Single.just("foo"))
        .test()
        .assertValue("foo")
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDoTransactionSingleFailsWhenFunctionFails() {
    Exception ex = new IllegalArgumentException("foobar");
    this.databaseAccess.doTransactionSingle(conn -> Single.error(ex))
        .test()
        .assertNoValues()
        .assertError(ex)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionSingleFailsWhenFunctionClosesConnection() {
    this.databaseAccess.doTransactionSingle(connection -> Single.just(1).doOnSuccess(t -> connection.close()))
        .test()
        .assertNoValues()
        .assertError(SQLException.class)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionSingleRollsBackChangesWhenErrorOccurs() throws SQLException {
    try (Statement statement = this.connection.createStatement()) {
      String sql = "CREATE TABLE test_table (col1 INTEGER);";
      statement.execute(sql);
    }

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionSingle(conn -> {
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

      return Single.error(exception);
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

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

  @Test
  public void testDoTransactionMaybeReturnsFunctionResult() {
    this.databaseAccess.doTransactionMaybe(conn -> Maybe.just("foo"))
        .test()
        .assertValue("foo")
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDoTransaction() {
    this.databaseAccess.doTransactionMaybe(conn -> Maybe.empty())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testDoTransactionMaybeFailsWhenFunctionFails() {
    Exception ex = new IllegalArgumentException("foobar");
    this.databaseAccess.doTransactionMaybe(conn -> Maybe.error(ex))
        .test()
        .assertNoValues()
        .assertError(ex)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionMaybeFailsWhenFunctionClosesConnection() {
    this.databaseAccess.doTransactionMaybe(connection -> Maybe.fromAction(connection::close))
        .test()
        .assertNoValues()
        .assertError(SQLException.class)
        .assertNotComplete();
  }

  @Test
  public void testDoTransactionMaybeRollsBackChangesWhenErrorOccurs() throws SQLException {
    try (Statement statement = this.connection.createStatement()) {
      String sql = "CREATE TABLE test_table (col1 INTEGER);";
      statement.execute(sql);
    }

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionMaybe(conn -> {
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

      return Maybe.error(exception);
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

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
