package org.aksw.sante.api.exception.error;

public class ApiErrorResponse {
	private ApiError apiError;

	public ApiErrorResponse(ApiError apiError) {
		this.apiError = apiError;
	}

	public ApiError getApiError() {
		return this.apiError;
	}
}
