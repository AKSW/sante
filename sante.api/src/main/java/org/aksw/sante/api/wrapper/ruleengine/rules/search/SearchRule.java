package org.aksw.sante.api.wrapper.ruleengine.rules.search;

import org.aksw.sante.api.wrapper.ruleengine.rules.AbstractQueryRule;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.SearchEngine;

// search logic currently implemented in SANTé

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit -> PlainRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, boolean score) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, score(contents) -> ScoreRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> classes) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, classes -> ClassesRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes -> PrefixesClassesRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes, java.util.Set<org.sante.lucene.Filter> filters) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes, (filters) -> PrefixesClassesFiltersRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes, java.util.Set<org.sante.lucene.Filter> filters, boolean score) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes, (filters), score(contents) -> PrefixesClassesFiltersScoreRule

// public org.sante.lucene.ResultSet<org.aksw.sante.entity.Entity> search(java.lang.String query, int offset, int limit, java.util.Set<java.lang.String> prefixes, java.util.Set<java.lang.String> classes, java.util.Set<org.sante.lucene.Filter> filters, boolean score, org.apache.lucene.search.Sort sort) throws java.lang.Exception { /* compiled code */ }
// query, offset, limit, prefixes, classes, (filters), sort -> PrefixesClassesFiltersSortRule

/**
 * A base rule for search.
 */
public abstract class SearchRule extends AbstractQueryRule<Entity> {

	/**
	 * The SearchEngine that is going to be used to execute the rule.
	 */
	protected final SearchEngine searchEngine;

	/**
	 * Constructs the SearchRule.
	 *
	 * @param searchEngine the SearchEngine
	 */
	public SearchRule(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}
}
