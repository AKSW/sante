package org.aksw.sante.smile.core;

import org.aksw.sante.entity.Resource;

public abstract class ResourceWrapper extends Resource {

	private static final long serialVersionUID = 4495475906376841150L;
	/**
	 * 
	 */
	private String label = null;
	private String snippet = null;
	
	public ResourceWrapper() {
		super(null);
	}
	
	public ResourceWrapper(String uri) {
		super(uri);
		
	}
	
	public ResourceWrapper(String uri, String label) {
		this(uri);
		this.label = label;
		this.snippet = label;
	}
	
	public boolean isAvailable() {
		return false;
	}	

	public String getId() {
		return getURI();
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getSnippet() {
		return snippet;
	}
	
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}
