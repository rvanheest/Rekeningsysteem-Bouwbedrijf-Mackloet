package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.fail;

public interface DatabaseFixture extends TestSupportFixture {

  default Path databaseFile() {
    return this.getTestDir().resolve("database.db");
  }

  default DatabaseConnection initDatabaseConnection() throws Exception {
    this.resetTestDir();

    DatabaseConnection databaseAccess = new DatabaseConnection(
        "org.sqlite.JDBC",
        String.format("jdbc:sqlite:%s", this.databaseFile())) {

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

    databaseAccess.initConnectionPool();

    return databaseAccess;
  }
}
