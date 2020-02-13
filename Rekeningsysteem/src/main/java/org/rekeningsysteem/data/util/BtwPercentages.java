package org.rekeningsysteem.data.util;

import java.util.Objects;

public final class BtwPercentages {

	private final double loonPercentage;
	private final double materiaalPercentage;

	public BtwPercentages(double loonPercentage, double materiaalPercentage) {
		this.loonPercentage = loonPercentage;
		this.materiaalPercentage = materiaalPercentage;
	}

	public double getLoonPercentage() {
		return this.loonPercentage;
	}

	public double getMateriaalPercentage() {
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
		return "<BtwPercentage[" + String.valueOf(this.loonPercentage) + ", "
				+ String.valueOf(this.materiaalPercentage) + "]>";
	}
}
