package org.aksw.sante.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MaximumWeightedCache <S,P> extends HashMap<S, P> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2899935050694697665L;
	private List<WeightedObject<S>> weighObjects = new ArrayList<WeightedObject<S>>();
	private int maxSize = 10;
	private Comparator<WeightedObject<S>> comparator = null;
			
	public MaximumWeightedCache(int maxSize, Comparator<WeightedObject<S>> comparator) {
		this.maxSize = maxSize;
		this.comparator = comparator;
	}
	
	public P put(S key, P value, double weight) {
		if(weighObjects.size() == maxSize) {
			S loswestWeightedKey = weighObjects.get(maxSize-1).getKey();
			remove(loswestWeightedKey);
		}
		WeightedObject<S> wO = new WeightedObject<S>(key, weight);
		Collections.sort(weighObjects, comparator);
		weighObjects.add(wO);
		return super.put(key, value);
	}
	
	public P get(Object key) {
		return super.get(key);
	}
	
}
