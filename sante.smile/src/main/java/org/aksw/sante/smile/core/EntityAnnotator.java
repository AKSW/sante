package org.aksw.sante.smile.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;

public class EntityAnnotator extends Annotation<EntityAnnotator.TermAnnotation> {
	
	public Set<PropertyObjectAnnotation> annotate(AbstractEntityWrapper e, String query, Analyzer analyzer) {
		Set<PropertyObjectAnnotation> annotations = new HashSet<PropertyObjectAnnotation>();
		for(PropertyWrapper p : e.getProperties()) {
			PropertyObjectAnnotation pAnnotation = new PropertyObjectAnnotation(p, analyzer);
			try {
				pAnnotation.annotate(query);
				for(String term : pAnnotation.getTerms()) {
					TermAnnotation tAnnotation = get(term);
					TermAnnotation pTAnnotation = new TermAnnotation();
					pTAnnotation.score = pAnnotation.get(term);
					pTAnnotation.annotation = pAnnotation;
					if(tAnnotation == null || pTAnnotation.score > tAnnotation.score) {
						if(tAnnotation != null) {
							tAnnotation.annotation.remove(term);
						}
						put(term, pTAnnotation);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Set<String> terms = getTerms();
		for(String term : terms) {
			TermAnnotation termAnnotation = get(term);
			annotations.add(termAnnotation.annotation);
		}
		return annotations;
	}
	
	protected class TermAnnotation {
		double score;
		PropertyObjectAnnotation annotation;
	}
}
