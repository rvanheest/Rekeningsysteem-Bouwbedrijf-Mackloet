package org.rekeningsysteem.logging;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.inject.AbstractModule;

public class ConsoleLoggerModule extends AbstractModule {

	@Override
	protected void configure() {
		Logger rootLogger = Logger.getRootLogger();
		String pattern = "%d [%p|%l] %m%n";

		this.appendConsole(rootLogger, pattern);

		this.bind(Logger.class).toInstance(rootLogger);
	}

	private void appendConsole(Logger rootLogger, String pattern) {
		ConsoleAppender consoleAppender = new ConsoleAppender();
		consoleAppender.setLayout(new PatternLayout(pattern));
		consoleAppender.setThreshold(Level.ALL);
		consoleAppender.activateOptions();
		rootLogger.addAppender(consoleAppender);
	}
}
