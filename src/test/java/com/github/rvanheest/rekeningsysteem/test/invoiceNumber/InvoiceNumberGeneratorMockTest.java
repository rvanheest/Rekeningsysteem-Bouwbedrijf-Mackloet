package com.github.rvanheest.rekeningsysteem.test.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import io.reactivex.Maybe;
import io.reactivex.Single;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.function.Function;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceNumberGeneratorMockTest implements DatabaseFixture {

  private DatabaseConnection databaseAccess;
  @Mock private InvoiceNumberTable table;
  private InvoiceNumberGenerator generator;
  private final int yearNow = LocalDate.now().getYear();

  @Before
  public void setUp() throws Exception {
    this.databaseAccess = this.initDatabaseConnection();
    this.generator = new InvoiceNumberGenerator(this.table);
  }

  @After
  public void tearDown() throws Exception {
    this.databaseAccess.closeConnectionPool();
  }

  @Test
  public void testGenerateNextInvoiceNumber() throws Exception {
    Function<Connection, Maybe<InvoiceNumber>> mock1 = mock(Function.class);
    when(this.table.getInvoiceNumber()).thenReturn(mock1);
    when(mock1.apply(any(Connection.class))).thenReturn(Maybe.just(new InvoiceNumber(14, this.yearNow)));

    Function<Connection, Single<InvoiceNumber>> mock2 = mock(Function.class);
    when(this.table.setInvoiceNumber(eq(new InvoiceNumber(15, this.yearNow)))).thenReturn(mock2);
    when(mock2.apply(any(Connection.class))).thenReturn(Single.just(new InvoiceNumber(15, this.yearNow)));

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
        .test()
        .assertValue("15" + this.yearNow)
        .assertNoErrors()
        .assertComplete();

    verify(this.table).getInvoiceNumber();
    verify(mock1).apply(any(Connection.class));
    verify(this.table).setInvoiceNumber(eq(new InvoiceNumber(15, this.yearNow)));
    verify(mock2).apply(any(Connection.class));
    verifyNoMoreInteractions(this.table);
  }

  @Test
  public void testGenerateNextInvoiceNumberErrorInSetInvoiceNumber() throws Exception {
    Function<Connection, Maybe<InvoiceNumber>> mock1 = mock(Function.class);
    when(this.table.getInvoiceNumber()).thenReturn(mock1);
    when(mock1.apply(any(Connection.class))).thenReturn(Maybe.just(new InvoiceNumber(14, this.yearNow)));

    Exception ex = new IllegalArgumentException("foobar");
    Function<Connection, Single<InvoiceNumber>> mock2 = mock(Function.class);
    when(this.table.setInvoiceNumber(eq(new InvoiceNumber(15, this.yearNow)))).thenReturn(mock2);
    when(mock2.apply(any(Connection.class))).thenReturn(Single.error(ex));

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
        .test()
        .assertNoValues()
        .assertError(ex)
        .assertNotComplete();

    verify(this.table).getInvoiceNumber();
    verify(mock1).apply(any(Connection.class));
    verify(this.table).setInvoiceNumber(eq(new InvoiceNumber(15, this.yearNow)));
    verify(mock2).apply(any(Connection.class));
    verifyNoMoreInteractions(this.table);
  }

  @Test
  public void testGenerateFirstInvoiceNumber() throws Exception {
    Function<Connection, Maybe<InvoiceNumber>> mock1 = mock(Function.class);
    when(this.table.getInvoiceNumber()).thenReturn(mock1);
    when(mock1.apply(any(Connection.class))).thenReturn(Maybe.empty());

    Function<Connection, Single<InvoiceNumber>> mock2 = mock(Function.class);
    when(this.table.initInvoiceNumber()).thenReturn(mock2);
    when(mock2.apply(any(Connection.class))).thenReturn(Single.just(new InvoiceNumber(15, 2013)));

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
        .test()
        .assertValue("152013")
        .assertNoErrors()
        .assertComplete();

    verify(this.table).getInvoiceNumber();
    verify(mock1).apply(any(Connection.class));
    verify(this.table).initInvoiceNumber();
    verify(mock2).apply(any(Connection.class));
    verifyNoMoreInteractions(this.table);
  }

  @Test
  public void testGenerateFirstInvoiceNumberErrorInInitInvoiceNumber() throws Exception {
    Function<Connection, Maybe<InvoiceNumber>> mock1 = mock(Function.class);
    when(this.table.getInvoiceNumber()).thenReturn(mock1);
    when(mock1.apply(any(Connection.class))).thenReturn(Maybe.empty());

    Exception ex = new IllegalArgumentException("foobar");
    Function<Connection, Single<InvoiceNumber>> mock2 = mock(Function.class);
    when(this.table.initInvoiceNumber()).thenReturn(mock2);
    when(mock2.apply(any(Connection.class))).thenReturn(Single.error(ex));

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
        .test()
        .assertNoValues()
        .assertError(ex)
        .assertNotComplete();

    verify(this.table).getInvoiceNumber();
    verify(mock1).apply(any(Connection.class));
    verify(this.table).initInvoiceNumber();
    verify(mock2).apply(any(Connection.class));
    verifyNoMoreInteractions(this.table);
  }

  @Test
  public void testGenerateFirstInvoiceNumberErrorInGetInvoiceNumber() throws Exception {
    Exception ex = new IllegalArgumentException("foobar");
    Function<Connection, Maybe<InvoiceNumber>> mock = mock(Function.class);
    when(this.table.getInvoiceNumber()).thenReturn(mock);
    when(mock.apply(any(Connection.class))).thenReturn(Maybe.error(ex));

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
        .test()
        .assertNoValues()
        .assertError(ex)
        .assertNotComplete();

    verify(this.table).getInvoiceNumber();
    verify(mock).apply(any(Connection.class));
    verifyNoMoreInteractions(this.table);
  }
}
