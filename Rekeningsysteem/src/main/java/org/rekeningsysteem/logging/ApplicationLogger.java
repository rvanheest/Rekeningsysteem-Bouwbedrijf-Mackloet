package org.rekeningsysteem.logging;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class ApplicationLogger {
	
	private static Logger __instance;

	public static Logger getInstance() {
		if (__instance == null) {
			// TODO change consoleInstance() to fileInstance() before production
			__instance = consoleInstance();
		}
		return __instance;
	}

	public static Logger consoleInstance() {
		Logger logger = Logger.getRootLogger();

		ConsoleAppender consoleAppender = new ConsoleAppender();
		PatternLayout layout = new PatternLayout("%d [%p|%l] %m%n");
		consoleAppender.setLayout(layout);
		consoleAppender.setThreshold(Level.ALL);
		consoleAppender.activateOptions();
		logger.addAppender(consoleAppender);

		return logger;
	}

	public static Logger fileInstance() {
		Logger logger = Logger.getRootLogger();
		FileAppender fileAppender;
		try {
			PatternLayout layout = new PatternLayout("%d [%p|%l] %m%n");
			fileAppender = new FileAppender(layout, "./LogFile.txt");
			fileAppender.setThreshold(Level.ALL);
			fileAppender.activateOptions();
			logger.addAppender(fileAppender);
		}
		catch (IOException e) {
			// should not happen
			consoleInstance().fatal("Failure in FileAppender", e);
		}

		return logger;
	}
}
