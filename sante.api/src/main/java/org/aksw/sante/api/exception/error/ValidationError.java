package org.aksw.sante.api.exception.error;

public class ValidationError extends ReportableError {
	private final String field;

	public ValidationError(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}

	public String getField() {
		return this.field;
	}
}
