package org.aksw.sante.smile.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmileParams {
	
	@Value("${sante.index.path:#{null}}")
	public String indexPath = null;

	@Value("${sante.properties.labeling:#{null}}")
	public volatile String jsonLabelingProperties = null;
	
	@Value("${sante.properties.image:#{null}}")
	public volatile String jsonImageProperties = null;
	
	@Value("${sante.properties.hidden:#{null}}")
	public volatile String jsonHiddenProperties = null;
	
	@Value("${sante.properties.abstract:#{null}}")
	public volatile String jsonAbstractProperties = null;
	
	@Value("${sante.search.prefixes:#{null}}")
	public volatile String jsonSearchPrefixes = null;
	
	public volatile String[] imageProperties = null;
	public volatile String[] abstractProperties = null;
	public volatile String[] labelingProperties = null;
	public volatile String[] relevancePropertyOrder = null;
	public volatile String[] hiddenProperties = null;
	public volatile List<String> prefixes = null;
	
	@PostConstruct
	public void init() {
		hiddenProperties = stringJSONArray(jsonHiddenProperties);
		imageProperties = stringJSONArray(jsonImageProperties);
		labelingProperties = stringJSONArray(jsonLabelingProperties);
		abstractProperties = stringJSONArray(jsonAbstractProperties);
		String[] prefixesArray = stringJSONArray(jsonSearchPrefixes);
		prefixes = Arrays.asList(prefixesArray);
	}
	
	private static String[] stringJSONArray(String value) {
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
