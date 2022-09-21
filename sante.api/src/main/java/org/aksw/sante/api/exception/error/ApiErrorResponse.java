package org.aksw.sante.api.exception.error;

import lombok.Getter;

@Getter
public class ApiErrorResponse {
	private final ApiError apiError;

	public ApiErrorResponse(ApiError apiError) {
		this.apiError = apiError;
	}
}
