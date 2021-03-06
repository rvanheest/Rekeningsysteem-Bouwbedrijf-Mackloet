package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;

public interface RekeningVoidVisitor {

	void visit(MutatiesFactuur factuur) throws Exception;

	void visit(Offerte offerte) throws Exception;

	void visit(ParticulierFactuur factuur) throws Exception;

	void visit(ReparatiesFactuur factuur) throws Exception;
}
