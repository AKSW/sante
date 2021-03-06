package org.aksw.sante.smile.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private Map<String, String> resourceLabels = new HashMap<String, String>();
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
		addAnnotation(propertyURI, propertyURI, query);
		addAnnotation(propertyURI, property, query);
		for(ObjectWrapper object : property.getObjects()) {
			if(object.isLiteral()) {
				Literal l = object.getLabels().iterator().next();
				addAnnotation(propertyURI, l.getValue(), query);
			} else {
				addAnnotation(propertyURI, object, query);
				addAnnotation(propertyURI, object.getURI(), query);
			}
		}
	}
	
	private void addAnnotation(String propertyURI, Resource r, String query) throws IOException {
		Set<Literal> literals = r.getLabels();
		for(Literal l : literals) {
			addAnnotation(propertyURI, l.getValue(), query);
		}
	}
	
	private void addAnnotation(String propertyURI, String string, String query) throws IOException {
		String label = string;
		String[] labelTokens = NLPUtils.tokens("default", label, analyzer);
		String[] queryTokens = NLPUtils.tokens("default", query, analyzer);
		List<String> queryTokenList = new ArrayList<String>(Arrays.asList(queryTokens));
		List<String> labelTokenList = new ArrayList<String>(Arrays.asList(labelTokens));
		retainAll(queryTokenList, labelTokenList);
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
				resourceLabels.put(token, label);
			}
		}
	}
	
	public List<String> retainAll(List<String> query, List<String> label) {
		int i = 0;
		while(i < label.size()) {
			String labelToken = label.get(i);
			boolean contains = false;
			for(String queryToken : query) {
				if(labelToken.startsWith(queryToken)) {
					contains = true;
					break;
				}
			}
			if(contains) {
				i++;
			} else {
				label.remove(i);
			}			
		}
		return label;
	}
	
	public void remove(String term) {
		resourceLabels.remove(term);
		super.remove(term);
	}
}
