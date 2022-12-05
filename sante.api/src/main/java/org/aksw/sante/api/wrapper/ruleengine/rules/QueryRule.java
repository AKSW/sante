package org.aksw.sante.api.wrapper.ruleengine.rules;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.sante.lucene.ResultSet;

import java.io.Serializable;

/**
 * Interface that defines the behaviour of a QueryRule.
 *
 * @param <T> type that represents a single result of an executed query rule
 */
public interface QueryRule<T extends Serializable> {

	/**
	 * Checks if the rule applies to the query.
	 *
	 * @param query a query with parameters
	 * @return a boolean that indicates if a rule applies or not
	 */
	boolean queryRuleApplies(Query query);

	/**
	 * Executes the rule using a given query.
	 *
	 * @param query a query with parameters
	 * @return a ResultSet of single results
	 * @throws SearchSuggestException if any issue arises
	 */
	ResultSet<T> executeQuery(Query query) throws SearchSuggestException;
}
