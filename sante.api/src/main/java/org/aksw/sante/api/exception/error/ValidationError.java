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
	 * @param message detailed information about the validation issue
	 * @param field   invalid field.
	 */
	public ValidationError(String message, String field) {
		super(message);
		this.field = field;
	}
}
