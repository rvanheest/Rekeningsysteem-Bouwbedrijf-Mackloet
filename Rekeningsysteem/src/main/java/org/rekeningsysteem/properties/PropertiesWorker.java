package org.rekeningsysteem.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.logging.ApplicationLogger;

public class PropertiesWorker {

	private static PropertiesWorker __instance;

	public static PropertiesWorker getInstance() {
		if (__instance == null) {
			__instance = new PropertiesWorker();
		}
		return __instance;
	}

	public static PropertiesWorker getInstance(Properties properties, Path path, Logger logger) {
		return new PropertiesWorker(properties, path, logger);
	}

	private final Properties properties;
	private final Path path;
	private final Logger logger;

	private PropertiesWorker() {
		this(
			new OrderedProperties(),
			Optional.ofNullable(System.getProperty("App.config.file"))
				.map(Paths::get)
				.filter(Files::exists)
				.orElseGet(() -> Paths.get("config.properties")),
			ApplicationLogger.getInstance()
		);
	}

	private PropertiesWorker(Properties properties, Path path, Logger logger) {
		this.properties = properties;
		this.path = path;
		this.logger = logger;
		this.load();
	}

	private void load() {
		try (InputStream stream = FileUtils.openInputStream(this.path.toFile())) {
			this.properties.load(stream);
		}
		catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public void setProperty(PropertyKey key, String value) {
		this.setProperty(key.getKey(), value);
	}

	public void setProperty(PropertyKey key, Boolean value) {
		this.setProperty(key.getKey(), Boolean.toString(value));
	}

	public void setProperty(PropertyKey key, Path value) {
		this.setProperty(key.getKey(), value.toString());
	}

	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
		this.save();
	}

	private void save() {
		try (OutputStream stream = FileUtils.openOutputStream(this.path.toFile())) {
			this.properties.store(stream, "");
		}
		catch (IOException e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	public Optional<String> getProperty(PropertyKey key) {
		return this.getProperty(key.getKey());
	}

	public Optional<Double> getDoubleProperty(PropertyKey key) {
		return this.getProperty(key).map(Double::parseDouble);
	}

	public Optional<Boolean> getBooleanProperty(PropertyKey key) {
		return this.getProperty(key).map(Boolean::parseBoolean);
	}

	public boolean getBooleanProperty(PropertyKey key, boolean defaultValue) {
		return this.getBooleanProperty(key).orElse(defaultValue);
	}

	public Optional<Currency> getCurrencyProperty(PropertyKey key) {
		return this.getProperty(key).map(Currency::getInstance);
	}

	public Currency getCurrencyProperty(PropertyKey key, String defaultValue) {
		return this.getCurrencyProperty(key).orElseGet(() -> Currency.getInstance(defaultValue));
	}

	public Optional<DateTimeFormatter> getDateTimeFormatterProperty(PropertyKey key) {
		return this.getProperty(key).map(DateTimeFormatter::ofPattern);
	}

	public Optional<Path> getPathProperty(PropertyKey key) {
		return this.getProperty(key)
				.map(s -> s.replace("\\\\", "/").replace("\\", "/"))
				.map(Paths::get)
				.map(Path::toAbsolutePath);
	}

	public Optional<URI> getUriProperty(PropertyKey key) {
		return this.getPathProperty(key).map(Path::toUri);
	}

	private Optional<String> getProperty(String key) {
		String res = this.properties.getProperty(key);
		return res == null ? Optional.empty() : Optional.of(res);
	}
}
