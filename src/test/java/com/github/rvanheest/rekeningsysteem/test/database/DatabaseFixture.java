package com.github.rvanheest.rekeningsysteem.test.database;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;

public abstract class DatabaseFixture implements TestSupportFixture {

  protected DatabaseConnection databaseAccess;

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    this.databaseAccess = new DatabaseConnection(
        "org.sqlite.JDBC",
        "jdbc:sqlite:" + this.getTestDir().resolve("database.db")) {

      @Override
      protected DatabaseConnection.ConnectionPool createConnectionPool() {
        ConnectionPool pool = super.createConnectionPool();

        try (Connection connection = pool.getConnection();
            Statement statement = connection.createStatement()) {
          String query = FileUtils.readFileToString(
              new File(getClass().getClassLoader().getResource("database/db-tables.sql").toURI()));
          statement.executeUpdate(query);
        }
        catch (SQLException | URISyntaxException | IOException e) {
          fail(e.getMessage());
        }

        return pool;
      }
    };
    this.databaseAccess.initConnectionPool();
  }

  @After
  public void tearDown() throws Exception {
    this.databaseAccess.closeConnectionPool();
  }
}
