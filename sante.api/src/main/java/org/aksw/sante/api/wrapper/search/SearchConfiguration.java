package org.aksw.sante.api.wrapper.search;

import org.aksw.sante.api.wrapper.ruleengine.rules.search.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Sort;
import org.sante.lucene.SearchEngine;
import org.sante.lucene.SortFieldRelecanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Configuration of the search.
 */
@Configuration
public class SearchConfiguration {

	/**
	 * RDF label URI.
	 */
	private static final String RDF_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";

	/**
	 * Path of the index that resides on the local file system.
	 */
	@Value("${index.path}")
	String indexPath;

	/**
	 * Configure an IndexReader for the IoC container.
	 *
	 * @return the configured IndexReader
	 */
	@Bean
	public IndexReader getReader() {
		try {
			Path indexPath = (new File(this.indexPath)).toPath();
			return SearchEngine.newReader(indexPath);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Configure a SearchEngine (without label) for the IoC container.
	 *
	 * @return a properly configured SearchEngine
	 * @see SearchEngine
	 */
	@Bean("without-label")
	@Autowired
	public SearchEngine getSearchEngine(IndexReader reader) {
		try {
			return new SearchEngine(reader);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Configure a SearchEngine (with label) for the IoC container.
	 *
	 * @return a properly configured SearchEngine
	 * @see SearchEngine
	 */
	@Bean("with-label")
	@Autowired
	public SearchEngine getSearchEngineWithLabel(IndexReader reader) throws IOException {
		var labels = new HashSet<String>();
		labels.add(RDF_LABEL);
		return new SearchEngine(reader, labels);
	}

	/**
	 * Configure a Sort for the IoC container.
	 *
	 * @param reader an IndexReader to configure the Sort
	 * @return a properly configured Sort
	 */
	@Bean
	@Autowired
	public Sort getSort(IndexReader reader) {
		SortFieldRelecanceFactory sortFieldRelevanceFactory = new SortFieldRelecanceFactory();
		return sortFieldRelevanceFactory.create(reader);
	}

	/**
	 * Returns a list of instantiated SearchRules.
	 *
	 * @param searchEngine the SearchEngine that is used by these SearchRules
	 * @return a list of instantiated SearchRules
	 */
	public List<? extends SearchRule> getInstantiatedSearchRules(SearchEngine searchEngine) {
		return Arrays.asList(
				new PlainSearchRule(searchEngine),
				new ScoreSearchRule(searchEngine),
				new ClassesSearchRule(searchEngine),
				new PrefixesClassesSearchRule(searchEngine),
				new PrefixesClassesFiltersSearchRule(searchEngine),
				new PrefixesClassesFiltersScoreSearchRule(searchEngine),
				new PrefixesClassesFiltersScoreSortSearchRule(searchEngine, this.getSort(this.getReader()))
		);
	}
}
