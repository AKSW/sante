package org.aksw.sante.api.endpoints.reconciliation.data.result;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Feature of a ReconciliationCandidate.
 */
@Getter
@Setter
public class ReconciliationCandidateFeature {

	/**
	 * An ID of the feature.
	 */
	@NotEmpty
	private String id;

	/**
	 * A value of the feature.
	 */
	@NotEmpty
	private String value;
}
