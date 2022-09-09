package org.aksw.sante.api;

import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.SearchEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Configuration
public class Config {

	@Bean
	public SearchEngine getSearchEngine() throws IOException {
		Path indexPath = (new File(SmileParams.getInstance().indexPath)).toPath();
		IndexReader reader = SearchEngine.newReader(indexPath);
		return new SearchEngine(reader);
	}
}
