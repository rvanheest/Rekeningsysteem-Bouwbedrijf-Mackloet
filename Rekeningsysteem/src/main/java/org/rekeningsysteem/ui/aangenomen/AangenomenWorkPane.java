package org.rekeningsysteem.ui.aangenomen;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.working.AbstractWorkModule;

public class AangenomenWorkPane extends AbstractWorkModule {

	public AangenomenWorkPane() {
		super(new ImageView(new Image(Main.getResource("/images/aangenomen.png"))));
	}
}
