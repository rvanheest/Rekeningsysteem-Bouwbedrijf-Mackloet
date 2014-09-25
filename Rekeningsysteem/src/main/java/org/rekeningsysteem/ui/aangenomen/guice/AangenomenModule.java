package org.rekeningsysteem.ui.aangenomen.guice;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.guice.ButtonImage;
import org.rekeningsysteem.application.guice.TabName;
import org.rekeningsysteem.ui.FactuurnummerPane.FactuurnummerType;
import org.rekeningsysteem.ui.OmschrFactuurHeaderController;
import org.rekeningsysteem.ui.WorkingPane;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AangenomenModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(FactuurnummerType.class).toInstance(FactuurnummerType.FACTUUR);
		this.bind(String.class).annotatedWith(TabName.class).toInstance("Aangenomen factuur");
	}

	@Provides
	public WorkingPane[] provideWorkingPanes(OmschrFactuurHeaderController header) {
		return new WorkingPane[] { header.getUI() };
	}

	@Provides
	@ButtonImage
	public ImageView provideButtonImage() {
		return new ImageView(new Image(Main.getResource("/images/aangenomen.png")));
	}
}
