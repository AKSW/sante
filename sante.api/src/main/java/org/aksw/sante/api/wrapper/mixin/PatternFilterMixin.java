package org.aksw.sante.api.wrapper.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Jackson mixin that exists only for annotation purposes.
 */
@SuppressWarnings("unused")
public abstract class PatternFilterMixin {

	/**
	 * Pattern that is supposed to be filtered for.
	 */
	@JsonSerialize
	private String pattern;

	/**
	 * Constructs a PatternFilterMixin.
	 * Can not be called since class is abstract.
	 * Exists for annotation purposes only.
	 *
	 * @param pattern pattern that is supposed to be filtered for
	 */
	@JsonCreator
	public PatternFilterMixin(@JsonProperty("pattern") String pattern) {
	}
}
