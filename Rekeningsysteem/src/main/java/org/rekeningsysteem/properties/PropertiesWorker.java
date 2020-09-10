package org.rekeningsysteem.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.rekeningsysteem.logging.ApplicationLogger;

public class PropertiesWorker {

	private static PropertiesWorker __instance;

	public static PropertiesWorker getInstance() {
		if (__instance == null) {
			__instance = new PropertiesWorker();
		}
		return __instance;
	}

	public static PropertiesWorker getInstance(Properties properties, File file, Logger logger) {
		return new PropertiesWorker(properties, file, logger);
	}

	private final Properties properties;
	private final File file;
	private Logger logger;

	private PropertiesWorker() {
		this(
			new Properties(),
			Optional.ofNullable(System.getProperty("App.config.file"))
				.map(File::new)
				.filter(File::exists)
				.orElseGet(() -> new File("config.properties")),
			ApplicationLogger.getInstance()
		);
	}

	private PropertiesWorker(Properties properties, File file, Logger logger) {
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
			this.properties.store(stream, "");
		}
		catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public Optional<String> getProperty(PropertyKey key) {
		return this.getProperty(key.getKey());
	}

	public Optional<String> getProperty(String key) {
		String res = this.properties.getProperty(key);
		return res == null ? Optional.empty() : Optional.of(res);
	}
}
