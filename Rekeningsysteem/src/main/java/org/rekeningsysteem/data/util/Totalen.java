package org.rekeningsysteem.data.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Totalen {

	public class NettoBtwTuple {

		private final Geld netto;
		private final Geld btw;

		public NettoBtwTuple(Geld netto, Geld btw) {
			this.netto = netto;
			this.btw = btw;
		}

		public NettoBtwTuple add(NettoBtwTuple other) {
			return new NettoBtwTuple(this.netto.add(other.netto), this.btw.add(other.btw));
		}

		public Geld getNetto() {
			return this.netto;
		}

		public Geld getBtw() {
			return this.btw;
		}

		public Geld getTotaal() {
			return this.netto.add(this.btw);
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof NettoBtwTuple) {
				NettoBtwTuple that = (NettoBtwTuple) other;
				return Objects.equals(this.netto, that.netto)
						&& Objects.equals(this.btw, that.btw);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.netto, this.btw);
		}

		@Override
		public String toString() {
			return "<NettoBtwTuple[" + String.valueOf(this.netto) + ", "
					+ String.valueOf(this.btw) + "]>";
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
		map.entrySet()
				.stream()
				.forEach(entry -> res.put(entry.getKey(), f.apply(entry.getValue())));

		return Collections.unmodifiableMap(res);
	}

	public Set<BtwPercentage> getBtwPercentages() {
	    return Collections.unmodifiableSet(this.nettoBtwPerPercentage.keySet());
    }

	public Map<BtwPercentage, NettoBtwTuple> getNettoBtwTuple() {
		return Collections.unmodifiableMap(this.nettoBtwPerPercentage);
	}

	public Map<BtwPercentage, Geld> getNetto() {
		return Collections.unmodifiableMap(sep(this.nettoBtwPerPercentage, NettoBtwTuple::getNetto));
	}

	public Map<BtwPercentage, Geld> getBtw() {
		return Collections.unmodifiableMap(sep(this.nettoBtwPerPercentage, NettoBtwTuple::getBtw));
	}

	public Geld getSubtotaal() {
		return this.nettoBtwPerPercentage.values()
				.parallelStream()
				.map(NettoBtwTuple::getNetto)
				.reduce(new Geld(0.0), Geld::add);
	}

	public Geld getTotaal() {
		return this.nettoBtwPerPercentage
				.entrySet()
				.parallelStream()
				.filter(entry -> !entry.getKey().isVerlegd())
				.map(Map.Entry::getValue)
				.map(NettoBtwTuple::getTotaal)
				.reduce(new Geld(0.0), Geld::add);
	}

	public Totalen plus(Totalen t2) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		t2.nettoBtwPerPercentage
				.forEach((percentage, bedrag) -> map.merge(percentage, bedrag, NettoBtwTuple::add));

		return new Totalen(map);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Totalen) {
			Totalen that = (Totalen) other;
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
