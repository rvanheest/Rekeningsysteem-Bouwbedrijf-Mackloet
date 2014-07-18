package org.rekeningsysteem.logic.factuurnummer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class FileFactuurnummerManager implements FactuurnummerManager {

	private File file;
	private String factNr;
	private Logger logger;

	@Inject
	public FileFactuurnummerManager(PropertiesWorker worker, @Assisted PropertyKey file,
			Logger logger) {
		this.file = new File(worker.getProperty(file));
		this.logger = logger;
	}

	@Override
	public String getFactuurnummer() {
		if (this.factNr == null) {
			try {
				String factnr = FileUtils.readFileToString(this.file);
				String yearNow = String.valueOf(new Datum().getJaar());
				String newFnr;
				if (factnr.endsWith(yearNow)) {
					// same year
					String[] oldNr = factnr.split(yearNow);
					int newNumber = Integer.parseInt(oldNr[0]) + 1;
					newFnr = String.valueOf(newNumber) + yearNow;
				}
				else {
					// other year
					newFnr = "1" + yearNow;
					factnr = newFnr;
				}
				FileUtils.writeStringToFile(this.file, newFnr);
				this.factNr = factnr;

				return factnr;
			}
			catch (IOException e) {
				this.logger.error(e.getMessage(), e);
			}
		}
		return this.factNr;
	}
}
