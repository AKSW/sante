package org.aksw.sante.smile.view;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named(value="suggestionList")
@SessionScoped
public class SuggestionList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -556465236871580802L;
	List<AbstractSuggestionView> suggestions = null;
	
	public SuggestionList() {
	}
	
	public AbstractSuggestionView getSuggestion(String id) {
		return suggestions.get(new Integer(id));
	}
	
	public void setSuggestions(List<AbstractSuggestionView> suggestions) {
		this.suggestions = suggestions;
	}

}
