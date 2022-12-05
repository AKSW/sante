package org.aksw.sante.api.endpoints.dbpedia;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.aksw.sante.api.endpoints.dbpedia.data.DbpediaDocumentCollection;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.aksw.sante.api.exception.SearchSuggestException;

import java.util.Optional;

@RequestMapping("${api-path-prefix}/dbpedia-lookup")
public interface DbpediaLookupOperations {

	/**
	 * Provides a method for GET calls to the dbpedia-lookup endpoint.
	 * Calls the corresponding service that performs the actual search.
	 *
	 * @param searchQuery   the actual search string. If empty, the search is not reduced to any given search string.
	 * @param maxHits       limits the amount of search results
	 * @param searchClasses comma-separated list of classes that are to be searched
	 * @return a REST-response of the search results
	 * @throws SearchSuggestException if any issues arises during search
	 */
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	@Operation(
			summary = "Retrieve entities in the format of DBpedia-Lookup",
			description = "DBpedia Lookup provides a way of searching"
					+ " through RDF data using natural language search queries."
					+ " A user can therefore search for resources without having to use or learn SPARQL."
					+ " This endpoint provides a way to use SANTé in accordance with DBpedia Lookup.",
			tags = {"dbpedia-lookup"},
			parameters = {
					@Parameter(
							name = "maxHits",
							description = "Limit the amount of results",
							in = ParameterIn.QUERY
					),
					@Parameter(
							name = "searchQuery",
							description = "The natural language search query."
									+ " Can be left empty, which will return all results.",
							in = ParameterIn.QUERY
					),
					@Parameter(
							name = "searchClasses",
							description = "Comma separated list of classes which should be taken into consideration."
									+ " Can be left empty, which will consider all classes",
							in = ParameterIn.QUERY
					)
			},
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
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	DbpediaDocumentCollection lookupDbpedia(
			Integer maxHits,
			Optional<String> searchQuery,
			Optional<String> searchClasses
	) throws SearchSuggestException;
}
