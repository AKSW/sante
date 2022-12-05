package org.aksw.sante.api.endpoints.santesuggest.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Candidate that represents a single SANTé suggestion result.
 */
@Getter
@Setter
public class SanteSuggestCandidate {

	/**
	 * URI that represents the suggestion.
	 */
	private String suggestion;

	/**
	 * Score of the candidate.
	 */
	private String score;
}
