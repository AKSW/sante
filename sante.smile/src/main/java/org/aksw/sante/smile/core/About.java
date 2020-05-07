package org.aksw.sante.smile.core;

import org.aksw.sante.entity.Entity;

public class About extends IndexedEntityWrapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1817785137105077673L;
	Entity e = null;
	public About(Entity entity) {
		super(entity);
		e = entity;
	}
	
	public String getTwitter() {
		return e.getPropertyObjectValue("Twitter", null);
	}
	
	public String getAuthor() {
		return e.getPropertyObjectValue("Author", null);
	}
	
	public String getRepo() {
		return e.getPropertyObjectValue("Repository", null);
	}
	
	public String getLicense() {
		return e.getPropertyObjectValue("License", null);
	}

	public String getIssues() {
		return e.getPropertyObjectValue("Issues", null);
	}
}
