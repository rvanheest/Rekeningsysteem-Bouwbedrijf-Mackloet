package com.github.rvanheest.rekeningsysteem.esselinkItems;

import com.github.rvanheest.rekeningsysteem.database.Database;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import io.reactivex.Observable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.Charsets;
import org.javamoney.moneta.Money;

import javax.money.format.MonetaryFormats;
import java.nio.file.Path;
import java.util.Locale;

public class EsselinkItemHandler {

  private final Database database;

  public EsselinkItemHandler(Database database) {
    this.database = database;
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

  public Observable<Integer> load(Path csv) {
    return this.database.loadEsselinkItems(this.read(csv));
  }
}
