package org.rekeningsysteem.data.util;

import java.util.Objects;

public final class BtwPercentages {

	private final BtwPercentage loonPercentage;
	private final BtwPercentage materiaalPercentage;

	public BtwPercentages(BtwPercentage loonPercentage, BtwPercentage materiaalPercentage) {
		this.loonPercentage = loonPercentage;
		this.materiaalPercentage = materiaalPercentage;
	}

	public BtwPercentages(double loonPercentage, double materiaalPercentage) {
		this(new BtwPercentage(loonPercentage, false), new BtwPercentage(materiaalPercentage, false));
	}

	public BtwPercentage getLoonPercentage() {
		return this.loonPercentage;
	}

	public BtwPercentage getMateriaalPercentage() {
		return this.materiaalPercentage;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof BtwPercentages) {
			BtwPercentages that = (BtwPercentages) other;
			return Objects.equals(this.loonPercentage, that.loonPercentage)
					&& Objects.equals(this.materiaalPercentage, that.materiaalPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.loonPercentage, this.materiaalPercentage);
	}

	@Override
	public String toString() {
		return "<BtwPercentages[" + String.valueOf(this.loonPercentage) + ", "
				+ String.valueOf(this.materiaalPercentage) + "]>";
	}
}
