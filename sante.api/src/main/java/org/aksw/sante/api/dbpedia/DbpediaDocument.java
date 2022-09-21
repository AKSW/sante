package org.aksw.sante.api.dbpedia;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

/**
 * Record class for encapsulating data in the same form as returned by DBpedia-Lookup.
 * Attention: All private fields are lists since this is the way DBpedia-Lookup is formatted.
 * All fields can only be assigned (through the builder) with an entire list.
 * Since a DbpediaDocument is not supposed to change once it is created, there is no need to change those lists
 * on an atomic (by element) level.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class DbpediaDocument {
	/**
	 * Score of the document.
	 */
	private List<String> score;

	/**
	 * ? TODO
	 */
	private List<String> refCount;

	/**
	 * TODO
	 */
	private List<String> resource;

	/**
	 * TODO
	 */
	private List<String> redirectLabel;

	/**
	 * TODO
	 */
	private List<String> typeName;

	/**
	 * TODO
	 */
	private List<String> comment;

	/**
	 * TODO
	 */
	private List<String> label;

	/**
	 * TODO
	 */
	private List<String> type;

	/**
	 * TODO
	 */
	private List<String> category;

	/**
	 * Constructs the document.
	 * Can not be called externally.
	 */
	private DbpediaDocument() {
	}

	/**
	 * Constructs the document using the given builder.
	 *
	 * @param builder builder to create a DbpediaDocument.
	 */
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

	/**
	 * Builder to construct DbpediaDocument objects dynamically.
	 */
	public static class DbpediaDocumentBuilder {

		/**
		 * TODO
		 */
		private List<String> score;

		/**
		 * TODO
		 */
		private List<String> refCount;

		/**
		 * TODO
		 */
		private List<String> resource;

		/**
		 * TODO
		 */
		private List<String> redirectLabel;

		/**
		 * TODO
		 */
		private List<String> typeName;

		/**
		 * TODO
		 */
		private List<String> comment;

		/**
		 * TODO
		 */
		private List<String> label;

		/**
		 * TODO
		 */
		private List<String> type;

		/**
		 * TODO
		 */
		private List<String> category;

		/**
		 * Constructs the builder.
		 * Can not be called externally.
		 */
		private DbpediaDocumentBuilder() {
		}

		/**
		 * Wraps the constructor in static method that initiates the building process of a DbpediaDocument.
		 *
		 * @return a new DbpediaDocumentBuilder
		 */
		public static DbpediaDocumentBuilder newDbpediaDocument() {
			return new DbpediaDocumentBuilder();
		}

		/**
		 * Adds a score to a DbpediaDocument.
		 *
		 * @param score score to be added
		 * @return      this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addScore(List<String> score) {
			this.score = score;
			return this;
		}

		/**
		 * TODO
		 * Adds a refCount to a DbpediaDocument.
		 *
		 * @param refCount  refCount to be added
		 * @return          this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addRefCount(List<String> refCount) {
			this.refCount = refCount;
			return this;
		}

		/**
		 * TODO
		 * Adds a resource to a DbpediaDocument.
		 *
		 * @param resource  resource to be added
		 * @return          this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addResource(List<String> resource) {
			this.resource = resource;
			return this;
		}

		/**
		 * TODO
		 * Adds a redirectLabel to a DbpediaDocument.
		 *
		 * @param redirectLabel redirectLabel to be added
		 * @return              this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addRedirectLabel(List<String> redirectLabel) {
			this.redirectLabel = redirectLabel;
			return this;
		}

		/**
		 * TODO
		 * Adds a typeName to a DbpediaDocument.
		 *
		 * @param typeName  typeName to be added
		 * @return          this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addTypeName(List<String> typeName) {
			this.typeName = typeName;
			return this;
		}

		/**
		 * TODO
		 * Adds a comment to a DbpediaDocument.
		 *
		 * @param comment   comment to be added
		 * @return          this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addComment(List<String> comment) {
			this.comment = comment;
			return this;
		}

		/**
		 * TODO
		 * Adds a label to a DbpediaDocument.
		 *
		 * @param label label to be added
		 * @return      this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addLabel(List<String> label) {
			this.label = label;
			return this;
		}

		/**
		 * TODO
		 * Adds a type to a DbpediaDocument.
		 *
		 * @param type  type to be added
		 * @return      this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addType(List<String> type) {
			this.type = type;
			return this;
		}

		/**
		 * TODO
		 * Adds a category to a DbpediaDocument.
		 *
		 * @param category  category to be added
		 * @return          this DbpediaDocumentBuilder
		 */
		public DbpediaDocumentBuilder addCategory(List<String> category) {
			this.category = category;
			return this;
		}

		/**
		 * Creates the DbpediaDocument once the building process is done.
		 *
		 * @return a new DbpediaDocument
		 */
		public DbpediaDocument create() {
			return new DbpediaDocument(this);
		}
	}
}
