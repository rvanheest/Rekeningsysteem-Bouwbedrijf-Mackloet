package com.github.rvanheest.rekeningsysteem.test.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.database.InvoiceNumberTable;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumberGenerator;
import com.github.rvanheest.rekeningsysteem.test.database.DatabaseFixture;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InvoiceNumberGeneratorTest extends DatabaseFixture {

  private final InvoiceNumberTable table = new InvoiceNumberTable();
  private final InvoiceNumberGenerator generator = new InvoiceNumberGenerator(table);
  private final int yearNow = LocalDate.now().getYear();

  @Before
  public void setUp() throws Exception {
    this.resetTestDir();
    super.setUp();
  }

  @Test
  public void testGenerateFirstInvoiceNumber() {
    // make sure table is empty
    this.databaseAccess.doTransactionMaybe(this.table.getInvoiceNumber())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
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

    this.databaseAccess.doTransactionSingle(this.generator.calculateAndPersist())
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
        this.table.setInvoiceNumber(new InvoiceNumber(15, 2013)).apply(connection)
            .flatMap(in -> this.generator.calculateAndPersist().apply(connection)))
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

    this.databaseAccess.doTransactionObservable(connection ->
        this.generator.calculateAndPersist().apply(connection)
            .flatMapObservable(s1 -> this.generator.calculateAndPersist().apply(connection)
                .flatMapObservable(s2 -> this.generator.calculateAndPersist().apply(connection)
                    .flatMapObservable(s3 -> Observable.just(s1, s2, s3))))
    )
        .test()
        .assertValueSequence(expected)
        .assertNoErrors()
        .assertComplete();
  }
}
