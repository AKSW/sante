package org.aksw.sante.main;

public class Sante {
	public static void main(String[] args) throws Exception {
		Command measuresOption = new IndexCommand();
		Command datasetsOption = new SearchCommand();
		CommandFactory factory = new CommandFactory(measuresOption, 
				datasetsOption);
		Command option = factory.getCommand(args);
		if(option == null) {
			help();
		} else {
			option.process(args);
		}
	}

	private static void help() {
		System.out.println("sante [command [option]]:");
		System.out.println(" search -query <query> -path <path>");
		System.out.println("  -query\t\ta natural language query.");
		System.out.println("  -path\t\tan index directory.");
		System.out.println(" index -endpoint <endpoint> -path <path>");
		System.out.println("  -endpoint\ta sparql endpoint.");
		System.out.println("  -path\t\ttarget index directory.");
		System.out.println(" server -war <smile.war> -path <path> [-port <port>]");
		System.out.println("  -war\tthe smile war file.");
		System.out.println("  -path\ttarget index directory.");
		System.out.println("  -port\tserver publishing port (default 8080).");
	}
}
