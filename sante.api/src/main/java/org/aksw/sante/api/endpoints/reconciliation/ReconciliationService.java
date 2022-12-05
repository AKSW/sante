package org.aksw.sante.api.endpoints.reconciliation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aksw.sante.api.endpoints.reconciliation.data.result.ReconciliationResult;
import org.aksw.sante.api.endpoints.reconciliation.data.query.ReconciliationQuery;
import org.aksw.sante.api.endpoints.reconciliation.data.result.ReconciliationCandidate;
import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.wrapper.provider.SearchProvider;
import org.aksw.sante.entity.Entity;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Reconciliation service that performs the reconciliation using SANTe.
 *
 * @see SearchEngine
 */
@Service
public class ReconciliationService {

	/**
	 * The provider that actually executes the search.
	 */
	private final SearchProvider searchProvider;

	/**
	 * The object mapper to deserialize a string into an object that can be reconciled.
	 */
	private final ObjectMapper objectMapper;

	private final Validator validator;

	/**
	 * Constructs the ReconciliationService and inject the necessary dependencies.
	 *
	 * @param searchProvider provider that executes the search
	 * @param objectMapper   the object mapper to deserialize a string
	 */
	@Autowired
	public ReconciliationService(
			@Qualifier("reconciliation") SearchProvider searchProvider,
			ObjectMapper objectMapper,
			Validator validator
	) {
		this.searchProvider = searchProvider;
		this.objectMapper = objectMapper;
		this.validator = validator;
	}

	/**
	 * Performs the reconciliation of a single query.
	 *
	 * @param reconciliationQuery query object for the reconciliation
	 * @return a reconciliation result
	 * @throws SearchSuggestException if any issue arises during search
	 */
	private ReconciliationResult reconcile(ReconciliationQuery reconciliationQuery) throws SearchSuggestException {

		ReconciliationResult result = new ReconciliationResult();

		ResultSet<Entity> resultSet = this.searchProvider.search(reconciliationQuery.toQuery());

		while (resultSet.hasNext()) {
			Entity entity = resultSet.next();

			try {
				result.addReconciliationCandidate(new ReconciliationCandidate(
						entity.getURI(),
						entity.getLabel(),
						resultSet.score(),
						true // match since it would not find a result if it wasn't a match
				));
			} catch (IOException exception) {
				throw new RuntimeException(exception);
			}
		}
		return result;
	}

	/**
	 * Performs the reconciliation of a batch of queries.
	 *
	 * @param queryBatch batch of reconciliation queries
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises during search
	 */
	public Map<String, ReconciliationResult> reconcileBatch(Map<String, ReconciliationQuery> queryBatch) throws SearchSuggestException {
		Map<String, ReconciliationResult> resultBatch = new HashMap<>();
		for (Map.Entry<String, ReconciliationQuery> entry : queryBatch.entrySet()) {
			String reconciliationQueryId = entry.getKey();
			ReconciliationQuery reconciliationQuery = entry.getValue();
			resultBatch.put(reconciliationQueryId, this.reconcile(reconciliationQuery));
		}
		return resultBatch;
	}

	/**
	 * Performs the reconciliation of a string that represents a batch of queries.
	 *
	 * @param queryBatch string that represents a batch of reconciliation queries
	 * @return a batch of reconciliation results
	 * @throws SearchSuggestException if any issue arises during search
	 */
	public Map<String, ReconciliationResult> reconcileBatch(String queryBatch) throws SearchSuggestException {

		try {
			TypeReference<HashMap<String, ReconciliationQuery>> typeRef = new TypeReference<>() {
			};
			Map<String, ReconciliationQuery> map = this.objectMapper.readValue(queryBatch, typeRef);

			for (ReconciliationQuery reconciliationQuery : map.values()) {
				Set<ConstraintViolation<ReconciliationQuery>> violations = this.validator.validate(reconciliationQuery);
				if (!violations.isEmpty()) {
					StringBuilder stringBuilder = new StringBuilder();
					for (ConstraintViolation<ReconciliationQuery> constraintViolation : violations) {
						stringBuilder.append(constraintViolation.getMessage());
					}
					throw new ConstraintViolationException(
							"Constraint violations occurred: " + stringBuilder, violations
					);
				}
			}
			return this.reconcileBatch(map);
		} catch (JsonProcessingException exception) {
			throw new RuntimeException(exception);
		}
	}
}
