package org.rekeningsysteem.data.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Totalen {

	private final Geld loon;
	private final Geld materiaal;
	private final Map<Double, Geld> btwPerPercentage;

	public Totalen() {
		this(new Geld(0), new Geld(0), new HashMap<>());
	}

	private Totalen(Geld loon, Geld materiaal, Map<Double, Geld> btwPerPercentage) {
		this.loon = loon;
		this.materiaal = materiaal;
		this.btwPerPercentage = btwPerPercentage;
	}

	public Totalen addBtw(double percentage, Geld btw) {
		if (!btw.isZero()) {
			Map<Double, Geld> newMap = new HashMap<>(this.btwPerPercentage);
			newMap.merge(percentage, btw, Geld::add);
			return new Totalen(this.loon, this.materiaal, newMap);
		}
		return this;
	}

	public Map<Double, Geld> getBtw() {
		return Collections.unmodifiableMap(this.btwPerPercentage);
	}

	public Totalen addLoon(Geld loon) {
		return new Totalen(this.loon.add(loon), this.materiaal, this.btwPerPercentage);
	}

	public Geld getLoon() {
		return new Geld(this.loon);
	}

	public Totalen addMateriaal(Geld materiaal) {
		return new Totalen(this.loon, this.materiaal.add(materiaal), this.btwPerPercentage);
	}

	public Geld getMateriaal() {
		return new Geld(this.materiaal);
	}

	public Geld getSubtotaal() {
		return this.materiaal.add(this.loon);
	}

	public Geld getTotaal() {
		return this.getSubtotaal().add(this.btwPerPercentage.entrySet()
				.stream()
				.map(Map.Entry::getValue)
				.reduce(new Geld(0), Geld::add));
	}

	public Totalen plus(Totalen t2) {
		Geld loon = this.loon.add(t2.loon);
		Geld materiaal = this.materiaal.add(t2.materiaal);
		Map<Double, Geld> btw = new HashMap<>(this.btwPerPercentage);
		t2.btwPerPercentage
				.forEach((percentage, bedrag) -> btw.merge(percentage, bedrag, Geld::add));

		return new Totalen(loon, materiaal, btw);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Totalen) {
			Totalen that = (Totalen) other;
			return Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.materiaal, that.materiaal)
					&& Objects.equals(this.btwPerPercentage, that.btwPerPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.loon, this.materiaal, this.btwPerPercentage);
	}

	@Override
	public String toString() {
		return "<Totalen[" + String.valueOf(this.loon) + ", "
				+ String.valueOf(this.materiaal) + ", "
				+ String.valueOf(this.btwPerPercentage) + "]>";
	}
}
