package org.rekeningsysteem.application.working;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;

public class RekeningToolbar extends ToolBar {
	
	public RekeningToolbar(Node... nodes) {
		super(nodes);
		
		this.setId("rekening-toolbar");
		this.setMinHeight(29);
		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
	}

	public RekeningToolbar(List<Node> nodes) {
		this(nodes.toArray(new Node[0]));
	}
}
