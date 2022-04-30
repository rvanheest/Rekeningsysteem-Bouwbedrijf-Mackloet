package org.rekeningsysteem.data.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record Totalen(Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage) {

	public record NettoBtwTuple(Geld netto, Geld btw) {

		private NettoBtwTuple add(NettoBtwTuple other) {
			return new NettoBtwTuple(this.netto.add(other.netto), this.btw.add(other.btw));
		}

		private Geld getTotaal(boolean verlegd) {
			return verlegd ? this.netto : this.netto.add(this.btw);
		}
	}

	public static Totalen Empty(){
		return new Totalen(new HashMap<>());
	}

	public Totalen(BtwPercentage percentage, Geld netto, Geld btw) {
		this(new HashMap<>());
		this.nettoBtwPerPercentage.put(percentage, new NettoBtwTuple(netto, btw));
	}

	public Totalen add(Geld netto) {
		return this.add(new BtwPercentage(0.0, false), netto, new Geld(0));
	}

	public Totalen add(BtwPercentage percentage, Geld netto, Geld btw) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		map.merge(percentage, new NettoBtwTuple(netto, btw), NettoBtwTuple::add);

		return new Totalen(map);
	}

	public Map<BtwPercentage, NettoBtwTuple> nettoBtwPerPercentage() {
		return Collections.unmodifiableMap(this.nettoBtwPerPercentage);
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
			.map(entry -> entry.getValue().getTotaal(entry.getKey().verlegd()))
			.reduce(new Geld(0.0), Geld::add);
	}

	public Totalen plus(Totalen t2) {
		Map<BtwPercentage, NettoBtwTuple> map = new HashMap<>(this.nettoBtwPerPercentage);
		t2.nettoBtwPerPercentage.forEach((percentage, bedrag) -> map.merge(percentage, bedrag, NettoBtwTuple::add));

		return new Totalen(map);
	}
}
