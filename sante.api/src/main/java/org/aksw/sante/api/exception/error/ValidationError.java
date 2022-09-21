package org.aksw.sante.api.exception.error;

import lombok.Getter;

@Getter
public class ValidationError extends ReportableError {
	private final String field;

	public ValidationError(String field, String message) {
		super();
		this.field = field;
		this.message = message;
	}
}
