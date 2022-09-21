package org.aksw.sante.api.exception.error;

import lombok.Getter;

@Getter
public class TypeMismatchError extends ReportableError {
	private final String parameter;

	public TypeMismatchError(String parameter, String message) {
		super();
		this.parameter = parameter;
		this.message = message;
	}
}
