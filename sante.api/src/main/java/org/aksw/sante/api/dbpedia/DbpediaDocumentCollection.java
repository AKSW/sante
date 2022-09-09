package org.aksw.sante.api.dbpedia;

import java.util.ArrayList;
import java.util.List;

public class DbpediaDocumentCollection {
	public DbpediaDocumentCollection() {
		this.docs = new ArrayList<>();
	}

	public List<DbpediaDocument> getDocs() {
		return docs;
	}

	public void setDocs(List<DbpediaDocument> docs) {
		this.docs = docs;
	}

	private List<DbpediaDocument> docs;

	public void addToDocs(DbpediaDocument doc) {
		this.docs.add(doc);
	}
}
