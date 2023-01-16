package org.aksw.sante.api.wrapper.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Jackson mixin that exists only for annotation purposes.
 */
@SuppressWarnings("unused")
public abstract class UriFilterMixin {

	/**
	 * URI that is to be filtered for.
	 */
	@JsonSerialize
	private String uri;

	/**
	 * Constructs a UriFilterMixin.
	 * Can not be called since class is abstract.
	 * Exists for annotation purposes only.
	 *
	 * @param uri uri that is to be filtered for
	 */
	@JsonCreator
	public UriFilterMixin(@JsonProperty("uri") String uri) {
	}
}
