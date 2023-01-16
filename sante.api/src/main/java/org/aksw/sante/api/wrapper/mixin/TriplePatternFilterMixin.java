package org.aksw.sante.api.wrapper.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Jackson mixin that exists only for annotation purposes.
 */
@SuppressWarnings("unused")
public abstract class TriplePatternFilterMixin {

	/**
	 * The URI of the subject.
	 */
	@JsonSerialize
	private String subjectURI;

	/**
	 * The URI of the predicate.
	 */
	@JsonSerialize
	private String predicateURI;

	/**
	 * The URI of the object.
	 */
	@JsonSerialize
	private String objectURI;

	/**
	 * Constructs a TriplePatternFilterMixin.
	 * Can not be called since class is abstract.
	 * Exists for annotation purposes only.
	 *
	 * @param subjectURI   subject URI
	 * @param predicateURI predicate URI
	 * @param objectURI    object URI
	 */
	@JsonCreator
	public TriplePatternFilterMixin(
			@JsonProperty("subjectUri") String subjectURI,
			@JsonProperty("predicateUri") String predicateURI,
			@JsonProperty("objectUri") String objectURI
	) {
	}
}
