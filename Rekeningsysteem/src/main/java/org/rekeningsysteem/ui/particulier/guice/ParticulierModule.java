package org.rekeningsysteem.ui.particulier.guice;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.guice.ButtonImage;
import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.ui.header.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;
import org.rekeningsysteem.ui.WorkingPane;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class ParticulierModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurnummerType.class).toInstance(FactuurnummerType.FACTUUR);
		this.bind(String.class).annotatedWith(TabName.class).toInstance("Particulier factuur");
	}

	@Provides
	public WorkingPane[] provideWorkingPanes(OmschrFactuurHeaderController header) {
		return new WorkingPane[] { header.getUI() };
	}

	@Provides
	@ButtonImage
	public ImageView provideButtonImage() {
		return new ImageView(new Image(Main.getResource("/images/particulier.png")));
	}
}
