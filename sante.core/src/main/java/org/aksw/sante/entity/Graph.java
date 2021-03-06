package org.aksw.sante.entity;

import java.io.Serializable;
import java.util.List;

public class Graph implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5351109515121895911L;
	private List<String> paths;
	private String label;
	
	public List<String> getPaths() {
		return paths;
	}
	
	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<Entity> getEntities() {
		return null;
		
	}
}
