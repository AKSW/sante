package org.aksw.sante.core;

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
}
