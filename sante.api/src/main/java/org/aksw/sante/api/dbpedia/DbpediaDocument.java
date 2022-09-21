package org.aksw.sante.api.dbpedia;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class DbpediaDocument {
	private List<String> score;
	private List<String> refCount;
	private List<String> resource;
	private List<String> redirectLabel;
	private List<String> typeName;
	private List<String> comment;
	private List<String> label;
	private List<String> type;
	private List<String> category;

	private DbpediaDocument() {}

	private DbpediaDocument(DbpediaDocumentBuilder builder) {
		this();
		this.score = builder.score;
		this.refCount = builder.refCount;
		this.resource = builder.resource;
		this.redirectLabel = builder.redirectLabel;
		this.typeName = builder.typeName;
		this.comment = builder.comment;
		this.label = builder.label;
		this.type = builder.type;
		this.category = builder.category;
	}

	public static class DbpediaDocumentBuilder {
		private List<String> score;
		private List<String> refCount;
		private List<String> resource;
		private List<String> redirectLabel;
		private List<String> typeName;
		private List<String> comment;
		private List<String> label;
		private List<String> type;
		private List<String> category;

		private DbpediaDocumentBuilder() {}

		public static DbpediaDocumentBuilder newDbpediaDocument() {
			return new DbpediaDocumentBuilder();
		}

		public DbpediaDocumentBuilder addScore(List<String> score) {
			this.score = score;
			return this;
		}

		public DbpediaDocumentBuilder addRefCount(List<String> refCount) {
			this.refCount = refCount;
			return this;
		}

		public DbpediaDocumentBuilder addResource(List<String> resource) {
			this.resource = resource;
			return this;
		}

		public DbpediaDocumentBuilder addRedirectLabel(List<String> redirectLabel) {
			this.redirectLabel = redirectLabel;
			return this;
		}

		public DbpediaDocumentBuilder addTypeName(List<String> typeName) {
			this.typeName = typeName;
			return this;
		}

		public DbpediaDocumentBuilder addComment(List<String> comment) {
			this.comment = comment;
			return this;
		}

		public DbpediaDocumentBuilder addLabel(List<String> label) {
			this.label = label;
			return this;
		}

		public DbpediaDocumentBuilder addType(List<String> type) {
			this.type = type;
			return this;
		}

		public DbpediaDocumentBuilder addCategory(List<String> category) {
			this.category = category;
			return this;
		}

		public DbpediaDocument create() {
			return new DbpediaDocument(this);
		}
	}
}
