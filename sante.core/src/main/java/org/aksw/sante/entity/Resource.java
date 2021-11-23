package org.aksw.sante.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

public class Resource implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8827347876563115625L;
	
	private Set<Literal> labels = null;
	private String uri = null;
	
	public Resource(String uri) {
		this.uri = uri;
	}
	
	public Resource(String uri, List<Literal> labels) {
		addLabels(labels);
		this.uri = uri;
	}
	
	public Resource(String uri, Set<Literal> labels) {
		this.labels = labels;
		this.uri = uri;
	}
	
	private void init() {
		if(this.labels == null) {
			this.labels = new HashSet<Literal>();
		}
	}
	
	public void addLabels(Collection<Literal> labels) {
		init();
		this.labels.addAll(labels);
	}
	
	public void addLabel(Literal label) {
		init();
		this.labels.add(label);
	}
	
	public String getURI() {
		return uri;
	}
	
	public Set<Literal> getLabels() {
		return labels;
	}
	
	public Set<String> getStringLabels() {
		Set<String> stringLiterals = new HashSet<String>();
		if(labels == null) {
			return stringLiterals;
		}
		for(Literal literal : labels) {
			stringLiterals.add(literal.getValue());
		}
		return stringLiterals;
	}
	
	public Set<Literal> getLabels(String... langs) {
		if(labels == null) {
			return null;
		}
		Set<Literal> langLiterals = new HashSet<Literal>();
		for(Literal literal : labels) {
			if(ArrayUtils.contains(langs, literal.getLang())) {
				langLiterals.add(literal);
			}
		}
		return langLiterals;
	}
	
	public String getLabel(String... langs) {
		Set<Literal> labels = getLabels(langs);
		if(labels != null && labels.size() > 0) {
			return labels.iterator().next().getValue();
		}
		return null;
	}
	
	public String getLabel() {
		if(labels != null && labels.size() > 0) {
			return labels.iterator().next().getValue();
		}
		return null;
	}
	
	public String getLabel(String[] langs, String defaultValue) {
		String label = getLabel(langs);
		if(label == null) {
			return defaultValue;
		}
		return label;
	}
}
