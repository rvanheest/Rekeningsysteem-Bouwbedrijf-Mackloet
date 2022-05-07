package org.rekeningsysteem.data.util;

public interface BtwListItem extends ListItem {

	BtwPercentage loonBtwPercentage();

	BtwPercentage materiaalBtwPercentage();

	default Geld loonBtw() {
		return this.loon().multiply(this.loonBtwPercentage().percentage()).divide(100);
	}

	default Geld materiaalBtw() {
		return this.materiaal().multiply(this.materiaalBtwPercentage().percentage()).divide(100);
	}
}
