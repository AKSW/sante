package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractEntityWrapper extends ResourceWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6115503655288249317L;
	
	public AbstractEntityWrapper(String uri, String label) {
		super(uri, label);
	}
	
	public AbstractEntityWrapper(String uri) {
		super(uri);
	}
	
	public String getUri() {
		return getURI();
	}

	public abstract boolean isAvailable();
	
	public abstract String getDescription();
	
	public abstract String getImage();
	
	public abstract boolean getHasImage();
	
	public abstract List<PropertyWrapper> getLiteralProperties();
	
	public abstract List<PropertyWrapper> getObjectProperties();
	
	public abstract List<PropertyWrapper> getProperties();
	
	public abstract AbstractEntityWrapper clone();
}
