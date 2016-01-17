package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.Currency;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.ItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

@XmlRootElement(name = "particulier-factuur")
@XmlType(propOrder = { "factuurHeader", "currency", "list" })
public class ParticulierFactuurAdaptee extends RekeningAdapteeVisitable {

	private OmschrFactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<ParticulierArtikel> list = new ItemList<>();

	private ParticulierFactuurAdaptee() {
	}

	@XmlJavaTypeAdapter(OmschrFactuurHeaderAdapter.class)
	public OmschrFactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public ParticulierFactuurAdaptee setFactuurHeader(OmschrFactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
		return this;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public ParticulierFactuurAdaptee setCurrency(Currency currency) {
		this.currency = currency;
		return this;
	}

	@XmlJavaTypeAdapter(ItemListAdapter.class)
	public ItemList<ParticulierArtikel> getList() {
		return this.list;
	}

	public ParticulierFactuurAdaptee setList(ItemList<ParticulierArtikel> list) {
		this.list = list;
		return this;
	}

	@Override
	public <T> T accept(RekeningAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static ParticulierFactuurAdaptee build(
			Function<ParticulierFactuurAdaptee, ParticulierFactuurAdaptee> builder) {
		return builder.apply(new ParticulierFactuurAdaptee());
	}
}
