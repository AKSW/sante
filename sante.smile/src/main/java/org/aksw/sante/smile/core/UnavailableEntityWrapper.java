package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.util.List;

public class UnavailableEntityWrapper extends AbstractEntityWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6964081506965472547L;
	
	public static final int PROPLIST_SIZE = 4;
	
	public UnavailableEntityWrapper(String uri, String label) {
		super(uri, label);
	}
	
	public String getDescription() {
		return null;
	}
	
	public String getImage() {
		return null;
	}
	
	public boolean getHasImage() {
		return false;
	}
	
	public List<PropertyWrapper> getLiteralProperties() {
		return null;
	}
	
	public List<PropertyWrapper> getObjectProperties() {
		return null;
	}
	
	public List<PropertyWrapper> getProperties() {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return false;
	}
	
	public AbstractEntityWrapper clone() {
		UnavailableEntityWrapper unavailableEntityWrapper = new UnavailableEntityWrapper(this.getURI(), 
				this.getLabel());
		return unavailableEntityWrapper;
	}
}
