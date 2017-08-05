package com.github.rvanheest.rekeningsysteem.model;

import java.util.Objects;

public final class TaxPercentages {

	private final double wagePercentage;
	private final double materialCostsPercentage;

	public TaxPercentages(double wagePercentage, double materialCostsPercentage) {
		this.wagePercentage = wagePercentage;
		this.materialCostsPercentage = materialCostsPercentage;
	}

	public double getWagePercentage() {
		return this.wagePercentage;
	}

	public double getMaterialCostsPercentage() {
		return this.materialCostsPercentage;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof TaxPercentages) {
          TaxPercentages that = (TaxPercentages) other;
			return Objects.equals(this.wagePercentage, that.wagePercentage)
					&& Objects.equals(this.materialCostsPercentage, that.materialCostsPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.wagePercentage, this.materialCostsPercentage);
	}

	@Override
	public String toString() {
		return "<TaxPercentages[" + String.valueOf(this.wagePercentage) + ", "
				+ String.valueOf(this.materialCostsPercentage) + "]>";
	}
}
