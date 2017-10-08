package com.github.rvanheest.rekeningsysteem.test.database;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DatabaseConnectionTest extends DatabaseFixture {

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
    this.databaseAccess.doTransactionObservable(conn -> Observable.just(1, 2, 3)
        .concatWith(Observable.error(ex)))
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
    this.databaseAccess.doTransactionCompletable(connection -> {
      String query = "CREATE TABLE test_table (col1 INTEGER);";
      return Completable.using(connection::createStatement,
          statement -> Completable.fromAction(() -> statement.execute(query)),
          Statement::close);
    })
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionObservable(conn -> {
      try {
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
      }
      catch (SQLException e) {
        return Observable.error(e);
      }
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

    // the current content should not have test_table in the database
    this.databaseAccess.doTransactionMaybe(connection -> Maybe.using(connection::createStatement, statement -> {
      String query = "SELECT COUNT(*) FROM test_table;";
      io.reactivex.functions.Function<ResultSet, Maybe<Integer>> extractResult = resultSet -> {
        if (resultSet.next())
          return Maybe.just(resultSet.getInt(1));
        else
          return Maybe.empty();
      };
      return Maybe.using(() -> statement.executeQuery(query), extractResult, ResultSet::close);
    }, Statement::close)).test().assertValue(0).assertNoErrors().assertComplete();
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
    this.databaseAccess.doTransactionCompletable(connection -> {
      String query = "CREATE TABLE test_table (col1 INTEGER);";
      return Completable.using(connection::createStatement,
          statement -> Completable.fromAction(() -> statement.execute(query)),
          Statement::close);
    })
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionSingle(conn -> {
      try {
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
      }
      catch (SQLException e) {
        return Single.error(e);
      }
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

    // the current content should not have test_table in the database
    this.databaseAccess.doTransactionMaybe(connection -> Maybe.using(connection::createStatement, statement -> {
      String query = "SELECT COUNT(*) FROM test_table;";
      io.reactivex.functions.Function<ResultSet, Maybe<Integer>> extractResult = resultSet -> {
        if (resultSet.next())
          return Maybe.just(resultSet.getInt(1));
        else
          return Maybe.empty();
      };
      return Maybe.using(() -> statement.executeQuery(query), extractResult, ResultSet::close);
    }, Statement::close)).test().assertValue(0).assertNoErrors().assertComplete();
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
    this.databaseAccess.doTransactionCompletable(connection -> {
      String query = "CREATE TABLE test_table (col1 INTEGER);";
      return Completable.using(connection::createStatement,
          statement -> Completable.fromAction(() -> statement.execute(query)),
          Statement::close);
    })
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    Exception exception = new IllegalArgumentException("foobar");

    this.databaseAccess.doTransactionMaybe(conn -> {
      try {
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
      }
      catch (SQLException e) {
        return Maybe.error(e);
      }
    })
        .test()
        .assertNoValues()
        .assertError(exception)
        .assertNotComplete();

    // the current content should not have test_table in the database
    this.databaseAccess.doTransactionMaybe(connection -> Maybe.using(connection::createStatement, statement -> {
      String query = "SELECT COUNT(*) FROM test_table;";
      io.reactivex.functions.Function<ResultSet, Maybe<Integer>> extractResult = resultSet -> {
        if (resultSet.next())
          return Maybe.just(resultSet.getInt(1));
        else
          return Maybe.empty();
      };
      return Maybe.using(() -> statement.executeQuery(query), extractResult, ResultSet::close);
    }, Statement::close)).test().assertValue(0).assertNoErrors().assertComplete();
  }
}
