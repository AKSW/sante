package org.aksw.sante.core;

import java.util.Map;

public class Namespacer {
	
	public Map<String, String> namespaces;
	
	public Namespacer(Map<String, String> namespaces) {
		this.namespaces = namespaces;
	}
	
	public String rename(String uri) {
		return namespaces.get(uri);
	}

}
