package org.aksw.sante.api.endpoints.santesuggest;

import org.aksw.sante.api.endpoints.santesuggest.data.SanteSuggestResponseDto;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for the SANTé suggest endpoint.
 */
@RestController
public class SanteSuggestController implements SanteSuggestOperations {

	/**
	 * Service that is going to perform the suggestion.
	 */
	private final SanteSuggestService santeSuggestService;

	/**
	 * Constructs the SanteSuggestController.
	 *
	 * @param santeSuggestService service that is going to perform the suggestion
	 */
	@Autowired
	public SanteSuggestController(SanteSuggestService santeSuggestService) {
		this.santeSuggestService = santeSuggestService;
	}

	/**
	 * Calls the service to perform the suggestion from a given query
	 *
	 * @param query the query that contains the suggestion parameters
	 * @return a SanteSuggestResponseDto that contains all relevant result data
	 * @throws SearchSuggestException if any issue arises during suggestion
	 */
	@Override
	public SanteSuggestResponseDto santeSuggest(Query query) throws SearchSuggestException {
		return this.santeSuggestService.santeSuggest(query);
	}
}
