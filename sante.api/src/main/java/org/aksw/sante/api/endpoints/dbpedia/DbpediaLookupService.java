package org.aksw.sante.api.endpoints.dbpedia;

import org.aksw.sante.api.endpoints.dbpedia.data.DbpediaDocument;
import org.aksw.sante.api.endpoints.dbpedia.data.DbpediaDocumentCollection;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.Query;
import org.aksw.sante.api.wrapper.provider.SearchProvider;
import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Property;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.URIObject;
import org.aksw.sante.entity.LiteralObject;
import org.sante.lucene.ResultSet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * The service to perform searches similar to DBpedia-Lookup.
 */
@Service
public class DbpediaLookupService {

	private final SearchProvider searchProvider;

	/**
	 * The URI for RDF Schema types.
	 */
	private static final String RDF_SCHEMA_TYPE = "http://www.w3.org/2000/01/rdf-schema#type";

	/**
	 * The URI for RDF Schema comments.
	 */
	private static final String RDF_COMMENT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#comment";

	/**
	 * Constructs the service and injects the necessary dependencies.
	 */
	public DbpediaLookupService(@Qualifier("dbpedia-lookup") SearchProvider searchProvider) {
		this.searchProvider = searchProvider;
	}

	/**
	 * Performs the search and returns the result as a DbpediaDocumentCollection of DbpediaDocument objects.
	 *
	 * @param maxHits       limit the amount of search results
	 * @param searchQuery   the search string. If empty, the search will not be reduced to any search string.
	 * @param searchClasses comma-separated list of classes that are to be searched
	 * @return a DbpediaDocumentCollection of DbpediaDocument objects
	 * @throws SearchSuggestException if any issues arises during search
	 */
	public DbpediaDocumentCollection lookupDbpedia(
			Integer maxHits,
			String searchQuery,
			Set<String> searchClasses
	) throws SearchSuggestException {
		try {

			DbpediaDocumentCollection dbpediaDocumentCollection = new DbpediaDocumentCollection();

			Query query = new Query();
			query.setLimit(maxHits);
			query.setQ(searchQuery);
			query.setClasses(searchClasses);

			ResultSet<Entity> results = this.searchProvider.search(query);

			while (results.hasNext()) {
				Entity entity = results.next();
				List<Property> typeProperties = entity.getProperties(RDF_SCHEMA_TYPE);
				List<Property> commentProperties = entity.getProperties(RDF_COMMENT);

				DbpediaDocument.DbpediaDocumentBuilder doc = DbpediaDocument.DbpediaDocumentBuilder.newDbpediaDocument()
						.addResource(Collections.singletonList(entity.getURI()))
						.addScore(Collections.singletonList(Float.toString(results.score())));

				if (entity.getLabels() != null) {
					doc.addLabel(entity.getLabels().stream().map(Literal::getValue).collect(Collectors.toList()));
				}

				doc.addType(this.getTypeFromTypeProperties(typeProperties))
						.addTypeName(this.getTypeNameFromTypeProperties(typeProperties))
						.addComment(this.getCommentFromCommentProperties(commentProperties));

				dbpediaDocumentCollection.addDocument(doc.create());
			}
			return dbpediaDocumentCollection;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Extract the type from given type properties.
	 *
	 * @param typeProperties list of type properties
	 * @return type. Formatted as a list of types with a single element.
	 */
	private List<String> getTypeFromTypeProperties(List<Property> typeProperties) {
		if (typeProperties == null) {
			return null;
		}
		return typeProperties.stream()
				.map(typeProperty -> typeProperty.getObject().getURI())
				.collect(Collectors.toList());
	}

	/**
	 * Extract the type name from given type properties.
	 *
	 * @param typeProperties list of type properties
	 * @return type name. Formatted as a list of types with a single element.
	 */
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

	/**
	 * Extract comments from given comment properties.
	 *
	 * @param commentProperties list of comment properties
	 * @return list of comments
	 */
	private List<String> getCommentFromCommentProperties(List<Property> commentProperties) {
		if (commentProperties == null) {
			return null;
		}
		return commentProperties.stream()
				.map(commentProperty -> ((LiteralObject) commentProperty.getObject()).getValue())
				.collect(Collectors.toList());
	}
}