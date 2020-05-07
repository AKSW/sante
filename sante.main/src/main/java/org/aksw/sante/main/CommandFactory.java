package org.aksw.sante.main;

import java.util.Arrays;
import java.util.List;

public class CommandFactory {
	List<Command> options;
	public CommandFactory(List<Command> options) {
		this.options = options;
	}
	
	public CommandFactory(Command... options) {
		this.options = Arrays.asList(options);
	}
	
	public Command getCommand(String args[]) {
		for(Command command : options) {
			if(command.canProcess(args)) {
				return command;
			}
		}
		return null;
	}
}
