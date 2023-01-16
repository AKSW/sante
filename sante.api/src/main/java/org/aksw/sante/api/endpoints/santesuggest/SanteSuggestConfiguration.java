package org.aksw.sante.api.endpoints.santesuggest;

import org.aksw.sante.api.wrapper.suggest.RuleEngineSuggestProvider;
import org.aksw.sante.api.wrapper.suggest.SuggestConfiguration;
import org.sante.lucene.FuzzyQuerySuggester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the SanteSuggest endpoint.
 */
@Configuration
public class SanteSuggestConfiguration {

	/**
	 * The configuration that contains the rules for the suggestion rule engine.
	 */
	private final SuggestConfiguration suggestConfiguration;

	/**
	 * Constructs the SanteSuggestConfiguration.
	 *
	 * @param suggestConfiguration configuration of the suggest wrapper
	 */
	@Autowired
	public SanteSuggestConfiguration(SuggestConfiguration suggestConfiguration) {
		this.suggestConfiguration = suggestConfiguration;
	}

	/**
	 * Provides a rule engine based suggestion provider for the SanteSuggestService.
	 *
	 * @param fuzzyQuerySuggester suggester that is going to be used for the suggestions
	 * @return a properly configured RuleEngineSuggestProvider
	 */
	@Bean("sante-suggest")
	@Autowired
	public RuleEngineSuggestProvider getRuleEngineSuggestProvider(FuzzyQuerySuggester fuzzyQuerySuggester) {
		return new RuleEngineSuggestProvider(
				this.suggestConfiguration.getInstantiatedSuggestRules(fuzzyQuerySuggester)
		);
	}
}
