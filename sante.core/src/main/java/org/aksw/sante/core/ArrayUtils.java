package org.aksw.sante.core;

import java.util.Arrays;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {
	public static boolean containPattern(String[] patterns, String[] array) {
		for(String entry : array) {
			for(String pattern : patterns) {
				if(entry.contains(pattern)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean containPattern(String[] patterns, String element) {
		for(String pattern : patterns) {
			if(element.contains(pattern)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containAllPatterns(String[] patterns, String[] array) {
		return Arrays.asList(patterns).containsAll(Arrays.asList(array)) && 
				Arrays.asList(array).containsAll(Arrays.asList(patterns));
	}
}
