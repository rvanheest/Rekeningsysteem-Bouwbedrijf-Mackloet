package com.github.rvanheest.rekeningsysteem.database;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Savepoint;
import java.util.Optional;

public class DatabaseConnection {

  protected interface ConnectionPool extends DataSource, AutoCloseable {}
  private class CommonConnectionPool extends BasicDataSource implements ConnectionPool {}

  private final String dbDriverClassName;
  private final String dbUrl;
  private final Optional<String> dbUsername;
  private final Optional<String> dbPassword;
  private ConnectionPool pool;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public DatabaseConnection(String dbDriverClassName, String dbUrl) {
    this.dbDriverClassName = dbDriverClassName;
    this.dbUrl = dbUrl;
    this.dbUsername = Optional.empty();
    this.dbPassword = Optional.empty();
  }

  public DatabaseConnection(String dbDriverClassName, String dbUrl, String dbUsername, String dbPassword) {
    this.dbDriverClassName = dbDriverClassName;
    this.dbUrl = dbUrl;
    this.dbUsername = Optional.ofNullable(dbUsername);
    this.dbPassword = Optional.ofNullable(dbPassword);
  }

  protected ConnectionPool createConnectionPool() {
    CommonConnectionPool source = new CommonConnectionPool();
    source.setDriverClassName(dbDriverClassName);
    source.setUrl(dbUrl);
    dbUsername.ifPresent(source::setUsername);
    dbPassword.ifPresent(source::setPassword);

    return source;
  }

  public void initConnectionPool() {
    logger.info("Creating database connection ...");
    pool = createConnectionPool();
    logger.info("Database connected with URL = " + dbUrl + ", user = " + dbUsername +  ", password = ****");
  }

  public void closeConnectionPool() throws Exception {
    logger.info("Closing database connection ...");
    pool.close();
    logger.info("Database connection closed");
  }

  public <T> Observable<T> doTransactionObservable(Function<Connection, Observable<T>> actionFunc) {
    return Observable.using(() -> pool.getConnection(), connection -> {
      connection.setAutoCommit(false);
      Savepoint savepoint = connection.setSavepoint();

      return actionFunc.apply(connection)
          .doOnError(e -> connection.rollback(savepoint))
          .doOnComplete(() -> {
            connection.commit();
            connection.setAutoCommit(true);
          });
    }, Connection::close);
  }

  public <T> Maybe<T> doTransactionMaybe(Function<Connection, Maybe<T>> actionFunc) {
    return Maybe.using(() -> pool.getConnection(), connection -> {
      connection.setAutoCommit(false);
      Savepoint savepoint = connection.setSavepoint();

      return actionFunc.apply(connection)
          .doOnEvent((t, e) -> {
            if (t == null && e == null) {
              connection.commit();
              connection.setAutoCommit(true);
            }
            else if (t == null && e != null) {
              connection.rollback(savepoint);
            }
          });
    }, Connection::close);
  }

  public <T> Single<T> doTransactionSingle(Function<Connection, Single<T>> actionFunc) {
    return Single.using(() -> pool.getConnection(), connection -> {
      connection.setAutoCommit(false);
      Savepoint savepoint = connection.setSavepoint();

      return actionFunc.apply(connection)
          .doOnEvent((t, e) -> {
            if (t != null && e == null) {
              connection.commit();
              connection.setAutoCommit(true);
            }
            else if (t == null && e != null) {
              connection.rollback(savepoint);
            }
          });
    }, Connection::close);
  }
}
