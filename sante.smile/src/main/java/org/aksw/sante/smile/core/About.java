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
		return e.getPropertyObjectValue("Twitter", "ANY");
	}
	
	public String getAuthor() {
		return e.getPropertyObjectValue("Author", "ANY");
	}
	
	public String getRepo() {
		return e.getPropertyObjectValue("Repository", "ANY");
	}
	
	public String getLicense() {
		return e.getPropertyObjectValue("License", "ANY");
	}

	public String getIssues() {
		return e.getPropertyObjectValue("Issues", "ANY");
	}
}
