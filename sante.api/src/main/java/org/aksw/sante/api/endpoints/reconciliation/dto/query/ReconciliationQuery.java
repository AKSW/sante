package org.aksw.sante.api.endpoints.reconciliation.dto.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.aksw.sante.api.endpoints.reconciliation.dto.ReconciliationProperty;
import org.aksw.sante.api.endpoints.reconciliation.dto.ReconciliationType;
import org.aksw.sante.api.wrapper.Query;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A query with all necessary properties for reconciliation.
 */
@Getter
@Setter
public class ReconciliationQuery {

	/**
	 * A query string.
	 */
	@NotEmpty
	private String query;

	/**
	 * A List of types that the query is reduced to.
	 */
	private List<@Valid ReconciliationType> type;

	/**
	 * A maximum amount of result candidates that are to be returned.
	 */
	@Min(value = 1, message = "limit but be set greater than 1")
	private int limit;

	/**
	 * A List of properties that possible candidates must possess.
	 */
	private List<@Valid ReconciliationProperty> properties;

	/**
	 * Returns the names of the types as strings.
	 *
	 * @return a set of type names.
	 */
	public Set<String> returnTypeNames() {
		if (this.type != null) {
			return this.type.stream().map(ReconciliationType::getName).collect(Collectors.toSet());
		}
		return null;
	}

	@JsonIgnore
	public Query toQuery() {

		Query query = new Query();

		query.setQ(this.getQuery());
		query.setOffset(0); // Reconciliation does not define an offset
		query.setLimit(this.getLimit());
		query.setPrefixes(new HashSet<>()); // Reconciliation does not define prefixes
		query.setClasses(this.returnTypeNames());
		query.setFilters(new HashSet<>()); // Reconciliation does not define filters
		query.setContent(new HashSet<>(Set.of("score", "sort"))); // Reconciliation demands a score and recommends sort

		return query;
	}
}
