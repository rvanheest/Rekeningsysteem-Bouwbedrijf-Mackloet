package org.rekeningsysteem.logic.bedragmanager;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ListItem;

public interface BedragManager<E extends ListItem> {

	void setBtwPercentage(BtwPercentage btwPercentage);

	boolean add(E item);

	boolean remove(E item);
	
	void clear();
	
	Totalen getTotalen();
}
