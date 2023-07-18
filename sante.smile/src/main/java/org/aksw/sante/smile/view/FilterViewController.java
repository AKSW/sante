package org.aksw.sante.smile.view;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.aksw.sante.smile.core.ItemAutoCompleteSnippetGeneartor;
import org.aksw.sante.smile.core.SmileParams;
import org.sante.lucene.Filter;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Named
@ViewScoped
public class FilterViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173739028408143306L;

	@Autowired
    public SmileParams smileParams;
	
	private String inputTextQuery = "";

	@Value("${sante.index.path:#{null}}")
	private String indexPath = null;
	private Map<String, AbstractSuggestionView> selectedTags = new HashMap<String, AbstractSuggestionView>();
	private String contentFilter = null;
	private Map<String, AbstractSuggestionView> suggestedTagMap = new HashMap<String, AbstractSuggestionView>();
	private List<AbstractSuggestionView> suggestedTags = new ArrayList<AbstractSuggestionView>();
	private Map<String, String> classMap = new HashMap<String, String>();
	{
		classMap.put("class", "CLASS");
		classMap.put("entity", "ENTITY");
		classMap.put("ontology", "ONTOLOGY");
		classMap.put("property", "PROPERTY");
	}

	public FilterViewController() {
	}

	public String getInputTextQuery() {
		return inputTextQuery;
	}
	
	public void setInputTextQuery(String inputTextQuery) {
		this.inputTextQuery = inputTextQuery;
	}

	public Collection<AbstractSuggestionView> getResources() {
		suggestedTagMap.clear();
		suggestedTags.clear();
		File index = new File(indexPath);
		try (FuzzyQuerySuggester suggester = new FuzzyQuerySuggester(index);) {
			Set<String> classFilters = new HashSet<String>();
			if (contentFilter != null) {
				classFilters.add(contentFilter);
			}
			ResultSet<Suggestion> rs = suggester.suggest(inputTextQuery,
															0,
															10,
															null,
															classFilters,
															null,
															false,
															false,
															true);
			ItemAutoCompleteSnippetGeneartor generator = new ItemAutoCompleteSnippetGeneartor();
			int id = 0;
			while(rs.hasNext()) {
				Suggestion suggestion = rs.next();
				if(!inputTextQuery.isEmpty() && 
						!suggestion.match(inputTextQuery) && id == 0) {
					QueryPatternSuggestionView suggestionItem = new QueryPatternSuggestionView(id,
							inputTextQuery,
							rs.highlight(inputTextQuery),
							"*query pattern*");
					String html = generator.generate(suggestionItem);
					suggestionItem.setHtml(html);
					suggestedTagMap.put(suggestionItem.getId(), suggestionItem);
					suggestedTags.add(suggestionItem);
					id++;
				}
				String label = suggestion.getValue();
				GraphPatternSuggestionView suggestionItem = new GraphPatternSuggestionView(id,
						suggestion,
						label,
						rs.highlight(label),
						suggestion.getComponent());
				String html = generator.generate(suggestionItem);
				suggestionItem.setHtml(html);
				suggestedTagMap.put(suggestionItem.getId(), suggestionItem);
				suggestedTags.add(suggestionItem);
				id++;
			}
			if(rs.getSize() == 0) {
				QueryPatternSuggestionView suggestionItem = new QueryPatternSuggestionView(id,
						inputTextQuery,
						rs.highlight(inputTextQuery),
						"*query pattern*");
				String html = generator.generate(suggestionItem);
				suggestionItem.setHtml(html);
				suggestedTagMap.put(suggestionItem.getId(), suggestionItem);
				suggestedTags.add(suggestionItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suggestedTags;
    }
	
	public Set<Filter> getActiveFilters() {
		Set<Filter> filters = new HashSet<Filter>();
		for(AbstractSuggestionView suggestionItem : selectedTags.values()) {
			Filter filter = suggestionItem.getFilter();
			filters.add(filter);
		}
		return filters;
	}

	public Collection<AbstractSuggestionView> getSelectedTags() {
		return selectedTags.values();
	}

	public void selectTag() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String tag = params.get("id");
		AbstractSuggestionView tagItem = suggestedTagMap.get(tag);
		if(tagItem != null) {
			selectedTags.put(tagItem.getId(), tagItem);
		}
	}

	public void unselectTag() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String tag = params.get("id");
		selectedTags.remove(tag);
	}

	public void filterTags() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String filter = params.get("content");
		this.contentFilter = classMap.get(filter);
	}
}
