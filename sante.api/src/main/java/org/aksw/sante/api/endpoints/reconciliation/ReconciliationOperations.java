package org.aksw.sante.api.endpoints.reconciliation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.aksw.sante.api.endpoints.reconciliation.data.query.ReconciliationQuery;
import org.aksw.sante.api.endpoints.reconciliation.data.result.ReconciliationResult;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Definition of the available reconciliation endpoints.
 */
@RequestMapping("${api-path-prefix}/reconcile")
public interface ReconciliationOperations {

	/**
	 * Call the reconciliation service with a batch of reconciliation queries.
	 *
	 * @param queryBatch a batch of reconciliation queries from the request body
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises
	 */
	@Operation(
			summary = "Retrieve entities in the format of W3C Reconciliation",
			description = "Reconciliation provides a way of matching similar entities."
					+ " SANTé is used in the background to facilitate the matching via a search query.",
			tags = {"reconciliation"},
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(
							examples = @ExampleObject("{\"q1\":{\"query\":\"string\",\"type\":[{\"id\":\"string\",\"name\":\"string\",\"broader\":[{\"id\":\"string\",\"name\":\"string\"}]}],\"limit\":1,\"properties\":[{\"pid\":\"string\",\"v\":\"string\"}]}}")
					)
			),
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The query was successful and returned a result.",
							content = @Content(
									examples = @ExampleObject("{\"q1\":{\"result\":[{\"id\":\"string\",\"name\":\"string\",\"score\":0,\"features\":[{\"id\":\"string\",\"value\":\"string\"}],\"match\":true}]}}"))
					),
					@ApiResponse(
							responseCode = "400",
							description = "The request contains invalid input.",
							content = @Content
					),
					@ApiResponse(
							responseCode = "500",
							description = "There was an issue while processing the request.",
							content = @Content
					)
			}
	)
	@PostMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseBody
	Map<String, ReconciliationResult> batchReconcilePost(
			@RequestBody Map<String, @Valid ReconciliationQuery> queryBatch
	) throws SearchSuggestException;

	/**
	 * Call the reconciliation service with a string that represents a batch of reconciliation queries.
	 *
	 * @param queries string representing a batch of reconciliation queries
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises
	 */
	@Operation(
			summary = "Retrieve entities in accordance W3C Reconciliation",
			description = "Reconciliation provides a way of matching similar entities."
					+ " ATTENTION: This GET endpoint is only supposed to be used for interactive debugging"
					+ " of reconciliation queries in a web browser.",
			tags = {"reconciliation"},
			parameters = {
					@Parameter(
							name = "queries",
							description = "Query batch",
							in = ParameterIn.QUERY,
							example = "{\"q1\":{\"query\":\"string\",\"type\":[{\"id\":\"string\",\"name\":\"string\",\"broader\":[{\"id\":\"string\",\"name\":\"string\"}]}],\"limit\":1,\"properties\":[{\"pid\":\"string\",\"v\":\"string\"}]}}"
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The reconciliation was successful and returned a result.",
							content = @Content(
									examples = @ExampleObject("{\"q1\":{\"result\":[{\"id\":\"string\",\"name\":\"string\",\"score\":0,\"features\":[{\"id\":\"string\",\"value\":\"string\"}],\"match\":true}]}}"))
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
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	Map<String, ReconciliationResult> batchReconcileGet(String queries) throws SearchSuggestException;
}
