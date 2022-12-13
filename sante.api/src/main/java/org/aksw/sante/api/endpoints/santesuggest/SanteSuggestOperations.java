package org.aksw.sante.api.endpoints.santesuggest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.aksw.sante.api.endpoints.santesuggest.dto.SanteSuggestResponseDto;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Definition of the available SANTé suggestion endpoints.
 */
@RequestMapping("${api-path-prefix}/sante-suggest")
public interface SanteSuggestOperations {

	/**
	 * Defines the method that should handle the suggestion given a query.
	 *
	 * @param query query that contains all parameters for the suggestion
	 * @return a SanteSuggestResponseDto containing all relevant result information
	 * @throws SearchSuggestException if any issue arises during suggestion
	 */
	@Operation(
			summary = "Suggest entities using SANTé",
			description = "SANTé suggest provides a way to get a suggestion."
					+ " It returns a list of possible suggestion results.",
			tags = {"sante-suggest"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							examples = @ExampleObject("{\"q\":\"string\",\"offset\":0,\"limit\":10,\"prefixes\":[\"string\"],\"classes\":[\"string\"],\"filters\":[{\"type\":\"patternFilter\",\"pattern\":\"string\"},{\"type\":\"triplePatternFilter\",\"subjectUri\":\"string\",\"predicateUri\":\"string\",\"objectUri\":\"string\"},{\"type\":\"uriFilter\",\"uri\":\"string\"}],\"content\":[\"string\"]}")
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The query was successful and returned a result."
					),
					@ApiResponse(
							responseCode = "400",
							description = "The request contains invalid input.",
							content = @Content
					),
					@ApiResponse(
							responseCode = "500",
							description = "There was an issue while processing the query.",
							content = @Content
					)
			}
	)
	@PostMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseBody
	SanteSuggestResponseDto santeSuggest(@RequestBody @Valid Query query) throws SearchSuggestException;
}
