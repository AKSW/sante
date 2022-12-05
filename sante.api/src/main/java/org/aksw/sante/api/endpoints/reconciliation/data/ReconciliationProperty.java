package org.aksw.sante.api.endpoints.reconciliation.data;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * Property of a reconciliation attempt.
 */
@Getter
@Setter
public class ReconciliationProperty {

	/**
	 * The ID of the property.
	 */
	@NotEmpty
	private String pid;

	/**
	 * The value of the property.
	 */
	@NotEmpty
	private String v;
}
