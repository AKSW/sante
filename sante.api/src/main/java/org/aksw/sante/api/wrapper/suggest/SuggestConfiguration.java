package org.aksw.sante.api.wrapper.suggest;

import org.aksw.sante.api.wrapper.ruleengine.rules.suggest.PlainSuggestRule;
import org.aksw.sante.api.wrapper.ruleengine.rules.suggest.PrefixesClassesFiltersUrisScoreResultsSuggestRule;
import org.aksw.sante.api.wrapper.ruleengine.rules.suggest.SuggestRule;
import org.aksw.sante.api.wrapper.ruleengine.rules.suggest.PrefixesClassesUrisScoreResultsSuggestRule;
import org.sante.lucene.FuzzyQuerySuggester;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration of the suggestion.
 */
@Configuration
public class SuggestConfiguration {

	/**
	 * Path of the index that resides on the local file system.
	 */
	@Value("${index.path}")
	private String indexPath;

	/**
	 * Configure a FuzzyQuerySuggester for the IoC container.
	 *
	 * @return a properly configured FuzzyQuerySuggester
	 */
	@Bean
	public FuzzyQuerySuggester getFuzzyQuerySuggester() {
		try {
			return new FuzzyQuerySuggester(new File(this.indexPath));
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Returns a list of instantiated SuggestRules.
	 *
	 * @param fuzzyQuerySuggester the FuzzySearchSuggester that is used by these SuggestRules
	 * @return a list of instantiated SuggestRules
	 */
	public List<? extends SuggestRule> getInstantiatedSuggestRules(FuzzyQuerySuggester fuzzyQuerySuggester) {
		return Arrays.asList(
				new PlainSuggestRule(fuzzyQuerySuggester),
				new PrefixesClassesUrisScoreResultsSuggestRule(fuzzyQuerySuggester),
				new PrefixesClassesFiltersUrisScoreResultsSuggestRule(fuzzyQuerySuggester)
		);
	}
}
