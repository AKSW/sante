package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.Property;

public class PropertyWrapper extends ResourceWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6964081506965472547L;
	
	private List<ObjectWrapper> objects = null;
	private Set<Literal> labels = null;

	public PropertyWrapper(Property[] properties, String defaultLabel, String... langs) {
		super(properties[0].getURI(), properties[0].getLabel(langs, properties[0].getURI()));
		this.labels = properties[0].getLabels();
		this.objects = new ArrayList<ObjectWrapper>();
		for(Property property : properties) {
			ObjectWrapper objectWrapper = new ObjectWrapper(property.getObject(), defaultLabel, langs);
			objects.add(objectWrapper);
		}
	}
	
	public Set<Literal> getLabels() {
		return labels;
	}
	
	public PropertyWrapper(List<Property> properties, String defaultLabel, String... langs) {
		this(properties.toArray(new Property[properties.size()]), defaultLabel, langs);
	}

	public List<ObjectWrapper> getObjects() {
		return objects;
	}
}