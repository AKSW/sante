package org.aksw.sante.entity;

import java.io.Serializable;
import java.util.List;

public class Property extends Resource implements Serializable {

	private Object value = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -5861527474766081544L;


	public Property(String uri, List<Literal> labels, String oURI, List<Literal> oLabels) {
		super(uri, labels);
		this.value = new URIObject(oURI, oLabels);
	}
	
	public Property(String uri, List<Literal> labels, String literal, String lang) {
		super(uri, labels);
		this.value = new LiteralObject(literal, lang);
	}
	
	public Property(String uri, List<Literal> labels, LiteralObject literal) {
		super(uri, labels);
		this.value = literal;
	}
	
	public Property(String uri, Literal label, LiteralObject object) {
		super(uri);
		addLabel(label);
		this.value = object;
	}
	
	public Object getObject() {
		return value;
	}
	
	public String asTriple(String subject) {
		String triple =  escape(subject) + " " 
				+ escape(getURI()) + " ";
		if(this.value.isLiteral()) {
			triple += this.value.getURI();
		} else {
			triple += escape(this.value.getURI()); 
		}
		return triple;
	}
	
	private String escape(String uri) {
		return "<" + uri + ">";
	}
}