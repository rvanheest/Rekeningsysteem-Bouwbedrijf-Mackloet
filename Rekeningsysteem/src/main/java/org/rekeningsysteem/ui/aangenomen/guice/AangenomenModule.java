package org.rekeningsysteem.ui.aangenomen.guice;

import java.util.Currency;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.guice.ButtonImage;
import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.aangenomen.AangenomenListItemPane;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.list.ListPaneController;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class AangenomenModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurnummerType.class).toInstance(FactuurnummerType.FACTUUR);
		this.bind(String.class).annotatedWith(TabName.class).toInstance("Aangenomen factuur");
		this.bind(ItemPane.class).to(AangenomenListItemPane.class);
		
		this.install(new FactoryModuleBuilder().build(AangenomenListItemControllerFactory.class));
	}

	@Provides
	public WorkingPane[] provideWorkingPanes(OmschrFactuurHeaderController header,
			ListPaneController list) {
		return new WorkingPane[] { header.getUI(), list.getUI() };
	}

	@Provides
	@ButtonImage
	public ImageView provideButtonImage() {
		return new ImageView(new Image(Main.getResource("/images/aangenomen.png")));
	}

	@Provides
	public Currency provideCurrency(PropertiesWorker worker) {
		String currencyCode = worker.getProperty(PropertyModelEnum.VALUTAISO4217).get();
		return Currency.getInstance(currencyCode);
	}
}
