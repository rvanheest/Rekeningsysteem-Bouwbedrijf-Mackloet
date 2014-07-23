package org.rekeningsysteem.logic.factuurnummer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileFactuurnummerManager implements FactuurnummerManager {

	private final Optional<File> file;
	private Optional<String> factNr;
	private final Logger logger;

	@Inject
	public FileFactuurnummerManager(PropertiesWorker worker, @Assisted PropertyKey fileProp, Logger logger) {
		this.file = worker.getProperty(fileProp).map(File::new);
		this.factNr = Optional.empty();
		this.logger = logger;
	}
	
	private Optional<String> readFromFile() {
		return this.file.map(f -> {
			try {
				return FileUtils.readFileToString(f);
			}
			catch (IOException e) {
				this.logger.error(e.getMessage(), e);
				return null;
			}
		});
	}

	private void writeToFile(String text) {
		this.file.ifPresent(f -> {
			try {
				FileUtils.writeStringToFile(f, text);
			}
			catch (IOException e) {
				this.logger.error(e.getMessage(), e);
			}
		});
	}

	@Override
	public String getFactuurnummer() {
		if (!this.factNr.isPresent()) {
			Optional<String> nr = this.readFromFile();
			String yearNow = String.valueOf(LocalDate.now().getYear());
			if (nr.map(s -> s.endsWith(yearNow)).orElse(false)) {
				// same year
				nr.map(s -> s.substring(0, s.indexOf(yearNow)))
						.map(Integer::parseInt)
						.map(i -> i + 1)
						.map(String::valueOf)
						.map(s -> s.concat(yearNow))
						.ifPresent(this::writeToFile);
				this.factNr = nr;
			}
			else {
				// first in current year
				this.factNr = Optional.of("1".concat(yearNow));
				this.factNr.ifPresent(this::writeToFile);
			}
			return this.factNr.get();
		}
		return this.factNr.get();
	}
}
