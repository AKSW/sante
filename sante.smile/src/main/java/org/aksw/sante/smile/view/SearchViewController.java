package org.aksw.sante.smile.view;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.About;
import org.aksw.sante.smile.core.AbstractEntityWrapper;
import org.aksw.sante.smile.core.EntitySnippetGeneartor;
import org.aksw.sante.smile.core.IndexedEntityWrapper;
import org.aksw.sante.smile.core.ItemAutoCompleteSnippetGeneartor;
import org.aksw.sante.smile.core.ObjectWrapper;
import org.aksw.sante.smile.core.PropertyWrapper;
import org.aksw.sante.smile.core.ResourceWrapper;
import org.aksw.sante.smile.core.SmileParams;
import org.aksw.sante.smile.core.UnavailableEntityWrapper;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.primefaces.event.SelectEvent;
import org.sante.lucene.EntityVisitor;
import org.sante.lucene.Filter;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.sante.lucene.Suggestion;

import freemarker.template.TemplateException;

@Named
@ViewScoped
public class SearchViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173739028408143306L;
	
	private static Logger logger = Logger.getLogger(SearchViewController.class);
	
	@Inject
	private SuggestionList suggestionList = null;
	
	@Inject
	private FilterViewController filterViewController = null;
	
	@Inject
	private UserSessionController sessionController = null;

	private Map<String, AbstractEntityWrapper> resultEntityMap = new HashMap<String, AbstractEntityWrapper>();
	private Map<String, ResourceWrapper> detailResourceMap = new HashMap<String, ResourceWrapper>();
	private List<AbstractEntityWrapper> entities = new ArrayList<AbstractEntityWrapper>();

	private long totalHits = 0;
	private double queryTime = 0;
	private String inputTextQuery = "";
	private AbstractSuggestionView selectedItem = null;
	private List<AbstractSuggestionView> selectedItems = null;
	private int pageSize = 10;
	private int page = 0;
	private List<String> langs = null;
	
	private volatile Set<Filter> filters = new HashSet<Filter>();
	private volatile Set<String> classes = new HashSet<String>();
	private volatile Set<String> prefixes = new HashSet<String>();

	private String indexDir = null;
	private volatile String[] labelingProperties = null;
	private volatile String[] imageProperties = null;
	private volatile String[] abstractProperties = null;
	private volatile String[] relevancePropertyOrder = null;
	private volatile String[] hidenProperties = null;
	private List<AbstractSuggestionView> suggestions = new ArrayList<AbstractSuggestionView>();
	private String selectedEntryId = "";
	private AbstractEntityWrapper selectedEntry = null;
	private List<AbstractEntityWrapper> openEntities = new ArrayList<AbstractEntityWrapper>();
	private Map<String, String> classMap = new HashMap<String, String>();
	{
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

	public void setSelectedQueryItem(AbstractSuggestionView selectedItem) {
		this.selectedItem = selectedItem;
		this.inputTextQuery = null;
		if(selectedItem != null) {
			String currentQuery = selectedItem.getQuery();
			this.inputTextQuery = currentQuery;
		}
	}
	
	public void setSelectedQueryItems(List<AbstractSuggestionView> selectedItems) {
		this.selectedItems = selectedItems;
		this.inputTextQuery = null;
		if(selectedItems != null) {
			for(AbstractSuggestionView suggestionItem : selectedItems) {
				this.inputTextQuery += suggestionItem.getQuery() + " ";
			}
		}
	}
	
	public List<AbstractSuggestionView> getSelectedQueryItems() {
		return selectedItems;
	}
	
	public void onItemSelect(SelectEvent<String> event) {
        FacesContext.getCurrentInstance().addMessage(null, 
        		new FacesMessage("Item Selected", 
        				event.getObject()));
    }
	
	public AbstractSuggestionView getSelectedQueryItem() {
		return selectedItem;
	}

	public void setInputTextQuery(String inputTextQuery) {
		this.inputTextQuery = inputTextQuery;
	}

	public List<AbstractEntityWrapper> getEntityLazyModel() {
		return entities;
	}

	public boolean isQueryEmpty() {
		if (inputTextQuery == null) {
			return true;
		}
		return inputTextQuery.isEmpty();
	}

	public void filterContent() {
		Map<String, String> params = FacesContext.getCurrentInstance().
				getExternalContext().
				getRequestParameterMap();
		String filter = params.get("content");
		classes.clear();
		if(filter != null) {
			String contentType = classMap.get(filter);
			classes.add(contentType);
		}
	}

	public AbstractEntityWrapper getAbout() throws IOException, 
	TemplateException, 
	URISyntaxException {
		SearchEngine searchEngine = new SearchEngine();
		EntitySnippetGeneartor snippetGenerator = new EntitySnippetGeneartor();
		Entity about = searchEngine.about();
		AbstractEntityWrapper entityWrapper = new IndexedEntityWrapper(about, 
				labelingProperties,
				imageProperties,
				abstractProperties,
				relevancePropertyOrder,
				hidenProperties,
				langs);
		About aboutWrapper = new About(about);
		entityWrapper.setSnippet(snippetGenerator.generate(inputTextQuery, aboutWrapper));
		return entityWrapper;
	}
	
	public List<AbstractSuggestionView> suggest(String query) {
		suggestions.clear();
		if(query.isEmpty()) {
			cleanQuery();
			return suggestions;
		}
		File index = new File(indexDir);
		try (FuzzyQuerySuggester suggester = new FuzzyQuerySuggester(index);) {
			ResultSet<Suggestion> rs = suggester.suggest(query,
											0,
											5,
											prefixes,
											classes,
											filters,
											true,
											false,
											false);
			ItemAutoCompleteSnippetGeneartor generator = new ItemAutoCompleteSnippetGeneartor();
			Integer id = 0;
			while(rs.hasNext()) {
				Suggestion suggestion = rs.next();
				String label = suggestion.getValue().toLowerCase();
				label = label.replace(">", "");
				if(!suggestion.match(query) && suggestions.size() == 0) {
					AbstractSuggestionView querySuggestion = createPatternQuerySuggestion(id, 
							query, 
							generator,
							rs);
					suggestions.add(querySuggestion);
					id++;
				}
				AbstractSuggestionView querySuggestion = createQuerySuggestion(id, 
						suggestion,
						label,
						generator,
						rs);
				suggestions.add(querySuggestion);
				id++;
			}
			if(rs.getSize() == 0) {
				String tokenizedQuery = query.toLowerCase();
				AbstractSuggestionView querySuggestion = createPatternQuerySuggestion(id, 
						tokenizedQuery,
						generator,
						rs);
				suggestions.add(querySuggestion);
			}
			this.suggestionList.setSuggestions(suggestions);
		} catch (Exception e) {
			logger.error("Error generating suggestions.", e);
		}
		return suggestions;
    }
	
	public AbstractSuggestionView createPatternQuerySuggestion(int id, 
			String query, 
			ItemAutoCompleteSnippetGeneartor generator, 
			ResultSet<Suggestion> rs) throws IOException, 
	InvalidTokenOffsetsException, 
	TemplateException {
		query = query.toLowerCase();
		QueryPatternSuggestionView suggestionItem = new QueryPatternSuggestionView(id,
				query,
				rs.highlight(query),
				query);
		String html = generator.generate(suggestionItem);
		suggestionItem.setHtml(html);
		return suggestionItem;
	}
	
	public AbstractSuggestionView createPatternQuerySuggestion(
			String query, 
			ItemAutoCompleteSnippetGeneartor generator) throws IOException, 
	InvalidTokenOffsetsException, 
	TemplateException {
		query = query.toLowerCase();
		QueryPatternSuggestionView suggestionItem = new QueryPatternSuggestionView(0,
				query,
				query,
				query);
		String html = generator.generate(suggestionItem);
		suggestionItem.setHtml(html);
		return suggestionItem;
	}
	
	public AbstractSuggestionView createQuerySuggestion(int id,
			Suggestion suggestion,
			String label,
			ItemAutoCompleteSnippetGeneartor generator,
			ResultSet<Suggestion> rs) throws IOException, 
	InvalidTokenOffsetsException, 
	TemplateException {
		GraphPatternSuggestionView suggestionItem = new GraphPatternSuggestionView(id, 
				suggestion,
				label,
				rs.highlight(label),
				label);
		String html = generator.generate(suggestionItem);
		suggestionItem.setHtml(html);
		return suggestionItem;
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
			Set<Filter> filters = filterViewController.getActiveFilters();
			filters.addAll(this.filters);
			// language
			List<String> displayLangs = sessionController.getLangs();
			try (IndexReader reader = SearchEngine.newReader(indexPath);) {
				SearchEngine searchEngine = new SearchEngine(reader);
				EntitySnippetGeneartor snippetGenerator = new EntitySnippetGeneartor();
				ResultSet<Entity> result = searchEngine.search(inputTextQuery, 
					offset,
					pageSize,
					prefixes,
					classes,
					filters);
				totalHits = result.getTotalHits();
				queryTime = result.getRuntime() / ((double)1000);
				result.accept(new EntityVisitor() {
					@Override
					public boolean visit(Entity entity, float score) throws Exception {
						AbstractEntityWrapper entityWrapper = new IndexedEntityWrapper(entity,
								labelingProperties,
								imageProperties,
								abstractProperties,
								relevancePropertyOrder,
								hidenProperties,
								displayLangs);
						entityWrapper.setSnippet(snippetGenerator.generate(inputTextQuery, 
								entityWrapper));
						entities.add(entityWrapper);
						resultEntityMap.put(entityWrapper.getId(), 
								entityWrapper);
						return true;
					}
				});
			}
		} catch (Exception e) {
			logger.error("Error loading content.", e);
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

	public void selectEntry() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		selectedEntryId = params.get("id");
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
		String openEntryId = params.get("id");
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
							hidenProperties,
							langs);
					loadResources(openEntry);
				} else {
					ResourceWrapper resource = detailResourceMap.get(openEntryId);
					String label = openEntryId;
					String uri = openEntryId;
					if(resource != null) {
						label = resource.getLabel();
						uri = resource.getURI();
					}
					openEntry = new UnavailableEntityWrapper(uri, 
							label);
				}
				resultEntityMap.put(openEntry.getId(), 
						openEntry);
			} catch (Exception e) {
				logger.error("Error opening entry: " + openEntryId, e);
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
		try {
			Map<String, String> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			inputTextQuery = requestParams.get("query");
			if(inputTextQuery != null) {
				ItemAutoCompleteSnippetGeneartor generator = new ItemAutoCompleteSnippetGeneartor();
				this.setSelectedQueryItem(createPatternQuerySuggestion(inputTextQuery, generator));
			}
		} catch (Exception e) {
			logger.error("Error while loading properties.", e);
		}
		if(params.prefixes != null) {
			for(String prefix : params.prefixes) {
				prefixes.add(prefix);
			}
		}
	}

	public void setSuggestionList(SuggestionList suggestionList) {
		this.suggestionList = suggestionList;
	}
	
	public void setFilterViewController (FilterViewController filterViewController) {
		this.filterViewController = filterViewController;
	}

	public void cleanQuery() {
		this.inputTextQuery = null;
		this.selectedItem = null;
		this.selectedItems = null;
	}

	public void resetPage() {
		this.page = 0;
	}

	public Long getTotalHits() {
		return totalHits;
	}
	
	public Double getQueryTime() {
		return queryTime;
	}
}
