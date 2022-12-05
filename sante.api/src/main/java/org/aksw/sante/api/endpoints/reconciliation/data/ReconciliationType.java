package org.aksw.sante.api.endpoints.reconciliation.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * A type to restrict reconciliation queries.
 */
@Getter
@Setter
public class ReconciliationType {

	/**
	 * The ID of the type.
	 */
	@NotEmpty
	private String id;

	/**
	 * The name of the type.
	 */
	@NotEmpty
	private String name;

	/**
	 * A list of broader reconciliation types.
	 */
	private List<ReconciliationType> broader;
}
