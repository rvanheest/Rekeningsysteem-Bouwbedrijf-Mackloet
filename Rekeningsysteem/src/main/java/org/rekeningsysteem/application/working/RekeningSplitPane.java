package org.rekeningsysteem.application.working;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.WorkingPane;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;

public class RekeningSplitPane extends BorderPane {

	private final List<WorkingPane> panes;

	public RekeningSplitPane(WorkingPane... panes) {
		this.panes = Arrays.asList(panes);

		this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		BorderPane leftSplitPane = new BorderPane();
		leftSplitPane.setId("left-panel");
		leftSplitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		leftSplitPane.setCenter(this.makeTree());

		this.setLeft(leftSplitPane);
	}

	private TreeView<String> makeTree() {
		TreeItem<String> treeRoot = new TreeItem<String>();

		TreeView<String> tree = new TreeView<>(treeRoot);
		tree.setId("panel-tree");
		tree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		tree.setShowRoot(false);
		tree.setEditable(false);
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		this.panes.stream().map(pane -> new TreeItem<>(pane.getTitle()))
				.forEach(treeRoot.getChildren()::add);
		tree.getSelectionModel().select(0);

		Observables.fromProperty(tree.getSelectionModel().selectedIndexProperty())
				.filter(Objects::nonNull)
				.map(Number::intValue)
				.filter(i -> i >= 0)
				.map(this.panes::get)
				.forEach(this::setCenter);

		return tree;
	}
}
