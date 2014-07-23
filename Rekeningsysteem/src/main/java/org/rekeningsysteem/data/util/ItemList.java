package org.rekeningsysteem.data.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.rekeningsysteem.logic.bedragmanager.BedragManager;
import org.rekeningsysteem.logic.bedragmanager.Totalen;

public class ItemList<E extends ListItem> implements BedragManager {

	private final List<E> list;
	private Totalen totalen;

	public ItemList() {
		this.list = new ArrayList<>();
		this.totalen = new Totalen();
	}

	public ItemList(Collection<? extends E> c) {
		this.list = new ArrayList<>();
		this.totalen = new Totalen();
		
		c.forEach(this::add);
	}

	@Override
	public Totalen getTotalen() {
		return this.totalen;
	}

	public int size() {
		return this.list.size();
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	public boolean add(E item) {
		boolean res = this.list.add(item);
		
		this.addLoonBedrag(item.getLoon());
		this.addMateriaalBedrag(item.getMateriaal());
		
		return res;
	}

	protected void addLoonBedrag(Geld geld) {
		Geld nieuwLoon = this.totalen.getLoon().add(geld);
		this.totalen = this.totalen.withLoon(nieuwLoon);
	}

	protected void addMateriaalBedrag(Geld geld) {
		Geld nieuwMateriaal = this.totalen.getMateriaal().add(geld);
		this.totalen = this.totalen.withMateriaal(nieuwMateriaal);
	}

	public boolean remove(E item) {
		boolean res = this.list.remove(item);
		
		this.subtractLoonBedrag(item.getLoon());
		this.subtractMateriaalBedrag(item.getMateriaal());
		
		return res;
	}

	protected void subtractLoonBedrag(Geld geld) {
		Geld nieuwLoon = this.totalen.getLoon().subtract(geld);
		this.totalen = this.totalen.withLoon(nieuwLoon);
	}

	protected void subtractMateriaalBedrag(Geld geld) {
		Geld nieuwMateriaal = this.totalen.getMateriaal().subtract(geld);
		this.totalen = this.totalen.withMateriaal(nieuwMateriaal);
	}
	
	public void clear() {
		this.list.clear();
		this.totalen = new Totalen();
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ItemList) {
			ItemList<?> that = (ItemList<?>) other;
			return Objects.equals(this.list, that.list)
					&& Objects.equals(this.totalen, that.totalen);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.list, this.totalen);
	}

	@Override
	public String toString() {
		return "<ItemList[" + this.list + ", " + this.totalen + "]>";
	}
}
