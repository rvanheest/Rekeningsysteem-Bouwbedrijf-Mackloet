package org.rekeningsysteem.ui.offerte.guice;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.guice.ButtonImage;
import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.ui.FactuurnummerPane.FactuurnummerType;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.offerte.OfferteHeaderController;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class OfferteModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurnummerType.class).toInstance(FactuurnummerType.OFFERTE);
		this.bind(String.class).annotatedWith(TabName.class).toInstance("Offerte");
	}

	@Provides
	public WorkingPane[] provideWorkingPanes(OfferteHeaderController header) {
		return new WorkingPane[] { header.getUI() };
	}

	@Provides
	@ButtonImage
	public ImageView provideButtonImage() {
		return new ImageView(new Image(Main.getResource("/images/offerte.png")));
	}
}
