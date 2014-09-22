package org.rekeningsysteem.application.guice;

import com.google.inject.AbstractModule;

public class ApplicationModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(Double.class).annotatedWith(SceneMinHeight.class).toInstance(728.0);
		this.bind(Double.class).annotatedWith(SceneMinWidth.class).toInstance(1061.0);
	}

}
