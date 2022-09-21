package org.aksw.sante.api.exception.error;

import lombok.Getter;

@Getter
public abstract class ReportableError {
	protected String type;
	protected String message;

	ReportableError() {
		this.type = this.getClass().getSimpleName();
	}
}
