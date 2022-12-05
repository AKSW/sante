package org.aksw.sante.api.endpoints.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Definition of the available resource endpoints.
 */
@RequestMapping("${api-path-prefix}/resource")
public interface ResourceOperations {

	/**
	 * Performs the resource lookup.
	 *
	 * @param uri the uri to perform the lookup for
	 * @return a JSON-LD serialized object
	 */
	@Operation(
			summary = "Perform a resource lookup given a URI.",
			description = "Starting from a URI, SANTé can be used to lookup a resource "
					+ " and return a JSON-LD formatted result.",
			tags = {"resource"},
			parameters = {
					@Parameter(
							name = "uri",
							description = "URI",
							in = ParameterIn.QUERY,
							example = "https://example.org/bob"
					)
			},
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "The resource lookup was successful and returned a result.",
							content = @Content
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
	@ResponseBody
	Object getResource(@RequestParam String uri);
}
