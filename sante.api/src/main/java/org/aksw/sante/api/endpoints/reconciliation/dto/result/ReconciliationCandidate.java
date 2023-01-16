package org.aksw.sante.api.endpoints.reconciliation.dto.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Candidate for a reconciliation result.
 */
@Getter
@Setter
public class ReconciliationCandidate {

	/**
	 * An ID of the candidate.
	 */
	private String id;

	/**
	 * A name of the candidate
	 */
	private String name;

	/**
	 * A score of the candidate that indicates how good it is matched.
	 */
	private double score;

	/**
	 * A list of features of the candidate.
	 */
	private List<ReconciliationCandidateFeature> features;

	/**
	 * A boolean that indicates if the candidate can be considered a match.
	 */
	private boolean match;

	/**
	 * Constructs a ReconciliationCandidate.
	 *
	 * @param id    id of the candidate
	 * @param name  name of the candidate
	 * @param score score of the match
	 * @param match indicator of an appropriate match
	 */
	public ReconciliationCandidate(
			String id,
			String name,
			double score,
			boolean match
	) {
		this.id = id;
		this.name = name;
		this.score = score;
		this.match = match;
	}
}
