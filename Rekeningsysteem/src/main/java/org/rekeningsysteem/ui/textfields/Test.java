package org.rekeningsysteem.ui.textfields;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.rekeningsysteem.rxjavafx.Observables;

public class Test extends Application {

//	public static void main(String[] args) throws ParseException {
//		DecimalFormat formatter = (DecimalFormat) NumberFormat.getIntegerInstance(Locale.getDefault());
//		formatter.setParseIntegerOnly(true);
//
//		System.out.println(formatter.parse("12,23"));
//		System.out.println(formatter.parse("-12,23"));
//		System.out.println(formatter.format(12.32));
//		System.out.println(formatter.format(-12.23));
//	}

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		IntegerField field = new IntegerField();
		Observables.fromProperty(field.valueProperty())
				.forEach(System.out::println);

		stage.setScene(new Scene(new StackPane(field), 300, 200));
		stage.setTitle("Rekeningsysteem Mackloet");
		stage.show();
	}
}
