package org.aksw.sante.api.endpoints.santesearch.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Candidate that represents a single SANTé search result.
 */
@Setter
@Getter
public class SanteSearchCandidate {

	/**
	 * URI that represents the candidate.
	 */
	private String uri;

	/**
	 * Corresponding instance of the candidate.
	 */
	private String instance;

	/**
	 * Score of the candidate.
	 */
	private String score;
}
