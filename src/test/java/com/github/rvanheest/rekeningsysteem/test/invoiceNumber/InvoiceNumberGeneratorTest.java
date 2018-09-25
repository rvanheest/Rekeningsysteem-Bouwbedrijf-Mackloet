package com.github.rvanheest.rekeningsysteem.test.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.database.DatabaseConnection;
import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.test.DatabaseFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceNumberGeneratorTest implements DatabaseFixture {

  private DatabaseConnection databaseAccess;
  private InvoiceNumberTable table = new InvoiceNumberTable();
  private InvoiceNumberGenerator generator;
  private final int yearNow = LocalDate.now().getYear();

  @Before
  public void setUp() throws Exception {
    this.databaseAccess = this.initDatabaseConnection();
    this.generator = new InvoiceNumberGenerator(new Database(this.databaseAccess));
  }

  @After
  public void tearDown() throws Exception {
    this.databaseAccess.closeConnectionPool();
  }

  @Test
  public void testGenerateFirstInvoiceNumber() {
    // make sure table is empty
    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    this.generator.calculateAndPersist()
        .test()
        .assertValue("1" + this.yearNow)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertValue(new InvoiceNumber(1, this.yearNow))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGenerateNextInvoiceNumber() {
    this.testGenerateFirstInvoiceNumber();

    this.generator.calculateAndPersist()
        .test()
        .assertValue("2" + this.yearNow)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertValue(new InvoiceNumber(2, this.yearNow))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGenerateNextWithFormerYear() {
    this.databaseAccess.doTransactionSingle(connection ->
        this.table.setInvoiceNumber(new InvoiceNumber(15, 2013)).apply(connection))
        .flatMap(in -> this.generator.calculateAndPersist())
        .test()
        .assertValue("1" + this.yearNow)
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertValue(new InvoiceNumber(1, this.yearNow))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testGenerateMultipleValuesInSequence() {
    List<String> expected = Stream.of(1, 2, 3).map(i -> i.toString() + this.yearNow).collect(Collectors.toList());

    this.generator.calculateAndPersist()
        .flatMap(s1 -> this.generator.calculateAndPersist()
            .flatMap(s2 -> this.generator.calculateAndPersist()
                .map(s3 -> Arrays.asList(s1, s2, s3))))
        .test()
        .assertValue(expected)
        .assertNoErrors()
        .assertComplete();
  }
}
