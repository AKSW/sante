package org.aksw.sante.api.endpoints.reconciliation.data.result;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Final result of the reconciliation process.
 */
@Getter
@Setter
public class ReconciliationResult {

	/**
	 * A set of possible candidates.
	 */
	private final Set<ReconciliationCandidate> result;

	/**
	 * Constructs a ReconciliationResult and initialises the result set.
	 */
	public ReconciliationResult() {
		this.result = new HashSet<>();
	}

	/**
	 * Adds a candidate to the result set.
	 *
	 * @param candidate a result candidate
	 */
	public void addReconciliationCandidate(ReconciliationCandidate candidate) {
		result.add(candidate);
	}
}
