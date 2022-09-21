package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates a type mismatch.
 * @see ReportableError
 */
@Getter
public class TypeMismatchError extends ReportableError {
	/**
	 * Parameter that had its type mismatched.
	 */
	private final String parameter;

	/**
	 * Constructs a TypeMismatchError and adds the required information.
	 *
	 * @param parameter parameter that had its type mismatched
	 * @param message   detailed information about the type mismatch
	 */
	public TypeMismatchError(String parameter, String message) {
		super();
		this.parameter = parameter;
		this.message = message;
	}
}
