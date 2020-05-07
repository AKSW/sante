package org.aksw.sante.smile.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.Resource;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.sante.lucene.NLPUtils;

public class PropertyObjectAnnotation extends Annotation<Double> {
	private PropertyWrapper property = null;
	private Map<String, Literal> resourceLabels = new HashMap<String, Literal>();
	private Analyzer analyzer = new EnglishAnalyzer();
	private Map<String, Double> boosts = new HashMap<String, Double>();
	{
		boosts.put("http://www.w3.org/2000/01/rdf-schema#label", 4.);
		boosts.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", 6.);
		boosts.put("http://www.w3.org/2000/01/rdf-schema#Property", 5.);
	}
	
	public PropertyObjectAnnotation(PropertyWrapper property, Analyzer analyzer) {
		this.property = property;
		this.analyzer = analyzer;
	}
	
	public PropertyWrapper getProperty() {
		return property;
	}

	public void annotate(String query) throws IOException {
		String propertyURI = property.getURI();
		addAnnotation(propertyURI, property, query);
		for(ObjectWrapper object : property.getObjects()) {
			if(object.isLiteral()) {
				Literal l = object.getLabels().iterator().next();
				addAnnotation(propertyURI, property, l, query);
			} else {
				addAnnotation(propertyURI, object, query);
			}
		}
	}
	
	private void addAnnotation(String propertyURI, Resource r, String query) throws IOException {
		Set<Literal> literals = r.getLabels();
		for(Literal l : literals) {
			addAnnotation(propertyURI, r, l, query);
		}
	}
	
	private void addAnnotation(String propertyURI, Resource r, Literal l, String query) throws IOException {
		String label = l.getValue();
		String[] labelTokens = NLPUtils.tokens("default", label, analyzer);
		String[] queryTokens = NLPUtils.tokens("default", query, analyzer);
		List<String> queryTokenList = new ArrayList<String>(Arrays.asList(queryTokens));
		List<String> labelTokenList = new ArrayList<String>(Arrays.asList(labelTokens));
		labelTokenList.retainAll(queryTokenList);
		Double score = (double) (labelTokenList.size() / labelTokens.length);
		if(score == 1) {
			score = Math.pow(score, labelTokens.length);
		} else {
			double intersection = labelTokenList.size() - queryTokenList.size();
			score = intersection / (queryTokenList.size() + labelTokenList.size() - intersection);
		}
		Double boost = boosts.get(propertyURI);
		if(boost == null) {
			boost = 0.;
		}
		score = score * 100;
		double tScore = (score / labelTokenList.size()) + boost;
		for(String token : labelTokenList) {
			Double pTScore = get(token);
			if(pTScore == null || pTScore < tScore) {
				put(token, tScore);
				resourceLabels.put(token, l);
			}
		}
	}
	
	public void remove(String term) {
		resourceLabels.remove(term);
		super.remove(term);
	}
	
	public Collection<Literal> getResourceLabels() {
		return resourceLabels.values();
	}
}
