package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;

public interface RekeningVisitor {

	void visit(MutatiesFactuur factuur) throws Exception;

	void visit(Offerte offerte) throws Exception;

	void visit(ParticulierFactuur2 factuur) throws Exception;

	void visit(ReparatiesFactuur factuur) throws Exception;
}
