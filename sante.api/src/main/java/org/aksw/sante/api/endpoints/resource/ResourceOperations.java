package org.aksw.sante.api.endpoints.resource;

import io.swagger.v3.oas.annotations.Operation;
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
	@Operation
	@GetMapping(
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseBody
	Object getResource(@RequestParam String uri);
}
