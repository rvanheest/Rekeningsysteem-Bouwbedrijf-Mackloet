package org.rekeningsysteem.data.util;

import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record Totalen(CurrencyUnit currency, Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage) {

	public record NettoBtwTuple(MonetaryAmount netto, MonetaryAmount btw) {

		private NettoBtwTuple add(NettoBtwTuple other) {
			return new NettoBtwTuple(this.netto.add(other.netto), this.btw.add(other.btw));
		}

		private MonetaryAmount getTotaal(boolean verlegd) {
			return verlegd ? this.netto : this.netto.add(this.btw);
		}
	}

	public Totalen(CurrencyUnit currency) {
		this(currency, new HashMap<>());
	}

	public Totalen(CurrencyUnit currency, BtwPercentage percentage, MonetaryAmount netto, MonetaryAmount btw) {
		this(currency);
		this.nettoBtwPerPercentage.put(percentage, new NettoBtwTuple(netto, btw));
	}

	public Totalen add(MonetaryAmount netto) {
		return this.add(new BtwPercentage(0.0, false), netto, Money.zero(this.currency));
	}

	public Totalen add(BtwPercentage percentage, MonetaryAmount netto, MonetaryAmount btw) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		map.merge(percentage, new NettoBtwTuple(netto, btw), NettoBtwTuple::add);

		return new Totalen(this.currency, map);
	}

	public Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage() {
		return Collections.unmodifiableMap(this.nettoBtwPerPercentage);
	}

	public MonetaryAmount getSubtotaal() {
		return this.nettoBtwPerPercentage.values()
			.parallelStream()
			.map(NettoBtwTuple::netto)
			.reduce(Money.zero(this.currency), MonetaryAmount::add);
	}

	public MonetaryAmount getTotaal() {
		return this.nettoBtwPerPercentage.entrySet()
			.parallelStream()
			.map(entry -> entry.getValue().getTotaal(entry.getKey().verlegd()))
			.reduce(Money.zero(this.currency), MonetaryAmount::add);
	}

	public Totalen plus(Totalen t2) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		t2.nettoBtwPerPercentage.forEach((percentage, bedrag) -> map.merge(percentage, bedrag, NettoBtwTuple::add));

		return new Totalen(this.currency, map);
	}
}
