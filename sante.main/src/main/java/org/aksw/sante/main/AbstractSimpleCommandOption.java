package org.aksw.sante.main;

public abstract class AbstractSimpleCommandOption implements Command {

	private String option;
	
	public AbstractSimpleCommandOption(String option) {
		this.option = option;
	}
	
	public boolean canProcess(String[] args) {		
		return args.length == 1 && args[0].contains(option); 
	}
	
}
