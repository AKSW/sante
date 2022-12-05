package org.aksw.sante.api.wrapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aksw.sante.api.wrapper.mixin.PatternFilterMixin;
import org.aksw.sante.api.wrapper.mixin.SanteFilterMixin;
import org.aksw.sante.api.wrapper.mixin.TriplePatternFilterMixin;
import org.aksw.sante.api.wrapper.mixin.UriFilterMixin;
import org.sante.lucene.Filter;
import org.sante.lucene.PatternFilter;
import org.sante.lucene.TriplePatternFilter;
import org.sante.lucene.URIFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the wrapper logic.
 */
@Configuration
public class WrapperConfiguration {

	/**
	 * Configures the jackson object mapper that is used by default.
	 * Connects all the custom mixins with their target classes.
	 *
	 * @param defaultObjectMapper the object mapper that already exists in the IoC container
	 */
	@SuppressWarnings("unused")
	@Autowired
	public void configureObjectMapper(ObjectMapper defaultObjectMapper) {
		defaultObjectMapper.addMixIn(Filter.class, SanteFilterMixin.class);
		defaultObjectMapper.addMixIn(PatternFilter.class, PatternFilterMixin.class);
		defaultObjectMapper.addMixIn(TriplePatternFilter.class, TriplePatternFilterMixin.class);
		defaultObjectMapper.addMixIn(URIFilter.class, UriFilterMixin.class);
	}
}
