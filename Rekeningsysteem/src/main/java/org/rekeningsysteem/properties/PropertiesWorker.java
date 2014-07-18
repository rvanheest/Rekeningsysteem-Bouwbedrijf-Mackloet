package org.rekeningsysteem.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.rekeningsysteem.properties.guice.PropertiesFile;
import org.rekeningsysteem.properties.guice.PropertiesObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PropertiesWorker {

	private final Properties properties;
	private final File file;
	private Logger logger;

	@Inject
	public PropertiesWorker(@PropertiesObject Properties properties, @PropertiesFile File file,
			Logger logger) {
		this.properties = properties;
		this.file = file;
		this.logger = logger;
		this.load();
	}

	private void load() {
		try (InputStream stream = FileUtils.openInputStream(this.file)) {
			this.properties.load(stream);
		}
		catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public void setProperty(PropertyKey key, String value) {
		this.setProperty(key.getKey(), value);
	}

	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
		this.save();
	}

	private void save() {
		try (OutputStream stream = FileUtils.openOutputStream(this.file)) {
			this.properties.store(stream, "test123");
		}
		catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public String getProperty(PropertyKey key) {
		return this.getProperty(key.getKey());
	}

	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
}
