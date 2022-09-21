package org.aksw.sante.api.exception.error;

public abstract class ReportableError {
	protected String type;
	protected String message;

	ReportableError() {
		this.type = this.getClass().getSimpleName();
	}

	public String getType() {
		return this.type;
	}

	public String getMessage() {
		return this.message;
	}
}
