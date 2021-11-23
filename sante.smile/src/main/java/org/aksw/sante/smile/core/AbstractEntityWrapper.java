package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class AbstractEntityWrapper extends ResourceWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6115503655288249317L;
	
	private final static Logger logger = Logger.getLogger(AbstractEntityWrapper.class);
	
	public AbstractEntityWrapper(String uri, String label) {
		super(uri, label);
	}
	
	public AbstractEntityWrapper(String uri) {
		super(uri);
	}
	
	public String getUri() {
		return getURI();
	}
	
	public String getEncodedUri() {
		try {
			return URLEncoder.encode(getURI(), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return "";
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
