package org.rekeningsysteem.data.util;

import java.util.Objects;

public final class Totalen {

	private final Geld loon;
	private final Geld loonBtw;
	private final Geld materiaal;
	private final Geld materiaalBtw;
	private final Geld subTotaal;
	private final Geld totaal;
	
	public Totalen() {
		this.loon = new Geld(0);
		this.loonBtw = new Geld(0);
		this.materiaal = new Geld(0);
		this.materiaalBtw = new Geld(0);
		this.subTotaal = new Geld(0);
		this.totaal = new Geld(0);
	}
	
	private Totalen(Geld loon, Geld loonBtw, Geld materiaal, Geld materiaalBtw) {
		this.loon = loon;
		this.loonBtw = loonBtw;
		this.materiaal = materiaal;
		this.materiaalBtw = materiaalBtw;
		this.subTotaal = new Geld(this.materiaal.add(this.loon));
		this.totaal = new Geld(this.subTotaal.add(this.materiaalBtw).add(this.loonBtw));
	}

	public Geld getLoon() {
		return new Geld(this.loon);
	}

	public Totalen withLoon(Geld loon) {
		return new Totalen(loon, this.loonBtw, this.materiaal, this.materiaalBtw);
	}

	public Geld getLoonBtw() {
		return new Geld(this.loonBtw);
	}

	public Totalen withLoonBtw(Geld loonBtw) {
		return new Totalen(this.loon, loonBtw, this.materiaal, this.materiaalBtw);
	}

	public Geld getMateriaal() {
		return new Geld(this.materiaal);
	}

	public Totalen withMateriaal(Geld materiaal) {
		return new Totalen(this.loon, this.loonBtw, materiaal, this.materiaalBtw);
	}

	public Geld getMateriaalBtw() {
		return new Geld(this.materiaalBtw);
	}

	public Totalen withMateriaalBtw(Geld materiaalBtw) {
		return new Totalen(this.loon, this.loonBtw, this.materiaal, materiaalBtw);
	}
	
	public Geld getSubtotaal() {
		return this.subTotaal;
	}
	
	public Geld getTotaal() {
		return this.totaal;
	}
	
	public Totalen plus(Totalen t2) {
		return new Totalen().withLoon(this.loon.add(t2.loon))
				.withLoonBtw(this.loonBtw.add(t2.loonBtw))
				.withMateriaal(this.materiaal.add(t2.materiaal))
				.withMateriaalBtw(this.materiaalBtw.add(t2.materiaalBtw));
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Totalen) {
			Totalen that = (Totalen) other;
			return Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.loonBtw, that.loonBtw)
					&& Objects.equals(this.materiaal, that.materiaal)
					&& Objects.equals(this.materiaalBtw, that.materiaalBtw);
			//subTotaal and totaal don't need to be compared: if those four are equal, then these
			//are equal as well.
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.loon, this.loonBtw, this.materiaal, this.materiaalBtw);
	}

	@Override
	public String toString() {
		return "<Totalen[" + String.valueOf(this.loon) + ", "
				+ String.valueOf(this.loonBtw) + ", "
				+ String.valueOf(this.materiaal) + ", "
				+ String.valueOf(this.materiaalBtw) + ", "
				+ String.valueOf(this.subTotaal) + ", "
				+ String.valueOf(this.totaal) + "]>";
	}
}
