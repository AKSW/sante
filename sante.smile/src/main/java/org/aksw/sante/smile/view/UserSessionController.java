package org.aksw.sante.smile.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.aksw.sante.smile.core.SmileParams;
import org.apache.log4j.Logger;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;

@Named
@SessionScoped
public class UserSessionController {
	
	private static Logger logger = Logger.getLogger(UserSessionController.class);
	
	@Autowired
    public SmileParams smileParams;
	
	private List<String> langs;
	private List<String> langOptions;
	private String lang;
	
	public UserSessionController() {
	}
	
	@PostConstruct
	public void init() {
		try {
			this.langs = setToList(SearchEngine.getLangs(new File(smileParams.indexPath)));
		} catch (IOException e) {
			logger.error("Error loading lanagues.", e);
		}
		this.langOptions = this.langs;
	}
	
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public String getLang() {
		return lang;
	}
	
	public List<String> getLangOptions() {
		return langOptions;
	}

	public List<String> getLangs() {
		List<String> displayLangs = new ArrayList<String>();
		if(lang != null) { // re-order
			displayLangs.remove(lang);
			displayLangs.add(0, lang);
		}
		return displayLangs;
	}
	
	private List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<String>();
		for(String entry : set) {
			list.add(entry);
		}
		return list;
	}
}
