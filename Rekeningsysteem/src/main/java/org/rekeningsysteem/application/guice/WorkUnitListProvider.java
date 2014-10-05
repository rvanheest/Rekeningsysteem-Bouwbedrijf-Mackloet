package org.rekeningsysteem.application.guice;

import java.util.Arrays;
import java.util.List;

import org.rekeningsysteem.application.working.WorkUnit;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;
import org.rekeningsysteem.ui.aangenomen.guice.AangenomenModule;
import org.rekeningsysteem.ui.mutaties.guice.MutatiesModule;
import org.rekeningsysteem.ui.offerte.guice.OfferteModule;
import org.rekeningsysteem.ui.particulier.guice.ParticulierModule;
import org.rekeningsysteem.ui.reparaties.guice.ReparatiesModule;

import com.google.inject.Provider;

public class WorkUnitListProvider implements Provider<List<WorkUnit>> {

	@Override
	public List<WorkUnit> get() {
		return Arrays.asList(new WorkUnit(new AangenomenModule(), new ConfigPropertiesModule(), new ConsoleLoggerModule()),
				new WorkUnit(new MutatiesModule()),
				new WorkUnit(new ReparatiesModule()),
				new WorkUnit(new ParticulierModule()),
				new WorkUnit(new OfferteModule()));
	}
}
