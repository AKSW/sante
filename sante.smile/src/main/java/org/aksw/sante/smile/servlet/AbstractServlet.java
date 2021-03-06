package org.aksw.sante.smile.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.json.JSONObject;

public abstract class AbstractServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5040053314053103548L;

	protected List<String> parseList(String array) {
		if(array != null) {
			return Arrays.asList(array.split(","));
		}
		return new ArrayList<String>();
	}
	
	protected Set<String> parseSet(String array) {
		if(array != null) {
			return new HashSet<String>(parseList(array));
		}
		return new HashSet<String>();
	}
	
	protected Integer parseInteger(String integer, Integer defaultValue) {
		if(integer == null) {
			return defaultValue;
		}
		return Integer.parseInt(integer);
	}
	
	protected String asJson(Map<String, Object> params) {
		JSONObject object = new JSONObject();
		for(Entry<String, Object> entry: params.entrySet()) {
			if(entry.getValue() != null) {
				object.put(entry.getKey(), entry.getValue());
			}
		}
		return object.toString();
	}
}
