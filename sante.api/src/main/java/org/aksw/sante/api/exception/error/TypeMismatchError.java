package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates a type mismatch.
 *
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
	 * @param message   detailed information about the type mismatch
	 * @param parameter parameter that had its type mismatched
	 */
	public TypeMismatchError(String message, String parameter) {
		super(message);
		this.parameter = parameter;
	}
}
