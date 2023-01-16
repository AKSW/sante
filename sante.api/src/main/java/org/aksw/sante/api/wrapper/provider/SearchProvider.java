package org.aksw.sante.api.wrapper.provider;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;

/**
 * The interface that defines the methods to search with SANTé using a given query.
 */
public interface SearchProvider {

	/**
	 * Performs a SANTé search using a query.
	 *
	 * @param query the query that contains the search parameters
	 * @return a set of entities that represent the search result
	 */
	ResultSet<Entity> search(Query query) throws SearchSuggestException;
}
