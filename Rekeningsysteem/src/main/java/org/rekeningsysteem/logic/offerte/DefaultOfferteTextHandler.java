package org.rekeningsysteem.logic.offerte;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.rekeningsysteem.exception.NoSuchFileException;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

public class DefaultOfferteTextHandler {

	private final Optional<File> file;
	private final Logger logger = ApplicationLogger.getInstance();

	public DefaultOfferteTextHandler() {
		this.file = PropertiesWorker.getInstance()
				.getProperty(PropertyModelEnum.OFFERTE_DEFAULT_TEXT_LOCATION)
				.map(File::new);
	}

	public String getDefaultText() {
		return this.file.filter(File::exists)
				.map(f -> {
					try {
						return FileUtils.readFileToString(f);
					}
					catch (IOException e) {
						// TODO throw exception?
						this.logger.error(e.getMessage(), e);
						return "";
					}
				})
				.orElse("");
	}

	/**
	 * Stores the text in the file that was supplied by the constructor.
	 * @param text the text to be stored
	 * @throws IOException in case an IO exception occurs while writing to the file
	 * @throws NoSuchFileException if there was no file supplied by the constructor
	 */
	public void setDefaultText(String text) throws IOException, NoSuchFileException {
		if (this.file.isPresent()) {
			File f = this.file.get();
			FileUtils.writeStringToFile(f, text, false);
		}
		else {
			throw new NoSuchFileException("Er bestaat geen file waarin deze tekst kan "
					+ "worden opgeslagen.");
		}
	}
}
