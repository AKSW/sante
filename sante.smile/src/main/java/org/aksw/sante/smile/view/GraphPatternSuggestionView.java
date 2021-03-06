package org.aksw.sante.smile.view;

import org.aksw.sante.entity.Triple;
import org.sante.lucene.Filter;
import org.sante.lucene.Suggestion;
import org.sante.lucene.TriplePatternFilter;

public class GraphPatternSuggestionView extends AbstractSuggestionView {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Filter filter = null;

	public GraphPatternSuggestionView(Integer id,
			Suggestion suggestion,
			String query,
			String title,
			String subtitle) {
		super(id,
				query,
				title,
				subtitle);
		if(suggestion.getProperty() != null) {
			String propertyURI = suggestion.getProperty().getURI();
			String objectURI = suggestion.getProperty().getObject().getURI();
			this.filter = new TriplePatternFilter(null, 
					propertyURI, 
					objectURI);
			this.setId("p:" + propertyURI + "o:" + objectURI);
		} else if(suggestion.getEntity() != null) {
			String entityURI = suggestion.getEntity().getURI();
			this.filter = new TriplePatternFilter(entityURI, 
					null, 
					null);
			this.setId("s:" + entityURI);
		} else if (suggestion.getTriple() != null) {
			Triple triple = suggestion.getTriple();
			String subjectURI = triple.getSubject().getURI();
			String predicateURI = triple.getProperty().getURI();
			this.filter = new TriplePatternFilter(subjectURI, 
					predicateURI, 
					null);
			this.setId("s:" + subjectURI + "p:" + predicateURI);
		}
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
}
