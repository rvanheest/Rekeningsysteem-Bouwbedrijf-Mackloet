package org.rekeningsysteem.data.util;

public abstract class BtwListItem extends ListItem {

	public abstract double getLoonBtwPercentage();

	public abstract double getMateriaalBtwPercentage();

	public Geld getLoonBtw() {
		return this.getLoon().multiply(this.getLoonBtwPercentage()).divide(100);
	}

	public Geld getMateriaalBtw() {
		return this.getMateriaal().multiply(this.getMateriaalBtwPercentage()).divide(100);
	}

	@Override
	public Geld getTotaal() {
		return super.getTotaal()
				.add(this.getLoonBtw())
				.add(this.getMateriaalBtw());
	}
}
