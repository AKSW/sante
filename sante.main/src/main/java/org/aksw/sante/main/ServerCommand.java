package org.aksw.sante.main;

import java.util.Map;

public class ServerCommand extends AbstractSimpleMapCommand {
	public ServerCommand() {
		super("server");
	}
	
	@Override
	public boolean canProcess(Map<String, String[]> commands) {
		return commands.containsKey("-path") &&
				commands.containsKey("-war"); 
	}

	@Override
	public void process(Map<String, String[]> commands) throws Exception {
		String indexParam = commands.get("-path")[0];
		String warParam = commands.get("-war")[0];
		String port = "8080";
		if(commands.containsKey("-port")) {
			port = commands.get("-port")[0];
		}
		Server server = new Server(Integer.parseInt(port));
		server.start(warParam, indexParam);
	}
}
