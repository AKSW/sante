package org.aksw.sante.smile.view;

import org.sante.lucene.Filter;
import org.sante.lucene.PatternFilter;

public class QueryPatternSuggestionView extends AbstractSuggestionView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Filter filter = null;

	public QueryPatternSuggestionView(Integer id,
			String query,
			String title,
			String subtitle) {
		super(id,
				query,
				title,
				subtitle);
		this.filter = new PatternFilter(query);
		setId(query);
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
}
