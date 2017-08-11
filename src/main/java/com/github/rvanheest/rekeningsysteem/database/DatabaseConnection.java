package com.github.rvanheest.rekeningsysteem.database;

import io.strati.functional.Try;
import io.strati.functional.function.TryFunction;
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

  public <T> Try<T> doTransaction(TryFunction<Connection, Try<T>> actionFunc) {
    return Try.ofFailable(() -> {
      try (Connection connection = pool.getConnection()) {
        connection.setAutoCommit(false);
        Savepoint savepoint = connection.setSavepoint();

        return actionFunc.apply(connection)
            .ifSuccess(connection::commit)
            .ifSuccess(() -> connection.setAutoCommit(true))
            .ifFailure(e -> connection.rollback(savepoint));
      }
    }).flatMap(x -> x);
  }
}
