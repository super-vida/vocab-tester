package cz.prague.vida.vocab;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * The Class VocabLogger.
 */
public class VocabLogger {

	/** The logger. */
	public static final Logger LOGGER =  Logger.getLogger("vocab");

	static {
		LOGGER.setLevel(Level.ALL);
		LOGGER.info("initializing - trying to load configuration file ...");

		try (InputStream configFile = VocabLogger.class.getResourceAsStream("logging.properties")) {
			LogManager.getLogManager().readConfiguration(configFile);
		}
		catch (IOException ex) {
			LOGGER.log(Level.SEVERE, "WARNING: Could not open configuration file", ex);
		}
		LOGGER.info("starting myApp");
	}

	private VocabLogger() {
		super();
	}

}
