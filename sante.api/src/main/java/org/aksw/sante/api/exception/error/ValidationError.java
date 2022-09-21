package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates an issue with a request validation.
 * @see ReportableError
 */
@Getter
public class ValidationError extends ReportableError {
	/**
	 * Invalid field.
	 */
	private final String field;

	/**
	 * Constructs a ValidationError and adds the required information.
	 *
	 * @param field     invalid field.
	 * @param message   detailed information about the validation issue
	 */
	public ValidationError(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}
}
