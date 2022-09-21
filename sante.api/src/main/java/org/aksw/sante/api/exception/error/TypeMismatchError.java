package org.aksw.sante.api.exception.error;

public class TypeMismatchError extends ReportableError {
	private final String parameter;

	public TypeMismatchError(String parameter, String message) {
		super();
		this.parameter = parameter;
		this.message = message;
	}

	public String getParameter() {
		return this.parameter;
	}
}
