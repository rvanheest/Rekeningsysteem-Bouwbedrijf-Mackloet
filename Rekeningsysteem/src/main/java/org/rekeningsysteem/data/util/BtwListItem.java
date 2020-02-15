package org.rekeningsysteem.data.util;

public interface BtwListItem extends ListItem {

	BtwPercentage getLoonBtwPercentage();

	BtwPercentage getMateriaalBtwPercentage();

	default Geld getLoonBtw() {
		return this.getLoon().multiply(this.getLoonBtwPercentage().getPercentage()).divide(100);
	}

	default Geld getMateriaalBtw() {
		return this.getMateriaalBtwPercentage().isVerlegd()
			? new Geld(0)
			: this.getMateriaal().multiply(this.getMateriaalBtwPercentage().getPercentage()).divide(100);
	}
}
