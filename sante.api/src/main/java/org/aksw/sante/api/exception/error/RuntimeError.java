package org.aksw.sante.api.exception.error;

/**
 * An error that that indicates an issue during runtime.
 *
 * @see ReportableError
 */
public class RuntimeError extends ReportableError {

	/**
	 * Constructs a JsonDeserializationError and adds the required information.
	 *
	 * @param message detailed information about the issue
	 */
	public RuntimeError(String message) {
		super(message);
	}
}
