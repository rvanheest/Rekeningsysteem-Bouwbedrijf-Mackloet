package com.github.rvanheest.rekeningsysteem.esselinkItems;

import com.github.rvanheest.rekeningsysteem.database.EsselinkItemTable;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Observable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.Charsets;
import org.javamoney.moneta.Money;

import javax.money.format.MonetaryFormats;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Locale;
import java.util.function.Function;

public class EsselinkItemHandler {

  private final EsselinkItemTable table;

  public EsselinkItemHandler() {
    this(new EsselinkItemTable());
  }

  public EsselinkItemHandler(EsselinkItemTable table) {
    this.table = table;
  }

  public Observable<EsselinkItem> read(Path csv) {
    return Observable.using(
        () -> CSVParser.parse(csv.toFile(), Charsets.UTF_8, CSVFormat.EXCEL.withDelimiter(';')),
        parser -> Observable.fromIterable(parser.getRecords())
            .skip(1)
            .map(record -> new EsselinkItem(
                record.get(0),
                record.get(1),
                Integer.parseInt(record.get(2)),
                record.get(3),
                Money.parse("EUR " + record.get(4), MonetaryFormats.getAmountFormat(Locale.getDefault())))),
        CSVParser::close);
  }

  public Function<Connection, Observable<Integer>> load(Path csv) {
    return connection -> this.table.clearData().apply(connection)
        .andThen(this.read(csv))
        .window(100)
        .flatMapSingle(items -> this.table.insertAll(items).apply(connection))
        .scan(0, Math::addExact);
  }
}
