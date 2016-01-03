package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;

public interface RekeningVisitor<T> {

	T visit(MutatiesFactuur factuur) throws Exception;

	T visit(Offerte offerte) throws Exception;

	T visit(ParticulierFactuur factuur) throws Exception;

	T visit(ReparatiesFactuur factuur) throws Exception;
}
