package org.aksw.sante.smile.view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.About;
import org.aksw.sante.smile.core.AbstractEntityWrapper;
import org.aksw.sante.smile.core.IndexedEntityWrapper;
import org.aksw.sante.smile.core.ObjectWrapper;
import org.aksw.sante.smile.core.PropertyWrapper;
import org.aksw.sante.smile.core.ResourceWrapper;
import org.aksw.sante.smile.core.SmileParams;
import org.aksw.sante.smile.core.StructuredSnippetGeneartor;
import org.aksw.sante.smile.core.UnavailableEntityWrapper;
import org.primefaces.json.JSONArray;
import org.sante.lucene.EntityVisitor;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SanteEngine;

import freemarker.template.TemplateException;

@ManagedBean(name="searchViewController")
@SessionScoped
public class SearchViewController implements Serializable {
	
	/**
	 * 
	 */	
	private static final long serialVersionUID = -2173739028408143306L;
	
	private Map<String, AbstractEntityWrapper> resultEntityMap = new HashMap<String, AbstractEntityWrapper>();
	private Map<String, ResourceWrapper> detailResourceMap = new HashMap<String, ResourceWrapper>();
	private List<AbstractEntityWrapper> entities = new ArrayList<AbstractEntityWrapper>();
	
	
	private String[] labelingProperties = null;
	private String[] imageProperties = null;
	private String[] abstractProperties = null;
	private String[] relevancePropertyOrder = null;
	private String[] hidenProperties = null;
	private String[] graphs = null;
	
	private long totalHits = 0;
	private String inputTextQuery = "";
	private int pageSize = 10;
	private int page = 0;
	private String indexDir = null;
	private String selectedEntryId = "";
	private String contentFilter = null;
	private AbstractEntityWrapper selectedEntry = null;
	private List<AbstractEntityWrapper> openEntities = new ArrayList<AbstractEntityWrapper>();
	private Map<String, String> classMap = new HashMap<String, String>(); {
		classMap.put("class", "CLASS");
		classMap.put("entity", "ENTITY");
		classMap.put("ontology", "ONTOLOGY");
		classMap.put("property", "PROPERTY");
	}

	public SearchViewController() {
		loadProperties();
	}
	
	public String getInputTextQuery() {
		return inputTextQuery;
	}

	public void setInputTextQuery(String inputTextQuery) {
		this.inputTextQuery = inputTextQuery;
	}

	public List<AbstractEntityWrapper> getEntityLazyModel() {
        return entities;
    }

	public boolean isQueryEmpty() {
		if(inputTextQuery == null) {
			return true;
		}
		return inputTextQuery.isEmpty();
	}
	
	public void filterContent() {
		Map<String, String> params = FacesContext.getCurrentInstance().
				getExternalContext().
				getRequestParameterMap();
		String filter = params.get("content");
		this.contentFilter = classMap.get(filter);
	}
	
	public AbstractEntityWrapper getAbout() throws IOException, 
								TemplateException,
								URISyntaxException {
		SanteEngine searchEngine = new SanteEngine();
		StructuredSnippetGeneartor snippetGenerator = new StructuredSnippetGeneartor();
		Entity about = searchEngine.about();
		AbstractEntityWrapper entityWrapper = new IndexedEntityWrapper(about,
				labelingProperties,
				imageProperties,
				abstractProperties,
				relevancePropertyOrder,
				hidenProperties);
		About aboutWrapper = new About(about);
		entityWrapper.setSnippet(snippetGenerator.generate(inputTextQuery,
				aboutWrapper));
		return entityWrapper;
	}

	public void nextPage() {
		page++;
		loadContent();
	}

	public void loadContent() {
		entities.clear();
		try {
			int offset = page * pageSize;
			File index = new File(indexDir);
			Path indexPath = index.toPath();
			try {
				SanteEngine searchEngine = new SanteEngine(indexPath);
				ArrayList<String> filters = new ArrayList<String>();
				if(contentFilter != null) {
					filters.add(contentFilter);
				}
				for(String graph : graphs) {
					filters.add(graph);
				}
				StructuredSnippetGeneartor snippetGenerator = new StructuredSnippetGeneartor();
				try(ResultSet result = searchEngine.search(inputTextQuery, offset, pageSize, filters);) {
					totalHits = result.getTotalHits();
					result.accept(new EntityVisitor() {
						@Override
						public boolean visit(Entity entity) throws Exception {
							AbstractEntityWrapper entityWrapper = new IndexedEntityWrapper(entity, 
									labelingProperties,
									imageProperties,
									abstractProperties,
									relevancePropertyOrder,
									hidenProperties);
							entities.add(entityWrapper);
							resultEntityMap.put(entityWrapper.getId(), entityWrapper);
							entityWrapper.setSnippet(snippetGenerator.generate(inputTextQuery, entityWrapper));
							return true;
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void selectEntry()  {
		Map<String, String> params = FacesContext.getCurrentInstance().
				getExternalContext().
				getRequestParameterMap();
		selectedEntryId = params.get("id");
		AbstractEntityWrapper openEntity = resultEntityMap.get(selectedEntryId);
		selectedEntry = openEntity;
		openEntities.clear();
		openEntities.add(openEntity);
		loadResources(openEntity);
	}
	
	public void loadResources(AbstractEntityWrapper entry) {
		detailResourceMap.clear();
		for(PropertyWrapper property : entry.getProperties()) {
			detailResourceMap.put(property.getId(), property);
			for(ObjectWrapper o : property.getObjects()) {
				detailResourceMap.put(o.getId(), o);
			}
		}
	}

	public void openEntry()  {
		Map<String, String> params = FacesContext.getCurrentInstance().
				getExternalContext().
				getRequestParameterMap();
		String openEntryId  = params.get("id");
		AbstractEntityWrapper openEntry = resultEntityMap.get(openEntryId);
		if(openEntry == null) {
			File index = new File(indexDir);
			Path indexPath = index.toPath();
			SanteEngine searchEngine;
			try {
				searchEngine = new SanteEngine(indexPath);
				Entity entity = searchEngine.getEntity(openEntryId);
				if(entity != null) {
					openEntry = new IndexedEntityWrapper(entity, 
						labelingProperties,
						imageProperties,
						abstractProperties,
						relevancePropertyOrder,
						hidenProperties
					);
					loadResources(openEntry);
				} else {
					ResourceWrapper resource = detailResourceMap.get(openEntryId);
					openEntry = new UnavailableEntityWrapper(openEntryId, resource.getLabel());
				}
				resultEntityMap.put(openEntry.getId(), openEntry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		selectedEntry = openEntry;
		selectedEntryId = openEntry.getId();
		AbstractEntityWrapper loadedEntry = getEntity(openEntities, openEntryId);
		if(loadedEntry != null) {
			openEntities = openEntities.subList(0, openEntities.indexOf(loadedEntry) + 1);
		} else {
			openEntities.add(openEntry);
		}
	}
	
	public AbstractEntityWrapper getEntity(List<AbstractEntityWrapper> list, String id) {
		for(AbstractEntityWrapper entity : list) {
			if(entity.getId().equals(id)) {
				return entity;
			}
		}
		return null;
	}

	public List<AbstractEntityWrapper> getOpenEntities() {
		return openEntities;
	}
	
	public void loadProperties() {
		Properties properties = new Properties();
		try (InputStream is = SearchViewController.class.getResourceAsStream("/config.properties");) {
			properties.load(is);
			indexDir = SmileParams.index;
			if(indexDir == null) {
				indexDir = properties.getProperty("index.dir");
			}
			hidenProperties = loadJSONArray("properties.hidden", properties);
			imageProperties = loadJSONArray("properties.image", properties);
			labelingProperties = loadJSONArray("properties.labeling", properties);
			abstractProperties = loadJSONArray("properties.abstract", properties);
			graphs = loadJSONArray("graphs", properties);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String[] loadJSONArray(String property, Properties properties) {
		String value = properties.getProperty(property);
		if(value == null || value.isEmpty()) {
			return new String[]{};
		}
		JSONArray valueArray = new JSONArray(value);
		List<String> entries = new ArrayList<String>();
		for(Object v : valueArray) {
			entries.add(v.toString());
		}
		return entries.toArray(new String[entries.size()]);
	}

	public String getIndexDir() {
		String indexPath = SmileParams.index; 
		if(indexPath == null) {
			Properties prop = new Properties();
			try (InputStream is = SearchViewController.class.getResourceAsStream("/config.properties");){
				// load a properties file
				prop.load(is);
				// get the property value and print it out
				indexPath = prop.getProperty("index.dir");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return indexPath;
	}

	public void resetInputTextQuery() {
		inputTextQuery = "";
	}
	
	public void resetPage() {
		page = 0;
	}

	public Long getTotalHits() {
		return totalHits;
	}
}
