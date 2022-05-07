package org.rekeningsysteem.data.offerte;

import org.rekeningsysteem.data.util.Document;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public record Offerte(FactuurHeader header, String tekst, boolean ondertekenen) implements Document { }
