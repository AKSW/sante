package org.aksw.sante.api.dbpedia;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

// TODO Add JavaDoc

/**
 * SpringBoot controller for the dbpedia-lookup endpoint.
 * @see RestController
 */
@RestController
@Validated
public class DbpediaLookupController {

	/**
	 * The service to perform searches analogously to DBpedia-Lookup.
	 */
	private final DbpediaLookupService dbpediaLookupService;

	/**
	 * Constructs the controller and injects all necessary dependencies.
	 *
	 * @param dbpediaLookupService the service to perform the search
	 * @see DbpediaLookupService
	 */
	public DbpediaLookupController(DbpediaLookupService dbpediaLookupService) {
		this.dbpediaLookupService = dbpediaLookupService;
	}

	/**
	 * Provides a method for GET calls to the dbpedia-lookup endpoint.
	 * Calls the corresponding service that performs the actual search.
	 *
	 * @param searchQuery   the actual search string. If empty, the search is not reduced to any given search string.
	 * @param maxHits       limits the amount of search results
	 * @param searchClasses comma-separated list of classes that are to be searched
	 * @return              a REST-response of the search results
	 * @throws Exception    if any issues arise
	 */
	@Operation(
			summary = "Retrieve entities in the format of DBpedia-Lookup",
			description = "DBpedia Lookup provides a way of searching"
					+ " through RDF data using natural language search queries."
					+ " A user can therefore search for resources without having to use or learn SPARQL."
					+ " This endpoint makes it possible to use SANTé in a similar fashion."
					+ " It returns a search result that is equivalent of that of DBpedia Lookup.",
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
			path = "/api/dbpedia-lookup",
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	protected DbpediaDocumentCollection lookupDbpedia(
			// TODO handle issues with maxHits being absent or becoming too large — what is this dependent on?
			@RequestParam(required = false) String searchQuery,
			@NotNull @Min(1) Integer maxHits,
			@RequestParam(required = false) String searchClasses
	) throws Exception {
		HashSet<String> classes = searchClasses == null
				? new HashSet<>()
				: new HashSet<>(Arrays.asList(searchClasses.split(",")));

		return this.dbpediaLookupService.lookupDbpedia(searchQuery, maxHits, classes);
	}
}
