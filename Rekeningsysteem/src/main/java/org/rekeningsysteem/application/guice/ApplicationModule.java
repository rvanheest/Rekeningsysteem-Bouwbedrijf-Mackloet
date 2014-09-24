package org.rekeningsysteem.application.guice;

import java.util.List;

import org.rekeningsysteem.application.working.AbstractWorkModule;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(Double.class).annotatedWith(SceneMinHeight.class).toInstance(728.0);
		this.bind(Double.class).annotatedWith(SceneMinWidth.class).toInstance(1061.0);
		this.bind(new TypeLiteral<List<AbstractWorkModule>>() {})
				.toProvider(new WorkModuleListProvider());
	}

}
