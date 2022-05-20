package org.rekeningsysteem.data.util;

import javax.money.MonetaryAmount;

public interface ListItem {

	MonetaryAmount loon();

	MonetaryAmount materiaal();
}
