package org.aksw.sante.main;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Sante {
	public static void main(String[] args) throws Exception {
		BasicConfigurator.configure();
		Logger rootLogger = org.apache.log4j.LogManager.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		Command debug = new DebugCommand();
		Command measuresOption = new IndexCommand();
		Command searchOption = new SearchCommand();
		Command serverOption = new ServerCommand();
		CommandFactory factory = new CommandFactory(measuresOption, 
				searchOption, serverOption);
		
		// checking debug mode
		if(debug.canProcess(args)) {
			debug.process(args);
		}
		
		Command option = factory.getCommand(args);
		if(option == null) {
			help();
		} else {
			option.process(args);
		}
	}

	private static void help() {
		System.out.println("sante [command [option]] [-debug]:");
		System.out.println(" search -query <query> -path <path>");
		System.out.println("  -query\t\ta natural language query.");
		System.out.println("  -path\t\tan index directory.");
		System.out.println(" index -endpoint <endpoint> -path <path> [-where <where>]");
		System.out.println("  -endpoint\ta sparql endpoint.");
		System.out.println("  -path\t\ttarget index directory.");
		System.out.println("  -where\t\ta filter where clause in the form of '{?s ?p ?o}'.");
		System.out.println(" server -war <smile.war> -path <path> [-port <port>]");
		System.out.println("  -war\tthe smile war file.");
		System.out.println("  -path\ttarget index directory.");
		System.out.println("  -port\tserver publishing port (default 8080).");
		System.out.println(" -debug\trun the command in debug mode.");
	}
}
