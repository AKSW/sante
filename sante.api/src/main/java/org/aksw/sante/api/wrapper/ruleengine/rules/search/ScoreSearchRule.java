package org.aksw.sante.api.wrapper.ruleengine.rules.search;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;

/**
 * Rule that executes for the set optional query field "content" (score).
 */
public class ScoreSearchRule extends SearchRule {

	/**
	 * Constructs the rule.
	 *
	 * @param searchEngine a SearchEngine needed to execute the rule
	 */
	public ScoreSearchRule(SearchEngine searchEngine) {
		super(searchEngine);
	}

	/**
	 * Returns a simple array of strings containing "classes" to indicate
	 * that exactly these optional fields of the query must be set for the rule to apply.
	 *
	 * @return a simple array of strings
	 */
	@Override
	protected String[] getSpecifiedOptionalFields() {
		return new String[]{
				"content"
		};
	}

	/**
	 * Executes the query using the SearchEngine.
	 *
	 * @param query a query with parameters
	 * @return a ResultSet of possible entities
	 * @throws SearchSuggestException if any issue arises during search
	 */
	@Override
	public ResultSet<Entity> executeQuery(Query query) throws SearchSuggestException {
		try {
			return this.searchEngine.search(
					query.getQ(),
					query.getOffset(),
					query.getLimit(),
					query.getContent().contains("score")
			);
		} catch (Exception exception) {
			throw new SearchSuggestException(exception);
		}
	}
}
