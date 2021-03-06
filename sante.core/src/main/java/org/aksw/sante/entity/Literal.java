package org.aksw.sante.entity;

import java.io.Serializable;

import org.apache.commons.text.StringEscapeUtils;

public class Literal implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2614926764025752197L;
	private String value;
	private String lang;
	private String type;
	
	public Literal() {
	}
	
	public Literal(Literal l) {
		this.setType(l.getType());
		this.setLang(l.getLang());
		this.setValue(l.getValue());
	}
	
	public Literal(String literal, String lang) {
		this.setLang(lang);
		this.setValue(literal);
	}
		
	public Literal(String literal) {
		this.setValue(literal);
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getLang() {
		return lang;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getURI() {
		if(type == null
				&& value == null
				&& lang == null) {
			return "";
		} else if((type == null || type.isEmpty()) 
				&& (lang == null || lang.isEmpty())) {
			return "\"" + StringEscapeUtils.escapeJava(value) + "\"";
		} else if (lang == null || lang.isEmpty()) {
			return "\"" + value + "\"^^"  + type;
		}
		return "\"" + value + "\"@"  + lang;
	}
	
	@Override
	public int hashCode() {
		String hashCode = "literal" + getURI();
		return hashCode.hashCode();
	}
}
