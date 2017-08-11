package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;

public abstract class DatabaseFixture implements TestSupportFixture {

  protected DatabaseConnection databaseAccess;
  protected Connection connection;

  @Before
  public void setUp() throws IOException {
    Path testDir = this.resetTestDir();
    Path databaseFile = testDir.resolve("database.db");
    String dbDriverClassName = "org.sqlite.JDBC";
    String dbUrl = "jdbc:sqlite:" + databaseFile;
    this.databaseAccess = new DatabaseConnection(dbDriverClassName, dbUrl) {

      @Override
      protected DatabaseConnection.ConnectionPool createConnectionPool() {
        ConnectionPool pool = super.createConnectionPool();

        try (Connection connection = pool.getConnection();
            Statement statement = connection.createStatement()) {
          String query = FileUtils.readFileToString(
              new File(getClass().getClassLoader().getResource("database/db-tables.sql").toURI()));
          statement.executeUpdate(query);

          DatabaseFixture.this.connection = pool.getConnection();
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
    this.connection.close();
    this.databaseAccess.closeConnectionPool();
  }
}
