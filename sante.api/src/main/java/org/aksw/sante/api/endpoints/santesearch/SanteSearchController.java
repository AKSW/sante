package org.aksw.sante.api.endpoints.santesearch;

import org.aksw.sante.api.endpoints.santesearch.data.SanteSearchResponseDto;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for the SANTé search endpoint.
 */
@RestController
public class SanteSearchController implements SanteSearchOperations {

	/**
	 * Service that is going to perform the search.
	 */
	private final SanteSearchService santeSearchService;

	/**
	 * Constructs the SanteSearchController.
	 *
	 * @param santeSearchService service that is going to perform the search
	 */
	@Autowired
	public SanteSearchController(SanteSearchService santeSearchService) {
		this.santeSearchService = santeSearchService;
	}

	/**
	 * Calls the service to perform the search from a given query.
	 *
	 * @param query the query that contains the search parameters
	 * @return a SanteSearchResponseDto that contains the relevant result data
	 * @throws SearchSuggestException if any issue arises during search
	 */
	@Override
	public SanteSearchResponseDto santeSearch(@Valid Query query) throws SearchSuggestException {
		return this.santeSearchService.santeSearch(query);
	}
}
