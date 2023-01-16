package org.aksw.sante.api.wrapper.ruleengine.rules.suggest;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;

/**
 * Rule that executes for set optional query fields "prefixes", "classes", "filter", "uris", "content" (score)
 * and "content" (emptyResults).
 */
public class PrefixesClassesFiltersUrisScoreResultsSuggestRule extends SuggestRule {

	/**
	 * Constructs the rule.
	 *
	 * @param fuzzyQuerySuggester a FuzzyQuerySuggester needed to execute the rule
	 */
	public PrefixesClassesFiltersUrisScoreResultsSuggestRule(FuzzyQuerySuggester fuzzyQuerySuggester) {
		super(fuzzyQuerySuggester);
	}

	/**
	 * Returns a simple array of strings containing "prefixes", "fields" and "filters" to indicate
	 * that exactly these optional fields of the query must be set for the rule to apply.
	 *
	 * @return a simple array of strings
	 */
	@Override
	protected String[] getSpecifiedOptionalFields() {
		return new String[]{
				"prefixes",
				"classes",
				"filters",
				"content"
		};
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
			return this.fuzzyQuerySuggester.suggest(
					query.getQ(),
					query.getOffset(),
					query.getLimit(),
					query.getPrefixes(),
					query.getClasses(),
					query.getFilters(),
					query.getContent().contains("uris"),
					query.getContent().contains("score"),
					query.getContent().contains("emptyResults")
			);
		} catch (Exception exception) {
			throw new SearchSuggestException(exception);
		}
	}
}
