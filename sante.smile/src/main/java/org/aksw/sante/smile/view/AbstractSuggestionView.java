package org.aksw.sante.smile.view;

import java.io.Serializable;
import  org.sante.lucene.Filter;

public abstract class AbstractSuggestionView implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 500909578408547295L;
	private Integer resultId;
	private String id;
	private String title;
	private String subtitle;
	private String html;
	private String query;

	public AbstractSuggestionView(int resultId, 
			String query,
			String title,
			String subtitle) {
		this.resultId = resultId;
		this.title = title;
		this.subtitle = subtitle;
		this.query = query;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getHtml() {
		return this.html;
	}
	
	public void setHtml(String html) {
		this.html = html;
	}
	
	public abstract Filter getFilter();

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
}
