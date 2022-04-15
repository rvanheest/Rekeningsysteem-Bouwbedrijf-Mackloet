package org.rekeningsysteem.data.util;

public interface BtwListItem extends ListItem {

	BtwPercentage getLoonBtwPercentage();

	BtwPercentage getMateriaalBtwPercentage();

	default Geld getLoonBtw() {
		return this.loon().multiply(this.getLoonBtwPercentage().percentage()).divide(100);
	}

	default Geld getMateriaalBtw() {
		return this.materiaal().multiply(this.getMateriaalBtwPercentage().percentage()).divide(100);
	}
}
