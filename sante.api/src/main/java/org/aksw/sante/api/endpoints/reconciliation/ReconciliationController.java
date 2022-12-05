package org.aksw.sante.api.endpoints.reconciliation;

import org.aksw.sante.api.endpoints.reconciliation.data.query.ReconciliationQuery;
import org.aksw.sante.api.endpoints.reconciliation.data.result.ReconciliationResult;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * Controller for the reconciliation endpoint.
 */
@RestController
@Validated
public class ReconciliationController implements ReconciliationOperations {

	/**
	 * The service that performs the reconciliation.
	 */
	private final ReconciliationService reconciliationService;

	/**
	 * Constructs the ReconciliationController.
	 *
	 * @param reconciliationService the reconciliation service
	 */
	@Autowired
	public ReconciliationController(ReconciliationService reconciliationService) {
		this.reconciliationService = reconciliationService;
	}

	/**
	 * Performs a batch reconciliation using a batch of ReconciliationQueries.
	 *
	 * @param queryBatch a batch of reconciliation queries from the request body
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises during reconciliation (search)
	 */
	@Override
	public Map<String, ReconciliationResult> batchReconcilePost(
			Map<String, @Valid ReconciliationQuery> queryBatch
	) throws SearchSuggestException {
		return this.reconciliationService.reconcileBatch(queryBatch);
	}

	/**
	 * Performs a batch reconciliation using a batch of ReconciliationQueries,
	 * formatted as a single JSON-encoded string.
	 *
	 * @param queries string representing a batch of reconciliation queries
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises during reconciliation (search)
	 */
	@Override
	public Map<String, ReconciliationResult> batchReconcileGet(String queries) throws SearchSuggestException {
		return this.reconciliationService.reconcileBatch(queries);
	}
}
