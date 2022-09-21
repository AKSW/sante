package org.aksw.sante.api.dbpedia;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection-wrapper for DbpediaDocument objects.
 *
 * @see DbpediaDocument
 */
@Getter
public class DbpediaDocumentCollection {

	/**
	 * A collection of DbpediaDocument objects.
	 */
	private final List<DbpediaDocument> docs;

	/**
	 * Constructs a DbpediaDocumentCollection.
	 * Creates an empty list for the internal list of documents.
	 */
	public DbpediaDocumentCollection() {
		this.docs = new ArrayList<>();
	}

	/**
	 * Adds a single document to the collection.
	 *
	 * @param doc a single DbpediaDocument
	 */
	public void addDocument(DbpediaDocument doc) {
		this.docs.add(doc);
	}
}
