package org.aksw.sante.main;

public interface Command {	
	public abstract boolean canProcess(String[] args);
	public abstract void process(String[] args) throws Exception;
}