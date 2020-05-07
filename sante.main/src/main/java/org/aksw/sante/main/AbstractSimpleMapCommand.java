package org.aksw.sante.main;

import java.util.Map;

public abstract class AbstractSimpleMapCommand extends MapCommand {

	private String option;
	
	public AbstractSimpleMapCommand(String option) {
		this.option = option;
	}

	@Override
	public boolean canProcess(Map<String, String[]> commands) {
		return commands.containsKey(option);
	}
	
	public String getOption() {
		return option;
	}
}
