package org.aksw.sante.api.wrapper.provider;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;

/**
 * The interface that defines the methods to suggest with SANTé using a given query.
 */
public interface SuggestProvider {

	/**
	 * Performs a SANTé suggestion using the query.
	 *
	 * @param query the query that contains the suggestion parameters
	 * @return a set of entities that represent a suggestion result
	 */
	ResultSet<Suggestion> suggest(Query query) throws SearchSuggestException;
}
