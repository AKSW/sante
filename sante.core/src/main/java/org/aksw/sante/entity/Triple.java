package org.aksw.sante.entity;

import java.io.Serializable;
import java.util.Set;

public class Triple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4312054040598192705L;
	private Property p = null;
	private Resource subject = null;
	
	public Triple(String subjectUri, 
			Set<Literal> subjectLabels, 
			Property p) {
		this.subject = new Resource(subjectUri, subjectLabels);
		this.p = p;
	}
	
	public Property getProperty() {
		return p;
	}
	
	public Resource getSubject() {
		return subject;
	}
}
