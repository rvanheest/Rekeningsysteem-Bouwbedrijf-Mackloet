package com.github.rvanheest.rekeningsysteem.model.totals;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Totals {

  public class NetTaxTuple {

    private final MonetaryAmount net;
    private final MonetaryAmount tax;

    private NetTaxTuple(MonetaryAmount net, MonetaryAmount tax) {
      this.net = net;
      this.tax = tax;
    }

    public MonetaryAmount getNet() {
      return this.net;
    }

    public MonetaryAmount getTax() {
      return this.tax;
    }

    public NetTaxTuple add(NetTaxTuple other) {
      return new NetTaxTuple(this.net.add(other.net), this.tax.add(other.tax));
    }

    public MonetaryAmount getTotal() {
      return this.net.add(this.tax);
    }

    @Override
    public boolean equals(Object other) {
      if (other instanceof NetTaxTuple) {
        NetTaxTuple that = (NetTaxTuple) other;
        return Objects.equals(this.net, that.net)
            && Objects.equals(this.tax, that.tax);
      }
      return false;
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.net, this.tax);
    }

    @Override
    public String toString() {
      return "(" + String.valueOf(this.net) + ", " + String.valueOf(this.tax) + ")";
    }
  }

  private final CurrencyUnit currency;
  private final Map<Double, NetTaxTuple> netTaxPerPercentage;

  public Totals(CurrencyUnit currency) {
    this(currency, new HashMap<>());
  }

  public Totals(CurrencyUnit currency, double percentage, MonetaryAmount net, MonetaryAmount tax) {
    this(currency);
    this.netTaxPerPercentage.put(percentage, new NetTaxTuple(net, tax));
  }

  private Totals(CurrencyUnit currency, Map<Double, NetTaxTuple> netTaxPerPercentage) {
    this.currency = currency;
    this.netTaxPerPercentage = netTaxPerPercentage;
  }

  public Totals add(MonetaryAmount net) {
    return this.add(0.0, net, Money.of(0, this.currency));
  }

  public Totals add(double percentage, MonetaryAmount net, MonetaryAmount tax) {
    HashMap<Double, NetTaxTuple> map = new HashMap<>(this.netTaxPerPercentage);
    map.merge(percentage, new NetTaxTuple(net, tax), NetTaxTuple::add);

    return new Totals(this.currency, map);
  }

  private static <K, V1, V2> Map<K, V2> separate(Map<K, V1> map, Function<V1, V2> f) {
    Map<K, V2> res = new HashMap<>();
    map.forEach((key, value) -> res.put(key, f.apply(value)));

    return Collections.unmodifiableMap(res);
  }

  public Map<Double, NetTaxTuple> getNettoBtwTuple() {
    return Collections.unmodifiableMap(this.netTaxPerPercentage);
  }

  public Map<Double, MonetaryAmount> getNet() {
    return Collections.unmodifiableMap(separate(this.netTaxPerPercentage, NetTaxTuple::getNet));
  }

  public Map<Double, MonetaryAmount> getTax() {
    return Collections.unmodifiableMap(separate(this.netTaxPerPercentage, NetTaxTuple::getTax));
  }

  public MonetaryAmount getSubtotal() {
    return this.netTaxPerPercentage.values()
        .parallelStream()
        .map(NetTaxTuple::getNet)
        .reduce(Money.zero(this.currency), MonetaryAmount::add);
  }

  public MonetaryAmount getTotal() {
    return this.netTaxPerPercentage.values()
        .parallelStream()
        .map(NetTaxTuple::getTotal)
        .reduce(Money.zero(this.currency), MonetaryAmount::add);
  }

  public Totals add(Totals t2) {
    Map<Double, NetTaxTuple> map = new HashMap<>(this.netTaxPerPercentage);
    t2.netTaxPerPercentage
        .forEach((percentage, bedrag) -> map.merge(percentage, bedrag, NetTaxTuple::add));

    return new Totals(this.currency, map);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Totals) {
      Totals that = (Totals) other;
      return Objects.equals(this.currency, that.currency)
          && Objects.equals(this.netTaxPerPercentage, that.netTaxPerPercentage);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.currency, this.netTaxPerPercentage);
  }

  @Override
  public String toString() {
    return "<Totals[" + String.valueOf(this.currency) + ", " + String.valueOf(this.netTaxPerPercentage) + "]>";
  }
}
