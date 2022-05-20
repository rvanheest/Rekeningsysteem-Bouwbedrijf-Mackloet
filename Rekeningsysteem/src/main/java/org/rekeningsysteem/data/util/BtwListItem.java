package org.rekeningsysteem.data.util;

import javax.money.MonetaryAmount;

public interface BtwListItem extends ListItem {

	BtwPercentage loonBtwPercentage();

	BtwPercentage materiaalBtwPercentage();

	default MonetaryAmount loonBtw() {
		return this.loon().multiply(this.loonBtwPercentage().percentage()).divide(100);
	}

	default MonetaryAmount materiaalBtw() {
		return this.materiaal().multiply(this.materiaalBtwPercentage().percentage()).divide(100);
	}
}
