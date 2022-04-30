package org.rekeningsysteem.application.settings.debiteur;

import java.util.Optional;

import io.reactivex.rxjava3.core.Observable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.list.ItemPane;
import org.rekeningsysteem.ui.textfields.PostcodeTextField;
import org.rekeningsysteem.util.OptionalUtils;

public class DebiteurItemPane extends ItemPane {

	private final GridPane grid = new GridPane();

	private final TextField naamTF = new TextField();
	private final TextField straatTF = new TextField();
	private final TextField nummerTF = new TextField();
	private final TextField postcodeTF = new PostcodeTextField();
	private final TextField plaatsTF = new TextField();
	private final TextField btwTF = new TextField();

	private final Observable<String> naam = Observables.fromProperty(this.naamTF.textProperty());
	private final Observable<String> straat = Observables.fromProperty(this.straatTF.textProperty());
	private final Observable<String> nummer = Observables.fromProperty(this.nummerTF.textProperty());
	private final Observable<String> postcode = Observables.fromProperty(this.postcodeTF.textProperty());
	private final Observable<String> plaats = Observables.fromProperty(this.plaatsTF.textProperty());
	private final Observable<String> btw = Observables.fromProperty(this.btwTF.textProperty());

	public DebiteurItemPane() {
		super("Nieuwe debiteur");

		this.grid.setHgap(10);
		this.grid.setVgap(1);
		this.grid.setAlignment(Pos.TOP_CENTER);

		this.initLabels();
		this.initTextFields();

		this.getChildren().add(1, this.grid);

		// this causes every TF to be 20 columns since we're in a GridPane
		this.naamTF.setPrefColumnCount(20);
		this.setPromptTexts();
	}

	private void initLabels() {
		this.grid.add(new Label("Naam"), 0, 0);
		this.grid.add(new Label("Straat"), 0, 1);
		this.grid.add(new Label("Nummer"), 0, 2);
		this.grid.add(new Label("Postcode"), 0, 3);
		this.grid.add(new Label("Plaats"), 0, 4);
		this.grid.add(new Label("BTW nr."), 0, 5);
	}

	private void initTextFields() {
		this.grid.add(this.naamTF, 1, 0);
		this.grid.add(this.straatTF, 1, 1);
		this.grid.add(this.nummerTF, 1, 2);
		this.grid.add(this.postcodeTF, 1, 3);
		this.grid.add(this.plaatsTF, 1, 4);
		this.grid.add(this.btwTF, 1, 5);
	}

	private void setPromptTexts() {
		this.naamTF.setPromptText("naam");
		this.straatTF.setPromptText("straat");
		this.nummerTF.setPromptText("nummer");
		this.postcodeTF.setPromptText("postcode");
		this.plaatsTF.setPromptText("plaats");
		this.btwTF.setPromptText("btw nummer");
	}

	public void setAsUpdate() {
		this.title.setText("Debiteur aanpassen");
		this.addBtn.setText("Aanpassen");
	}

	public Observable<String> getNaam() {
		return this.naam;
	}

	public void setNaam(String naam) {
		this.naamTF.setText(naam);
	}

	public Observable<String> getStraat() {
		return this.straat;
	}

	public void setStraat(String straat) {
		this.straatTF.setText(straat);
	}

	public Observable<String> getNummer() {
		return this.nummer;
	}

	public void setNummer(String nummer) {
		this.nummerTF.setText(nummer);
	}

	public Observable<String> getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcodeTF.setText(postcode);
	}

	public Observable<String> getPlaats() {
		return this.plaats;
	}

	public void setPlaats(String plaats) {
		this.plaatsTF.setText(plaats);
	}

	public Observable<Optional<String>> getBtwNummer() {
		return this.btw.map(OptionalUtils::fromString);
	}

	public void setBtwNummer(String btw) {
		this.btwTF.setText(btw);
	}
}
