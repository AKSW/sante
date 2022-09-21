package org.aksw.sante.api.configuration;

import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.SearchEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Configuration class for Spring beans.
 */
@Configuration
public class BeanConfig {

	// TODO decouple dependency on implicitly specified index path in SANTe.smile
	/**
	 * Configure a SearchEngine object for proper dependency injection.
	 *
	 * @return              a properly instantiated SearchEngine
	 * @throws IOException  if there is an issue with the locally (as a file) stored index
	 * @see SearchEngine
	 */
	@Bean
	public SearchEngine getSearchEngine() throws IOException {
		Path indexPath = (new File(SmileParams.getInstance().indexPath)).toPath();
		IndexReader reader = SearchEngine.newReader(indexPath);
		return new SearchEngine(reader);
	}
}
