package org.aksw.sante.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Entity extends Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5720330581700111849L;

	private HashMap<String, List<Property>> properties = new HashMap<String, List<Property>>();
	private HashMap<String, Property> propertyObject = new HashMap<String, Property>();
	
	public Entity() {
		super(null, null);
	}
	
	public Entity(String uri) {
		super(uri);
	}
	
	public Entity(String uri, List<Literal> labels) {
		super(uri, labels);
	}

	public Entity(List<Literal> labels) {
		super(null, labels);
	}

	public void addProperty(Property p) {
		Object o = p.getObject();
		String pObjectId = p.getURI() + o.getURI();
		Property property = propertyObject.get(pObjectId);
		if(property != null) { // adding labels
			property.addLabels(p.getLabels());
			if(!o.isLiteral()) {
				((URIObject )property.getObject()).
				addLabels(((URIObject)o).getLabels()); // add labels to the object
			}
		} else { // adding property
			List<Property> uriProperties = properties.get(p.getURI());
			if(uriProperties == null) {
				uriProperties = new ArrayList<Property>();
				properties.put(p.getURI(), uriProperties);
			}
			uriProperties.add(p);
			propertyObject.put(pObjectId, p);
		}
	}

	public List<Property> getProperties(String uri) {
		return properties.get(uri);
	}
	
	public List<Property> getAllProperties() {
		List<Property> allProperties = new ArrayList<Property>();
		for(List<Property> entryProperties : properties.values()) {
			allProperties.addAll(entryProperties);
		}
		return allProperties;
	}
	
	public Set<String> getPropertyURIs() {
		return properties.keySet();
	}

	public String getPropertyObjectValue(String uri, String lang, String defaultValue) {
		String value = getPropertyObjectValue(uri, lang);
		if(value == null) {
			return defaultValue;
		}
		return value;
	}
	
	public String getPropertyObjectValue(String uri, String lang) {
		if(properties.containsKey(uri)) {
			List<Property> properties = getProperties(uri);
			for(Property p : properties) {
				LiteralObject object = (LiteralObject) p.getObject();
				if(object.getLang() == lang) {
					return object.getValue();
				}
			}
		}
		return null;
	}
	
	public String getPropertyObjectValue(String uri) {
		if(properties.containsKey(uri)) {
			List<Property> properties = getProperties(uri);
			Property p = properties.get(0);
			LiteralObject object = (LiteralObject) p.getObject();
			return object.getValue();
		}
		return null;
	}

	public String getPropertyObjectValue(String[] uris, String defaultValue) {
		for(String uri : uris) {
			if(properties.containsKey(uri)) {
				return getPropertyObjectValue(uri);
			}
		}
		return defaultValue;
	}
}
