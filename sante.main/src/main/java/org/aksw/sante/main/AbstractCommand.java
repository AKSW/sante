package org.aksw.sante.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public abstract class AbstractCommand implements Command {
	private String label = null;
	
	public AbstractCommand(String label) {
		this.label = label;
	}
	
	public abstract Options getOptions();

	public String getLabel() {
		return label;
	}

    public boolean canProcess(CommandLine commandLine) {
    	return commandLine.hasOption(label);
    }
    
    public abstract void process(CommandLine commandLine);
}
