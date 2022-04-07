package org.rekeningsysteem.io.xml;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

class XmlWriterRekeningTypeVisitor implements RekeningVisitor<String> {

	@Override
	public String visit(MutatiesFactuur factuur) throws Exception {
		return "MutatiesFactuur";
	}

	@Override
	public String visit(Offerte offerte) throws Exception {
		return "Offerte";
	}

	@Override
	public String visit(ParticulierFactuur factuur) throws Exception {
		return "ParticulierFactuur";
	}

	@Override
	public String visit(ReparatiesFactuur factuur) throws Exception {
		return "ReparatiesFactuur";
	}
}
