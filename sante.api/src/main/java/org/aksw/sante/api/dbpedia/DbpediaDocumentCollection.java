package org.aksw.sante.api.dbpedia;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DbpediaDocumentCollection {
	private final List<DbpediaDocument> docs;

	public DbpediaDocumentCollection() {
		this.docs = new ArrayList<>();
	}

	public void addDocument(DbpediaDocument doc) {
		this.docs.add(doc);
	}
}
