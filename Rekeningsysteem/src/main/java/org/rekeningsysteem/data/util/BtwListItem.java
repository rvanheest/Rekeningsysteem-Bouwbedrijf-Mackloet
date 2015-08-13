package org.rekeningsysteem.data.util;

public interface BtwListItem extends ListItem {

	double getLoonBtwPercentage();

	double getMateriaalBtwPercentage();

	default Geld getLoonBtw() {
		return this.getLoon().multiply(this.getLoonBtwPercentage()).divide(100);
	}

	default Geld getMateriaalBtw() {
		return this.getMateriaal().multiply(this.getMateriaalBtwPercentage()).divide(100);
	}
}
