package org.rekeningsysteem.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class ApplicationLogger {
	
	private static Logger __instance;

	public static Logger getInstance() {
		if (__instance == null) {
			__instance = fileInstance();
		}
		return __instance;
	}

	public static Logger consoleInstance() {
		Logger logger = LoggerContext.getContext().getRootLogger();

		PatternLayout layout = PatternLayout.newBuilder().withPattern("%d [%p|%l] %m%n").build();
		ConsoleAppender consoleAppender = ConsoleAppender.newBuilder()
			.setLayout(layout)
			.setName("Rekeningsysteem-console-logger")
			.build();
		logger.addAppender(consoleAppender);
		logger.setLevel(Level.ALL);

		return logger;
	}

	public static Logger fileInstance() {
		Logger logger = LoggerContext.getContext().getRootLogger();
		PatternLayout layout = PatternLayout.newBuilder().withPattern("%d [%p|%l] %m%n").build();
		FileAppender fileAppender = FileAppender.newBuilder()
			.setLayout(layout)
			.withFileName("./LogFile.txt")
			.setName("Rekeningsysteem-file-logger")
			.build();
		logger.addAppender(fileAppender);
		logger.setLevel(Level.ALL);

		return logger;
	}
}
