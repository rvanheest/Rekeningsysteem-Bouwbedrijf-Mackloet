package org.rekeningsysteem.logic.factuurnummer.guice;

import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.FileFactuurnummerManager;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class FileFactuurnummerManagerModule extends AbstractModule {

	@Override
	protected void configure() {
		this.install(new FactoryModuleBuilder()
				.implement(FactuurnummerManager.class, FileFactuurnummerManager.class)
				.build(FactuurnummerManagerFactory.class));
		this.bind(PropertyKey.class)
				.annotatedWith(FactuurnummerKey.class)
				.toInstance(PropertyModelEnum.FACTUURNUMMERFILE);
		this.bind(PropertyKey.class)
				.annotatedWith(OffertenummerKey.class)
				.toInstance(PropertyModelEnum.OFFERTENUMMERFILE);
	}
}
