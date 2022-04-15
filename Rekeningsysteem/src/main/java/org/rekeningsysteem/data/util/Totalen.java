package org.rekeningsysteem.data.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public final class Totalen {

	public record NettoBtwTuple(Geld netto, Geld btw) {

		public NettoBtwTuple add(NettoBtwTuple other) {
			return new NettoBtwTuple(this.netto.add(other.netto), this.btw.add(other.btw));
		}

		public NettoBtwTuple wipeBtw() {
			return new NettoBtwTuple(this.netto, new Geld(0));
		}

		public Geld getTotaal() {
			return this.netto.add(this.btw);
		}
	}

	private final Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage;

	public Totalen() {
		this(new HashMap<>());
	}

	public Totalen(BtwPercentage percentage, Geld netto, Geld btw) {
		this();
		this.nettoBtwPerPercentage.put(percentage, new NettoBtwTuple(netto, btw));
	}

	private Totalen(Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage) {
		this.nettoBtwPerPercentage = nettoBtwPerPercentage;
	}

	public Totalen add(Geld netto) {
		return this.add(new BtwPercentage(0.0, false), netto, new Geld(0));
	}

	public Totalen add(BtwPercentage percentage, Geld netto, Geld btw) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		map.merge(percentage, new NettoBtwTuple(netto, btw), NettoBtwTuple::add);

		return new Totalen(map);
	}

	private static <K, V1, V2> Map<K, V2> sep(Map<K, V1> map, Function<V1, V2> f) {
		Map<K, V2> res = new HashMap<>();
		map.forEach((key, value) -> res.put(key, f.apply(value)));

		return Collections.unmodifiableMap(res);
	}

	public Set<BtwPercentage> getBtwPercentages() {
		return Collections.unmodifiableSet(this.nettoBtwPerPercentage.keySet());
	}

	public Map<BtwPercentage, NettoBtwTuple> getNettoBtwTuple() {
		return Collections.unmodifiableMap(this.nettoBtwPerPercentage);
	}

	public Map<BtwPercentage, Geld> getNetto() {
		return sep(this.nettoBtwPerPercentage, NettoBtwTuple::netto);
	}

	public Map<BtwPercentage, Geld> getBtw() {
		return sep(this.nettoBtwPerPercentage, NettoBtwTuple::btw);
	}

	public Geld getSubtotaal() {
		return this.nettoBtwPerPercentage.values()
			.parallelStream()
			.map(NettoBtwTuple::netto)
			.reduce(new Geld(0.0), Geld::add);
	}

	public Geld getTotaal() {
		return this.nettoBtwPerPercentage.entrySet()
			.parallelStream()
			.map(entry -> entry.getKey().verlegd() ? entry.getValue().wipeBtw() : entry.getValue())
			.map(NettoBtwTuple::getTotaal)
			.reduce(new Geld(0.0), Geld::add);
	}

	public Totalen plus(Totalen t2) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		t2.nettoBtwPerPercentage.forEach((percentage, bedrag) -> map.merge(percentage, bedrag, NettoBtwTuple::add));

		return new Totalen(map);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Totalen that) {
			return Objects.equals(this.nettoBtwPerPercentage, that.nettoBtwPerPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.nettoBtwPerPercentage);
	}

	@Override
	public String toString() {
		return "<Totalen[" + String.valueOf(this.nettoBtwPerPercentage) + "]>";
	}
}
