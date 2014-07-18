package org.rekeningsysteem.logic.factuurnummer.guice;

import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.PropertyFactuurnummerManager;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PropertyFactuurnummerManagerModule extends AbstractModule {

	@Override
	protected void configure() {
		this.install(new FactoryModuleBuilder()
				.implement(FactuurnummerManager.class, PropertyFactuurnummerManager.class)
				.build(FactuurnummerManagerFactory.class));
		this.bind(PropertyKey.class)
				.annotatedWith(FactuurnummerKey.class)
				.toInstance(PropertyModelEnum.FACTUURNUMMER);
		this.bind(PropertyKey.class)
				.annotatedWith(OffertenummerKey.class)
				.toInstance(PropertyModelEnum.OFFERTENUMMER);
	}
}
