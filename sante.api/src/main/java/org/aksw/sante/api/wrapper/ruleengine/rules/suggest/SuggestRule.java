package org.aksw.sante.api.wrapper.ruleengine.rules.suggest;

import org.aksw.sante.api.wrapper.ruleengine.rules.AbstractQueryRule;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.Suggestion;

// suggestion logic currently implemented in SANTé

// public org.sante.lucene.ResultSet<org.sante.lucene.Suggestion> suggest(java.lang.String query, int offset, int limit) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit -> SuggestPlainRule

// public org.sante.lucene.ResultSet<org.sante.lucene.Suggestion> suggest(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes, boolean uris, boolean score, boolean showResultsOnEmptyQuery) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes, uris(content), score(content), showResultsOnEmptyQuery(content) -> SuggestPrefixesClassesUrisScoreResultsRule

// public org.sante.lucene.ResultSet<org.sante.lucene.Suggestion> suggest(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes, java.util.Set<org.sante.lucene.Filter> filters, boolean suggestUris, boolean score, boolean showResultsOnEmptyQuery) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes, filters, uris(content), score(content), showResultsOnEmptyQuery(content) -> SuggestPrefixedClassesFiltersUrisScoreResultsRule

/**
 * A base rule for suggestions.
 */
public abstract class SuggestRule extends AbstractQueryRule<Suggestion> {

	/**
	 * The FuzzyQuerySuggester that is going to be used to execute the rule.
	 */
	protected FuzzyQuerySuggester fuzzyQuerySuggester;

	/**
	 * Constructs the SuggestRule.
	 *
	 * @param fuzzyQuerySuggester the FuzzyQuerySuggester
	 */
	public SuggestRule(FuzzyQuerySuggester fuzzyQuerySuggester) {
		this.fuzzyQuerySuggester = fuzzyQuerySuggester;
	}
}
