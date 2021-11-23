package org.aksw.sante.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
				String encodedValue = encode(value);
				return "\"" + encodedValue + "\"";
		} else if (lang == null || lang.isEmpty()) {
			return "\"" + value + "\"^^"  + type;
		}
		String encodedValue = encode(value);
		return "\"" + encodedValue + "\"@"  + lang;
	}
	
	private String encode(String value) {
		try {
			String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
			return encodedValue;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	@Override
	public int hashCode() {
		String hashCode = "literal" + getURI();
		return hashCode.hashCode();
	}
}
