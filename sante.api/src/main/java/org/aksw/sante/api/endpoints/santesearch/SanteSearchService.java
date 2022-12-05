package org.aksw.sante.api.endpoints.santesearch;

import org.aksw.sante.api.endpoints.santesearch.data.SanteSearchCandidate;
import org.aksw.sante.api.endpoints.santesearch.data.SanteSearchResponseDto;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.provider.SearchProvider;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service that carries out the search.
 */
@Service
public class SanteSearchService {

	/**
	 * The provider that actually executes the search.
	 */
	private final SearchProvider searchProvider;

	/**
	 * Constructs the SanteSearchService.
	 *
	 * @param searchProvider provider that executes the search
	 */
	public SanteSearchService(@Qualifier("sante-search") SearchProvider searchProvider) {
		this.searchProvider = searchProvider;
	}

	/**
	 * Performs the search and construct the response.
	 *
	 * @param query search query
	 * @return an appropriate response to the query that contains all relevant result information
	 * @throws SearchSuggestException if any issue arises during search
	 */
	public SanteSearchResponseDto santeSearch(Query query) throws SearchSuggestException {
		ResultSet<Entity> searchResult = this.searchProvider.search(query);

		SanteSearchResponseDto santeSearchResponseDto = new SanteSearchResponseDto();
		santeSearchResponseDto.setParams(query);

		while (searchResult.hasNext()) {
			Entity entity = searchResult.next();
			SanteSearchCandidate santeSearchCandidate = new SanteSearchCandidate();

			santeSearchCandidate.setUri(entity.getURI());

			if (query.hasContent()) {
				if (query.getContent().contains("instance")) {
					santeSearchCandidate.setInstance(entity.asTriples());
				}

				try {
					if (query.getContent().contains("score")) {
						santeSearchCandidate.setScore(Float.toString(searchResult.score()));
					}
				} catch (IOException exception) {
					throw new RuntimeException(exception);
				}
			}

			santeSearchResponseDto.addCandidateToResult(santeSearchCandidate);
		}

		return santeSearchResponseDto;
	}
}
