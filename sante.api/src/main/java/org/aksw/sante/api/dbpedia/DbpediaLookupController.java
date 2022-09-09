package org.aksw.sante.api.dbpedia;

import java.io.IOException;
import java.util.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;


// TODO Pascal/Upper-Camel-Case for query parameters
// TODO Add request validation
@RestController
public class DbpediaLookupController {
	private final DbpediaLookupService dbpediaLookupService;

	public DbpediaLookupController(DbpediaLookupService dbpediaLookupService) {
		this.dbpediaLookupService = dbpediaLookupService;
	}
	@Operation(
			summary = "Retrieve entities",
			tags = {"dbpedia-lookup"},
			responses = {
					@ApiResponse(responseCode = "400", description = "Invalid input")
			}
	)
	@GetMapping(path = "/API/dbpedia-lookup")
	@ResponseBody
	protected DbpediaDocumentCollection lookupDbpedia(
			@RequestParam(defaultValue = "10") Integer maxHits,
			@RequestParam(required = false) String searchQuery,
			@RequestParam(required = false) String searchClasses
	) throws IOException {
		HashSet<String> classes = searchClasses == null
				? new HashSet<>()
				: new HashSet<>(Arrays.asList(searchClasses.split(",")));

		return this.dbpediaLookupService.lookupDbpedia(maxHits, searchQuery, classes);
	}
}
