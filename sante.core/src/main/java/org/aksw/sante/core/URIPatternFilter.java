package org.aksw.sante.core;

public class URIPatternFilter {
	private String[] patternIn = null;
	private String[] patternOut = null;
		
	public URIPatternFilter() {
	}
	
	public URIPatternFilter(String[] patternIn) {
		this.patternIn = patternIn;
	}
	
	public URIPatternFilter(String[] patternIn, String[] patternOut) {
		this.patternIn = patternIn;
		this.patternOut = patternOut;
	}
	
	public void setPatternIn(String[] patternIn) {
		this.patternIn = patternIn;
	}
	
	public void setPatternOut(String[] patternOut) {
		this.patternOut = patternOut;
	}
	
	public boolean evalute(String uri) {
			return ((patternIn == null || ArrayUtils.containPattern(patternIn, uri)) && 
					(patternOut == null || !ArrayUtils.containPattern(patternOut, uri)));
	}
}
