package org.aksw.sante.api.wrapper.ruleengine.rules.search;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;

/**
 * Rule that executes if no optional query fields are set.
 */
public class PlainSearchRule extends SearchRule {

	/**
	 * Constructs the rule.
	 *
	 * @param searchEngine a SearchEngine needed to execute the rule
	 */
	public PlainSearchRule(SearchEngine searchEngine) {
		super(searchEngine);
	}

	/**
	 * Returns a simple empty array of strings to indicate
	 * that exactly no optional fields of the query must be set for the rule to apply.
	 *
	 * @return a simple empty array of strings
	 */
	@Override
	protected String[] getSpecifiedOptionalFields() {
		return new String[]{};
	}

	/**
	 * Executes the query using the SearchEngine
	 *
	 * @param query a query with parameters
	 * @return a ResultSet of possible entities
	 * @throws SearchSuggestException if any issue arises during search
	 */
	@Override
	public ResultSet<Entity> executeQuery(Query query) throws SearchSuggestException {
		try {
			return this.searchEngine.search(query.getQ(), query.getOffset(), query.getLimit());
		} catch (Exception exception) {
			throw new SearchSuggestException(exception);
		}
	}
}
