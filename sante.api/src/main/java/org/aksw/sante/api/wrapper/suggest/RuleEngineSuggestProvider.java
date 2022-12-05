package org.aksw.sante.api.wrapper.suggest;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.provider.SuggestProvider;
import org.aksw.sante.api.wrapper.ruleengine.QueryRuleEngine;
import org.aksw.sante.api.wrapper.ruleengine.rules.suggest.SuggestRule;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;

import java.util.List;

/**
 * Concrete SuggestProvider using a rule engine based approach for suggestion.
 */
public class RuleEngineSuggestProvider implements SuggestProvider {

	/**
	 * List of SuggestRules used with the rule engine.
	 */
	List<? extends SuggestRule> suggestRules;

	/**
	 * Constructs the RuleEngineSuggestProvider.
	 *
	 * @param suggestRules list of SuggestRules
	 */
	public RuleEngineSuggestProvider(List<? extends SuggestRule> suggestRules) {
		this.suggestRules = suggestRules;
	}

	/**
	 * Performs the suggestion using a given query.
	 *
	 * @param query the query that contains the suggestion parameters
	 * @return a ResultSet of suggestions
	 * @throws SearchSuggestException if any issue arises during search
	 */
	@Override
	public ResultSet<Suggestion> suggest(Query query) throws SearchSuggestException {
		return QueryRuleEngine.processQueryUsingQueryRules(query, this.suggestRules);
	}
}
