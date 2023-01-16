package org.aksw.sante.api.endpoints.reconciliation;

import org.aksw.sante.api.wrapper.search.RuleEngineSearchProvider;
import org.aksw.sante.api.wrapper.search.SearchConfiguration;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Reconciliation endpoint.
 */
@Configuration
public class ReconciliationConfiguration {

	/**
	 * The configuration that contains the rules for the search rule engine.
	 */
	private final SearchConfiguration searchConfiguration;

	/**
	 * Constructs the ReconciliationConfiguration.
	 *
	 * @param searchConfiguration configuration of the search wrapper
	 */
	@Autowired
	public ReconciliationConfiguration(SearchConfiguration searchConfiguration) {
		this.searchConfiguration = searchConfiguration;
	}

	/**
	 * Provides a rule engine based search provider for the ReconciliationService.
	 *
	 * @param searchEngine search engine that is going to be used for a search
	 * @return a properly configured RuleEngineSearchProvider
	 */
	@Bean("reconciliation")
	@Autowired
	public RuleEngineSearchProvider getRuleEngineSanteSearchProvider(
			@Qualifier("without-label") SearchEngine searchEngine) {
		return new RuleEngineSearchProvider(
				this.searchConfiguration.getInstantiatedSearchRules(searchEngine)
		);
	}
}
