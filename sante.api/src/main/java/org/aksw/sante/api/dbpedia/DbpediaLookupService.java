package org.aksw.sante.api.dbpedia;

import org.aksw.sante.entity.*;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DbpediaLookupService {

	private final SearchEngine searchEngine;
	private final String RDF_SCHEMA_TYPE = "http://www.w3.org/2000/01/rdf-schema#type";
	private final String RDF_COMMENT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#comment";

	public DbpediaLookupService(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public DbpediaDocumentCollection lookupDbpedia(
			String searchQuery,
			Integer maxHits,
			Set<String> searchClasses
	) throws Exception {
		var docs = new DbpediaDocumentCollection();

		// TODO call the correct search method — maybe one that can be called without a limit?
		//  or even better: create a search-query builder instead of method overloading
		ResultSet<Entity> results = this.searchEngine
				.search(searchQuery, 0, maxHits, null, searchClasses, null, true);

		while (results.hasNext()) {
			Entity entity = results.next();
			List<Property> typeProperties = entity.getProperties(this.RDF_SCHEMA_TYPE);
			List<Property> commentProperties = entity.getProperties(this.RDF_COMMENT);

			DbpediaDocument.DbpediaDocumentBuilder doc = DbpediaDocument.DbpediaDocumentBuilder.newDbpediaDocument()
					.addResource(Collections.singletonList(entity.getURI()))
					.addScore(Collections.singletonList(Float.toString(results.score())))
					.addLabel(entity.getLabels().stream().map(Literal::getValue).collect(Collectors.toList()))
					.addType(this.getTypeFromTypeProperties(typeProperties))
					.addTypeName(this.getTypeNameFromTypeProperties(typeProperties))
					.addComment(this.getCommentFromCommentProperties(commentProperties));

			docs.addDocument(doc.create());
		}
		return docs;
	}

	private List<String> getTypeFromTypeProperties(List<Property> typeProperties) {
		if (typeProperties == null) {
			return null;
		}
		return typeProperties.stream()
				.map(typeProperty -> typeProperty.getObject().getURI())
				.collect(Collectors.toList());
	}

	private List<String> getTypeNameFromTypeProperties(List<Property> typeProperties) {
		if (typeProperties == null) {
			return null;
		}
		return typeProperties.stream()
				.flatMap(typeProperty -> ((URIObject) typeProperty.getObject()).getLabels()
						.stream().map(Literal::getValue)
				)
				.collect(Collectors.toList());
	}

	private List<String> getCommentFromCommentProperties(List<Property> commentProperties) {
		if (commentProperties == null) {
			return null;
		}
		return commentProperties.stream()
				.map(commentProperty -> ((LiteralObject) commentProperty.getObject()).getValue())
				.collect(Collectors.toList());
	}
}