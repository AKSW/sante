package org.aksw.sante.smile.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Annotation <T extends Object> {
	private Map<String, T> termScore = new HashMap<String, T>();
	
	public void put(String term, T score) {
		termScore.put(term, score);
	}
	
	public Set<String> getTerms() {
		return termScore.keySet();
	}
	
	public void remove(String term) {
		termScore.remove(term);
	}
	
	public boolean isEmpty() {
		return termScore.isEmpty();
	}
	
	public T get(String term) {
		return termScore.get(term);
	}
}
