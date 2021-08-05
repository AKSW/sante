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
	private Set<String> labelingProperties = null;
	private static final String tripleEnding = " .";
	
	public Entity(String uri) {
		super(uri);
	}
		
	public Entity(String uri, Set<String> labelingProperties) {
		super(uri);
		this.labelingProperties = labelingProperties;
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
		if(labelingProperties != null && 
				labelingProperties.contains(p.getURI())) {
			o.accept(new ObjectVisitor() {				
				@Override
				public void visit(URIObject uriObject) {
				}	
				@Override
				public void visit(Literal literalObject) {
					addLabel(literalObject);
				}
			});
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
	
	public String getPropertyObjectValue(String uri, String[] langs) {
		for(String lang : langs) {
			String objectValue = getPropertyObjectValue(uri, lang);
			if(objectValue != null) {
				return objectValue;
			}
		}
		return null;
	}
	
	public String getPropertyObjectValue(String uri, String lang) {
		if(properties.containsKey(uri)) {
			List<Property> properties = getProperties(uri);
			for(Property p : properties) {
				Object object = p.getObject();
				if(object.isLiteral()) {
					LiteralObject literal = (LiteralObject) object;
					if(lang == null || lang.equalsIgnoreCase("ANY") || (literal.getLang() != null && (literal.getLang().equalsIgnoreCase(lang)))) {
						return literal.getValue();
					}
				} else {
					return object.getURI();
				}
			}
		}
		return null;
	}
	
	public String getPropertyObjectValue(String uri) {
		if(properties.containsKey(uri)) {
			List<Property> properties = getProperties(uri);
			Property p = properties.get(0);
			Object object = p.getObject();
			if(object.isLiteral()) {
				LiteralObject literal = (LiteralObject) object;
				return literal.getValue();
			} else {
				return object.getURI();
			}
		}
		return null;
	}

	public String getPropertyObjectValue(String[] uris, String defaultValue) {
		return getPropertyObjectValue(uris, new String[] {null}, defaultValue);
	}
	
	public String getPropertyObjectValue(String[] uris, String[] langs, String defaultValue) {
		if(uris != null) {
			for(String uri : uris) {
				if(properties.containsKey(uri)) {
					String value = getPropertyObjectValue(uri, langs);
					if(value != null) {
						return value;
					}
				}
			}
		}
		return defaultValue;
	}
	
	public String asTriples() {
		StringBuilder tripleBuilder = new StringBuilder();
		List<Property> properties = getAllProperties();
		for(Property p : properties) {
			String triple = p.asTriple(getURI());
			tripleBuilder.append(triple);
			tripleBuilder.append(tripleEnding);
			tripleBuilder.append(System.lineSeparator());
		}
		return tripleBuilder.toString();
	}
	
	public String asJSON() {
		StringBuilder tripleBuilder = new StringBuilder();
		List<Property> properties = getAllProperties();
		for(Property p : properties) {
			String triple = p.asTriple(getURI());
			tripleBuilder.append(triple);
			tripleBuilder.append(System.lineSeparator());
		}
		return tripleBuilder.toString();
	}
}
