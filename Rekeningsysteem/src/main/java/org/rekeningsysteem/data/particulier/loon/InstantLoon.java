package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public record InstantLoon(String omschrijving, Geld loon, BtwPercentage loonBtwPercentage) implements Loon { }
