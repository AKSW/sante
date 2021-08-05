package org.aksw.sante.smile.view;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.AbstractEntityWrapper;
import org.aksw.sante.smile.core.IndexedEntityWrapper;
import org.aksw.sante.smile.core.ObjectWrapper;
import org.aksw.sante.smile.core.PropertyWrapper;
import org.aksw.sante.smile.core.ResourceWrapper;
import org.aksw.sante.smile.core.SmileParams;
import org.aksw.sante.smile.core.UnavailableEntityWrapper;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.SearchEngine;

@Named
@ViewScoped
public class DetailsViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173739028408143306L;

	private Map<String, AbstractEntityWrapper> resultEntityMap = new HashMap<String, AbstractEntityWrapper>();
	private Map<String, ResourceWrapper> detailResourceMap = new HashMap<String, ResourceWrapper>();

	private volatile Set<String> prefixes = new HashSet<String>();

	private String indexDir = null;
	private volatile String[] labelingProperties = null;
	private volatile String[] imageProperties = null;
	private volatile String[] abstractProperties = null;
	private volatile String[] relevancePropertyOrder = null;
	private volatile String[] hidenProperties = null;
	private String selectedEntryId = "";
	private AbstractEntityWrapper selectedEntry = null;
	private List<AbstractEntityWrapper> openEntities = new ArrayList<AbstractEntityWrapper>();

	public DetailsViewController() {
		openEntities.clear();
		loadProperties();
		openEntry();
	}

	public void setSelectedEntry(AbstractEntityWrapper entry) {
		this.selectedEntry = entry;
	}

	public AbstractEntityWrapper getSelectedEntry() {
		return selectedEntry;
	}

	public String getSelectedEntryId() {
		return selectedEntryId;
	}

	public void setSelectedEntryId(String selectedEntryId) {
		this.selectedEntryId = selectedEntryId;
	}

	public void selectEntry() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		selectedEntryId = params.get("uri");
		AbstractEntityWrapper openEntity = resultEntityMap.get(selectedEntryId);
		selectedEntry = openEntity;
		openEntities.clear();
		openEntities.add(openEntity);
		loadResources(openEntity);
	}

	public void loadResources(AbstractEntityWrapper entry) {
		detailResourceMap.clear();
		for (PropertyWrapper property : entry.getProperties()) {
			detailResourceMap.put(property.getId(), property);
			for (ObjectWrapper o : property.getObjects()) {
				detailResourceMap.put(o.getId(), o);
			}
		}
	}

	public void openEntry() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String openEntryId = params.get("uri");
		if(openEntryId != null) {
			AbstractEntityWrapper openEntry = resultEntityMap.get(openEntryId);
			if (openEntry == null) {
				File index = new File(indexDir);
				SearchEngine searchEngine;
				try (IndexReader reader = SearchEngine.newReader(index.toPath());) { 
					searchEngine = new SearchEngine(reader);
					Entity entity = searchEngine.getEntity(openEntryId);
					if (entity != null) {
						openEntry = new IndexedEntityWrapper(entity, 
								labelingProperties, 
								imageProperties,
								abstractProperties, 
								relevancePropertyOrder, 
								hidenProperties);
						loadResources(openEntry);
					} else {
						openEntry = new UnavailableEntityWrapper(null, 
								openEntryId);
					}
					resultEntityMap.put(openEntry.getId(), 
							openEntry);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			selectedEntry = openEntry;
			selectedEntryId = openEntry.getId();
			AbstractEntityWrapper loadedEntry = getEntity(openEntities, 
					openEntryId);
			if (loadedEntry != null) {
				openEntities = openEntities.subList(0, 
						openEntities.indexOf(loadedEntry) + 1);
			} else {
				openEntities.add(openEntry);
			}
		} else {
			UnavailableEntityWrapper openEntry = new UnavailableEntityWrapper(null, 
					null);
			resultEntityMap.put(null, 
					openEntry);
		}
	}

	public AbstractEntityWrapper getEntity(List<AbstractEntityWrapper> list, String id) {
		for (AbstractEntityWrapper entity : list) {
			if (entity.getId().equals(id)) {
				return entity;
			}
		}
		return null;
	}

	public List<AbstractEntityWrapper> getOpenEntities() {
		return openEntities;
	}

	public void loadProperties() {
		SmileParams params = SmileParams.getInstance();
		this.indexDir = params.indexPath;
		this.hidenProperties = params.hidenProperties;
		this.imageProperties = params.imageProperties;
		this.labelingProperties = params.labelingProperties;
		this.abstractProperties = params.abstractProperties;
		if(params.prefixes != null) {
			for(String prefix : params.prefixes) {
				prefixes.add(prefix);
			}
		}
	}
}
