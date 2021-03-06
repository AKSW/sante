package org.aksw.sante.smile.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.aksw.sante.smile.view.SearchViewController;
import org.json.JSONArray;

public class SmileParams {
	
	private static SmileParams smileParams = null;
	
	public volatile String indexPath = null;
	public volatile String[] labelingProperties = null;
	public volatile String[] imageProperties = null;
	public volatile String[] abstractProperties = null;
	public volatile String[] relevancePropertyOrder = null;
	public volatile String[] hidenProperties = null;
	public volatile List<String> prefixes = null;

	public SmileParams() {
		try (InputStream is = SearchViewController.class.getResourceAsStream("/config.properties");) {
			Properties properties = new Properties();
			properties.load(is);
			indexPath = properties.getProperty("index.dir");
			hidenProperties = loadJSONArray("properties.hidden", properties);
			imageProperties = loadJSONArray("properties.image", properties);
			labelingProperties = loadJSONArray("properties.labeling", properties);
			abstractProperties = loadJSONArray("properties.abstract", properties);
			String[] prefixesArray = loadJSONArray("search.prefixes", properties);
			prefixes = Arrays.asList(prefixesArray);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static SmileParams getInstance() {
		if(smileParams == null) {
			smileParams = new SmileParams();
		}
		return smileParams;
	}
	
	private static String[] loadJSONArray(String property, Properties properties) {
		String value = properties.getProperty(property);
		if(value == null || value.isEmpty()) {
			return new String[]{};
		}
		JSONArray valueArray = new JSONArray(value);
		List<String> entries = new ArrayList<String>();
		for(Object v : valueArray) {
			entries.add(v.toString());
		}
		return entries.toArray(new String[entries.size()]);
	}
}
