package org.aksw.sante.api.dbpedia;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.aksw.sante.entity.URIObject;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.springframework.web.bind.annotation.*;


// TODO Pascal/Upper-Camel-Case for query parameters
// TODO Add request validation
@RestController
public class DbpediaLookupController {
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
			@RequestParam(required = false) String queryString,
			@RequestParam(required = false) String queryClasses
	) {
		HashSet<String> classes = queryClasses == null
				? new HashSet<>()
				: new HashSet<>(Arrays.asList(queryClasses.split(",")));

		Path indexPath = (new File(SmileParams.getInstance().indexPath)).toPath();
		var docs = new DbpediaDocumentCollection();
		try (IndexReader reader = SearchEngine.newReader(indexPath)) {
			SearchEngine searchEngine = new SearchEngine(reader);
			ResultSet<Entity> results = searchEngine.search(
					queryString,
					0,
					maxHits,
					null,
					classes,
					null,
					true
			);
			while (results.hasNext()) {
				var doc = new DbpediaDocument();

				Entity entity = results.next();

				var resource = entity.getURI();
				doc.setResource(Collections.singletonList(resource));

				var score = Float.toString(results.score());
				doc.setScore(Collections.singletonList(score));

				List<String> label = entity.getLabels().stream().map(Literal::getValue).collect(Collectors.toList());
				doc.setLabel(label);

				List<Property> typeProperties = entity.getProperties("http://www.w3.org/2000/01/rdf-schema#type");
				if (typeProperties != null) {
					List<String> type = typeProperties.stream()
							.map(typeProperty -> typeProperty.getObject().getURI())
							.collect(Collectors.toList());
					doc.setType(type);
					List<String> typeName = typeProperties.stream()
							.flatMap(typeProperty -> ((URIObject) typeProperty.getObject()).getLabels()
									.stream().map(Literal::getValue))
							.collect(Collectors.toList());
					doc.setTypeName(typeName);
				}

				List<Property> commentProperties = entity.getProperties("http://www.w3.org/1999/02/22-rdf-syntax-ns#comment");
				if (commentProperties != null) {
					List<String> comment = commentProperties.stream()
							.map(commentProperty -> ((LiteralObject) commentProperty.getObject()).getValue())
							.collect(Collectors.toList());
					doc.setComment(comment);
				}

				docs.addToDocs(doc);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return docs;
	}
}
