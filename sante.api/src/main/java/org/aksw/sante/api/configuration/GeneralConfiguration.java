package org.aksw.sante.api.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Spring beans.
 */
@Configuration
public class GeneralConfiguration {

	/**
	 * Configures the jackson object mapper that is used by default.
	 * Sets deserialization to fail on unknown properties
	 * and to exclude null-valued properties during serialization.
	 *
	 * @param defaultObjectMapper the object mapper that already exists in the IoC container
	 */
	@Autowired
	public void configureObjectMapper(ObjectMapper defaultObjectMapper) {
		defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		defaultObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
}
