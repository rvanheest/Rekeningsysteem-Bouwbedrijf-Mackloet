package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;

public interface RekeningVisitor {

	@Deprecated
	void visit(AangenomenFactuur factuur) throws Exception; // TODO remove this

	void visit(MutatiesFactuur factuur) throws Exception;

	void visit(Offerte offerte) throws Exception;

	@Deprecated
	void visit(ParticulierFactuur factuur) throws Exception; // TODO remove old
	void visit(ParticulierFactuur2 factuur) throws Exception;

	void visit(ReparatiesFactuur factuur) throws Exception;
}
