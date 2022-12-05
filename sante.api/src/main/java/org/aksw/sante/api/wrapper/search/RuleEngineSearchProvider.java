package org.aksw.sante.api.wrapper.search;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.ruleengine.QueryRuleEngine;
import org.aksw.sante.api.wrapper.provider.SearchProvider;
import org.aksw.sante.api.wrapper.ruleengine.rules.search.SearchRule;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;

import java.util.List;

/**
 * Concrete SearchProvider using a rule engine based approach to search.
 */
public class RuleEngineSearchProvider implements SearchProvider {

	/**
	 * List of SearchRules used with the rule engine.
	 */
	private final List<? extends SearchRule> searchRules;

	/**
	 * Constructs the RuleEngineSearchProvider.
	 *
	 * @param searchRules list of SearchRules
	 */
	public RuleEngineSearchProvider(List<? extends SearchRule> searchRules) {
		this.searchRules = searchRules;
	}

	/**
	 * Performs the search using a given query.
	 *
	 * @param query the query that contains the search parameters
	 * @return a ResultSet of entities
	 * @throws SearchSuggestException if any issue arises during search
	 */
	@Override
	public ResultSet<Entity> search(Query query) throws SearchSuggestException {
		return QueryRuleEngine.processQueryUsingQueryRules(query, this.searchRules);
	}
}
