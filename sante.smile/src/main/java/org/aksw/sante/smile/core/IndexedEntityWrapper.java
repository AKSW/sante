package org.aksw.sante.smile.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Property;

public class IndexedEntityWrapper extends AbstractEntityWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6964081506965472547L;

	private Entity entity = null;
	private String description = null;
	private String label = null;
	private String image = null;
	private List<PropertyWrapper> properties = null;

	private String[] labelingProperties = new String[]{"http://www.w3.org/2000/01/rdf-schema#label"};
	private String[] imageProperties = new String[]{"http://xmlns.com/foaf/0.1/Image"};
	private String[] abstractProperties = new String[]{"http://www.w3.org/2000/01/rdf-schema#comment"};
	private String[] relevancePropertyOrder = new String[]{""};
	private String[] hideProperties = new String[]{""};
	private String[] langArray = null; 
	private List<String> langs = null;
	
	public IndexedEntityWrapper(Entity entity,
			String[] labelingProperties,
			String[] imageProperties,
			String[] abstractProperties,
			String[] relevancePropertyOrder,
			String[] hideProperties,
			List<String> langs) {
		super(entity.getURI());
		this.labelingProperties = labelingProperties;
		this.imageProperties = imageProperties;
		this.abstractProperties = abstractProperties;
		this.relevancePropertyOrder = relevancePropertyOrder;
		this.hideProperties = hideProperties;
		this.entity = entity;
		this.langs = langs;
		if(langs != null) {
			langArray = new String[langs.size() + 1];
			int i = 0;
			for(String lang : langs) {
				langArray[i] = lang;
				i++;
			}
			langArray[i] = Entity.ANY_LANG;
		}
		loadImage();
		loadAbstract();
		loadProperties();
	}

	public IndexedEntityWrapper(Entity entity) {
		super(entity.getURI());
		this.entity = entity;
		loadImage();
		loadAbstract();
		loadProperties();
	}
	
	public boolean isSolution() {
		return false;
	}	

	public String getId() {
		return entity.getURI();
	}
	
	public String getURI() {
		return entity.getURI();
	}
	
	public String getEncodedURI() {
		return entity.getURI();
	}
	
	public String getLabel() {
		if(label == null) {
			label = entity.getPropertyObjectValue(labelingProperties, langArray, entity.getURI());
		}
		return label;
	}
	
	private void loadImage() {
		if(image == null) {
			image = entity.getPropertyObjectValue(imageProperties, "");
		}
	}
	
	private void loadAbstract() {
		if(description == null && abstractProperties != null) {
			description = entity.getPropertyObjectValue(abstractProperties, langArray, "");
		}
	}
	
	private void loadProperties() {
		if(properties == null) {
			List<Property> entityProperties = entity.getAllProperties();
			properties = extractRelevantProperties(entityProperties, hideProperties);
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getImage() {
		return image;
	}
	
	public boolean getHasImage() {
		return !image.isEmpty();
	}
	
	public List<PropertyWrapper> getLiteralProperties() {
		return properties;
	}
	
	public List<PropertyWrapper> getObjectProperties() {
		return properties;
	}
	
	public List<PropertyWrapper> getProperties() {
		return properties;
	}

	private List<PropertyWrapper> extractRelevantProperties(List<Property> entityProperties,
			String[] hideProperties) {
		List<PropertyWrapper> relProperties = new ArrayList<PropertyWrapper>();
		List<String> imagePropertiesList = Arrays.asList(imageProperties);
		List<String> hidePropertyList = null;
		if(hideProperties != null) {
			hidePropertyList = Arrays.asList(hideProperties);
		}
		for(String propertyURI : entity.getPropertyURIs()) {
			if(hidePropertyList == null || 
					!hidePropertyList.contains(propertyURI)) {
				List<Property> properties = entity.getProperties(propertyURI);
				PropertyWrapper propertyWrapper = new PropertyWrapper(properties, propertyURI, langArray);
				propertyWrapper.isImage(imagePropertiesList.contains(propertyURI));
				relProperties.add(propertyWrapper);
			}
		}
		return relProperties;
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public AbstractEntityWrapper clone() {
		IndexedEntityWrapper indexedEntityWrapper = new IndexedEntityWrapper(entity, 
				labelingProperties,
				imageProperties,
				abstractProperties,
				relevancePropertyOrder,
				hideProperties,
				langs);
		return indexedEntityWrapper;
	}
}
