package org.aksw.sante.api.wrapper.ruleengine;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.ruleengine.rules.QueryRule;
import org.sante.lucene.ResultSet;

import java.io.Serializable;
import java.util.List;

// inspired by https://www.baeldung.com/java-replace-if-statements
// and https://blog.devgenius.io/open-closed-principle-and-rule-engine-design-pattern-904c784501e5

/**
 * A rule engine that processes rules.
 * This pattern is used to abstract the possible actions of SANTé search or suggest logic.
 * If new methods are added, a corresponding new rule can be simply added to accommodate it.
 */
public abstract class QueryRuleEngine {

	/**
	 * Processes all rules and executes the first matching rule given a single query.
	 *
	 * @param query      query that a matching rule is to be processed for
	 * @param queryRules list of rules that should be tried
	 * @param <T>        type that represents a single result of an executed rule
	 * @return a ResultSet of possible results
	 * @throws SearchSuggestException if any issue arises during search or suggest
	 */
	public static <T extends Serializable> ResultSet<T> processQueryUsingQueryRules(Query query, List<? extends QueryRule<T>> queryRules) throws SearchSuggestException {
		QueryRule<T> queryRule = queryRules
				.stream()
				.filter(rule -> rule.queryRuleApplies(query))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Query rule engine can not match any rule "
						+ "so no search or suggestion is performed"
				));
		return queryRule.executeQuery(query);
	}
}
