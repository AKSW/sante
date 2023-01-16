package org.aksw.sante.api.endpoints.santesearch;

import org.aksw.sante.api.wrapper.search.SearchConfiguration;
import org.aksw.sante.api.wrapper.search.RuleEngineSearchProvider;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the SanteSearch endpoint.
 */
@Configuration
public class SanteSearchConfiguration {

	/**
	 * The configuration that contains the rules for the search rule engine.
	 */
	private final SearchConfiguration searchConfiguration;

	/**
	 * Constructs the SanteSearchConfiguration.
	 *
	 * @param searchConfiguration configuration of the search wrapper
	 */
	@Autowired
	public SanteSearchConfiguration(SearchConfiguration searchConfiguration) {
		this.searchConfiguration = searchConfiguration;
	}

	/**
	 * Provides a rule engine based search provider for the SanteSearchService.
	 *
	 * @param searchEngine search engine that is going to be used for a search
	 * @return a properly configured RuleEngineSearchProvider
	 */
	@Bean("sante-search")
	@Autowired
	public RuleEngineSearchProvider getRuleEngineSanteSearchProvider(
			@Qualifier("without-label") SearchEngine searchEngine) {
		return new RuleEngineSearchProvider(
				this.searchConfiguration.getInstantiatedSearchRules(searchEngine)
		);
	}
}
