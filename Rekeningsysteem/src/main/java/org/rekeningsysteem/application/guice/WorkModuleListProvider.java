package org.rekeningsysteem.application.guice;

import java.util.Arrays;
import java.util.List;

import org.rekeningsysteem.application.working.AbstractWorkModule;
import org.rekeningsysteem.ui.aangenomen.AangenomenWorkPane;

import com.google.inject.Provider;

public class WorkModuleListProvider implements Provider<List<AbstractWorkModule>> {

	@Override
	public List<AbstractWorkModule> get() {
		return Arrays.asList(new AangenomenWorkPane());
	}
}
