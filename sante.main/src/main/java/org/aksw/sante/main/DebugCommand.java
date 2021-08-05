package org.aksw.sante.main;

import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DebugCommand extends AbstractSimpleMapCommand {
 
	private static Logger logger = Logger.getLogger(DebugCommand.class);
	
	public DebugCommand() {
		super("-debug");
	}

	@Override
	public void process(Map<String, String[]> commands) throws Exception {
		BasicConfigurator.configure();
		Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
		rootLogger.setLevel(Level.DEBUG);
		logger.log(Level.DEBUG, "Running SANTé in DEBUG mode.");
	}


}
