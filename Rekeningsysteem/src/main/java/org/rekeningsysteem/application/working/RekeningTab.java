package org.rekeningsysteem.application.working;

import java.io.File;
import java.util.Optional;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.FactuurExporter;
import org.rekeningsysteem.io.pdf.guice.PdfExporterModule;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.guice.XmlMakerModule;
import org.rekeningsysteem.io.xml.guice.XmlReaderModule;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.guice.ConfigPropertiesModule;
import org.rekeningsysteem.ui.AbstractRekeningController;
import org.rekeningsysteem.ui.aangenomen.AangenomenController;

import rx.Observable;
import rx.subjects.PublishSubject;

import com.google.inject.Guice;

public class RekeningTab extends Tab {

	private final AbstractRekeningController controller;
	private final PublishSubject<Boolean> modified = PublishSubject.create();
	private Optional<File> saveFile;

	@Deprecated
	public RekeningTab(String name) {
		super(name);
		this.controller = null;
		this.setContent(new SplitPane());
	}

	public RekeningTab(String name, AbstractRekeningController controller) {
		this(name, controller, null);
	}

	public RekeningTab(String name, AbstractRekeningController controller, File file) {
		super(name);
		this.controller = controller;
		this.saveFile = Optional.ofNullable(file);

		this.setContent(this.controller.getUI());

		this.controller.getModel()
				.buffer(2, 1) // fired at first on the 2nd onNext!
				.map(list -> list.get(0).equals(list.get(1))) // will of course return false
				.scan((saved, equals) -> saved && equals)
				.map(b -> !b)
				.subscribe(this.modified::onNext);

		this.modified.filter(b -> b == true)
				.map(b -> this.getText())
				.filter(s -> !s.endsWith("*"))
				.map(s -> s + "*")
				.subscribe(this::setText);
	}

	public Observable<? extends AbstractRekening> getModel() {
		return this.controller.getModel();
	}

	public Optional<File> getSaveFile() {
		return this.saveFile;
	}

	public void setSaveFile(File file) {
		this.saveFile = Optional.ofNullable(file);
		this.saveFile.map(File::getName).ifPresent(this::setText);
	}

	public void initFactuurnummer() {
		this.controller.initFactuurnummer();
	}

	public static RekeningTab openFile(File file) {
		// TODO remove GUICE
		XmlReader reader = Guice.createInjector(new XmlReaderModule(), new ConsoleLoggerModule())
				.getInstance(XmlReader.class);
		Observable<AbstractRekening> factuur = reader.load(file);
		factuur.subscribe(System.out::println);
		
		// TODO make this more generic for other types of AbstractRekening
		return new RekeningTab(file.getName(), new AangenomenController(
				factuur.cast(AangenomenFactuur.class)), file);
	}

	public void save() {
		// TODO remove GUICE
		XmlMaker maker = Guice.createInjector(new XmlMakerModule(), new ConsoleLoggerModule())
				.getInstance(XmlMaker.class);
		this.getModel()
				.doOnNext(factuur -> this.saveFile.ifPresent(file -> maker.save(factuur, file)))
				.map(factuur -> this.getText())
				.filter(s -> s.endsWith("*"))
				.map(s -> s.substring(0, s.length() - 1))
				.subscribe(this::setText)
				.unsubscribe();
		this.modified.onNext(false);
	}

	public void export(File file) {
		if (file != null) {
			//TODO remove GUICE
			FactuurExporter pdf = Guice.createInjector(new PdfExporterModule(),
					new ConfigPropertiesModule(), new ConsoleLoggerModule())
					.getInstance(FactuurExporter.class);
			this.getModel().doOnNext(factuur -> pdf.export(factuur, file))
					.subscribe(factuur -> {
					},
							e -> e.printStackTrace())
					.unsubscribe();
		}
		//TODO what if file == null???
	}
}
