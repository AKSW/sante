package org.aksw.sante.api.dbpedia;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

	public List<String> getScore() {
		return score;
	}

	public void setScore(List<String> score) {
		this.score = score;
	}

	public List<String> getRefCount() {
		return refCount;
	}

	public void setRefCount(List<String> refCount) {
		this.refCount = refCount;
	}

	public List<String> getResource() {
		return resource;
	}

	public void setResource(List<String> resource) {
		this.resource = resource;
	}

	public List<String> getRedirectLabel() {
		return redirectLabel;
	}

	public void setRedirectLabel(List<String> redirectLabel) {
		this.redirectLabel = redirectLabel;
	}

	public List<String> getTypeName() {
		return typeName;
	}

	public void setTypeName(List<String> typeName) {
		this.typeName = typeName;
	}

	public List<String> getComment() {
		return comment;
	}

	public void setComment(List<String> comment) {
		this.comment = comment;
	}

	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> label) {
		this.label = label;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}
}
