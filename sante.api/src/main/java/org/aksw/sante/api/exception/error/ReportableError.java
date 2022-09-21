package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * Base class for specific errors that can be reported.
 */
@Getter
public abstract class ReportableError {
	/**
	 * Type of the specific error.
	 */
	protected String type;

	/**
	 * Specific error message that gives details about the error.
	 */
	protected String message;

	/**
	 * Constructs a ReportableError and sets the type to name of the class.
	 */
	public ReportableError() {
		this.type = this.getClass().getSimpleName();
	}
}
