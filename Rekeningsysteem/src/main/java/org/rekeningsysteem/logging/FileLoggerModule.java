package org.rekeningsysteem.logging;

import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.inject.AbstractModule;

public class FileLoggerModule extends AbstractModule {

	@Override
	protected void configure() {
		Logger rootLogger = Logger.getRootLogger();
		String pattern = "%d [%p|%l] %m%n";

		this.appendFile(rootLogger, pattern, "./LogFile.txt");

		this.bind(Logger.class).toInstance(rootLogger);
	}

	private void appendFile(Logger rootLogger, String pattern, String fileName) {
		FileAppender fileAppender;
		try {
			fileAppender = new FileAppender(new PatternLayout(pattern), fileName);
			fileAppender.setThreshold(Level.ALL);
			fileAppender.activateOptions();
			rootLogger.addAppender(fileAppender);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
