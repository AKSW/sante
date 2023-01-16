package org.aksw.sante.api.endpoints.resource;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the Resource endpoint.
 */
@Configuration
public class ResourceConfiguration {

	/**
	 * Returns an instantiated UrlValidator.
	 *
	 * @return an instantiated UrlValidator.
	 */
	@Bean
	UrlValidator getUrlValidator() {
		return new UrlValidator();
	}
}
