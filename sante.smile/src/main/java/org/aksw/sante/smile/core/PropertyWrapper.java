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
	private List<String> objectURIs = null;
	private boolean isImage = false;

	public PropertyWrapper(Property[] properties, String defaultLabel, String... langs) {
		super(properties[0].getURI(), properties[0].getLabel(langs, properties[0].getURI()));
		this.labels = properties[0].getLabels();
		this.objects = new ArrayList<ObjectWrapper>();
		this.objectURIs = new ArrayList<String>();
		for(Property property : properties) {
			String objectURI = property.getObject().getURI();
			ObjectWrapper objectWrapper = new ObjectWrapper(property.getObject(), objectURI, langs);
			objectURIs.add(objectURI);
			objects.add(objectWrapper);
		}
	}
	
	public boolean isImage() {
		return isImage;
	}
	
	public void isImage(boolean isImage) {
		this.isImage = isImage;
	}
	
	public List<String> getObjectURIs() {
		return objectURIs;
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