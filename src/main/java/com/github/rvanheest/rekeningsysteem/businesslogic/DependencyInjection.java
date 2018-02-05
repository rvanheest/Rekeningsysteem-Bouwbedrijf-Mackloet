package com.github.rvanheest.rekeningsysteem.businesslogic;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.DebtorTable;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

public class DependencyInjection {

  private static DependencyInjection __instance;

  public static void setInstance(DependencyInjection instance) {
    __instance = instance;
  }

  public static DependencyInjection getInstance() {
    if (__instance == null)
      throw new NullPointerException("DependencyInjection is not yet instantiated. "
          + "Use DependencyInjection.setInstance() to do so.");
    return __instance;
  }

  public DependencyInjection(PropertiesConfiguration props) {
    this.dbConnection = new DatabaseConnection(
        props.getString("database.driverclassname"),
        props.getString("database.url")
    );
    this.dbConnection.initConnectionPool();
  }

  public DependencyInjection(DatabaseConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  public DependencyInjection(File propsFile) throws ConfigurationException {
    this(new PropertiesConfiguration(propsFile));
  }

  private final DatabaseConnection dbConnection;
  private final DebtorTable debtorTable = new DebtorTable();

  protected SearchEngine<Debtor> newDebtorSearchEngine() {
    return new DebtorSearchEngine(this.dbConnection, this.debtorTable);
  }

  public SearchBoxPresenter<Debtor> newDebtorSearchBoxPresenter() {
    return new SearchBoxPresenter<>(this.newDebtorSearchEngine());
  }
}
