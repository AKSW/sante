package org.aksw.sante.api.wrapper.ruleengine.rules.suggest;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;

/**
 * Rule that executes if no optional query fields are set.
 */
public class PlainSuggestRule extends SuggestRule {

	/**
	 * Constructs the rule.
	 *
	 * @param fuzzyQuerySuggester a FuzzyQuerySuggester needed to execute the rule
	 */
	public PlainSuggestRule(FuzzyQuerySuggester fuzzyQuerySuggester) {
		super(fuzzyQuerySuggester);
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
	 * Executes the query using the FuzzyQuerySuggester.
	 *
	 * @param query a query with parameters
	 * @return a ResultSet of possible suggestions
	 * @throws SearchSuggestException if any issue arises during suggestion
	 */
	@Override
	public ResultSet<Suggestion> executeQuery(Query query) throws SearchSuggestException {
		try {
			return this.fuzzyQuerySuggester.suggest(query.getQ(), query.getOffset(), query.getLimit());
		} catch (Exception exception) {
			throw new SearchSuggestException(exception);
		}
	}
}
