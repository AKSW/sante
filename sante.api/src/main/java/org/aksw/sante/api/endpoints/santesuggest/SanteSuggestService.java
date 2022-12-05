package org.aksw.sante.api.endpoints.santesuggest;

import org.aksw.sante.api.endpoints.santesuggest.data.SanteSuggestCandidate;
import org.aksw.sante.api.endpoints.santesuggest.data.SanteSuggestResponseDto;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.provider.SuggestProvider;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service that carries out the suggestion.
 */
@Service
public class SanteSuggestService {

	/**
	 * The provider that actually executes the suggestion.
	 */
	private final SuggestProvider suggestProvider;

	/**
	 * Constructs the SanteSuggestService.
	 *
	 * @param suggestProvider provider that executes the suggestion
	 */
	public SanteSuggestService(@Qualifier("sante-suggest") SuggestProvider suggestProvider) {
		this.suggestProvider = suggestProvider;
	}

	/**
	 * Performs the suggestion and constructs the response.
	 *
	 * @param query suggestion query
	 * @return a SanteSuggestResponseDto that contains all relevant result information
	 * @throws SearchSuggestException if any issue arises during suggestion
	 */
	public SanteSuggestResponseDto santeSuggest(Query query) throws SearchSuggestException {
		ResultSet<Suggestion> suggestResult = this.suggestProvider.suggest(query);

		SanteSuggestResponseDto santeSuggestResponseDto = new SanteSuggestResponseDto();
		santeSuggestResponseDto.setParams(query);

		while (suggestResult.hasNext()) {
			Suggestion suggestion = suggestResult.next();
			SanteSuggestCandidate santeSuggestCandidate = new SanteSuggestCandidate();

			santeSuggestCandidate.setSuggestion(suggestion.getValue());

			try {
				if (query.hasContent()) {
					if (query.getContent().contains("highlight")) {
						// simply override to simplify code
						santeSuggestCandidate.setSuggestion(suggestResult.highlight(suggestion.getValue()));
					}

					if (query.getContent().contains("score")) {
						santeSuggestCandidate.setScore(Float.toString(suggestResult.score()));
					}
				}
			} catch (IOException | InvalidTokenOffsetsException exception) {
				throw new RuntimeException(exception);
			}

			santeSuggestResponseDto.addCandidateToResult(santeSuggestCandidate);
		}

		return santeSuggestResponseDto;
	}
}
