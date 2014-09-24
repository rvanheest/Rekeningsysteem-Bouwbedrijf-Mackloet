package org.rekeningsysteem.ui.aangenomen;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.working.AbstractWorkModule;
import org.rekeningsysteem.application.working.RekeningSplitPane;
import org.rekeningsysteem.application.working.RekeningTab;

public class AangenomenWorkPane extends AbstractWorkModule {

	public AangenomenWorkPane() {
		super(new ImageView(new Image(Main.getResource("/images/aangenomen.png"))),
				event -> new RekeningTab("Aangenomen factuur", new RekeningSplitPane()));
	}
}
